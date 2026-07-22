package com.app.authorization.server.application.service;

import com.app.authorization.server.application.dto.command.LoginCommand;
import com.app.authorization.server.application.dto.response.LoginResponse;
import com.app.authorization.server.application.dto.response.ValidateTokenResponse;
import com.app.authorization.server.application.mapper.AuthMapper;
import com.app.authorization.server.application.mapper.UserMapper;
import com.app.authorization.server.domain.exception.InvalidCredentialsException;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.model.valueobject.Password;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.AccessTokenPort;
import com.app.authorization.server.domain.repository.PasswordHasher;
import com.app.authorization.server.domain.repository.TwoFactorTokenPort;
import com.app.authorization.server.domain.repository.UIViewRepository;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordHasher passwordHasher;
    @Mock AccessTokenPort accessTokenPort;
    @Mock TwoFactorTokenPort twoFactorTokenPort;
    @Mock UserAccessProfilePort userAccessProfilePort;
    @Mock UIViewRepository uiViewRepository;
    @Mock AuthMapper authMapper;
    @Mock UserMapper userMapper;

    private AuthApplicationService service;

    @BeforeEach
    void setUp() {
        service = new AuthApplicationService(
                userRepository, passwordHasher, accessTokenPort, authMapper,
                twoFactorTokenPort, userMapper, userAccessProfilePort, uiViewRepository);
    }

    // --- login ---

    @Test
    void login_valid_credentials_without_2fa_returns_access_token() {
        User user = userWithout2fa();
        UserAccessProfile profile = profile("admin");
        given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
        given(passwordHasher.verify("pass", "hashed")).willReturn(true);
        given(userAccessProfilePort.loadByUsername("admin")).willReturn(profile);
        given(accessTokenPort.generateToken("admin", profile.getPermissions())).willReturn("jwt");
        LoginResponse expected = new LoginResponse("jwt", "admin", false);
        given(authMapper.toLoginResponse(user, "jwt")).willReturn(expected);

        LoginResponse result = service.login(new LoginCommand("admin", "pass"));

        assertThat(result.getToken()).isEqualTo("jwt");
        verify(twoFactorTokenPort, never()).generateTwoFactorToken("admin", profile.getPermissions());
    }

    @Test
    void login_valid_credentials_with_2fa_returns_2fa_token() {
        User user = userWith2fa();
        UserAccessProfile profile = profile("admin");
        given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
        given(passwordHasher.verify("pass", "hashed")).willReturn(true);
        given(userAccessProfilePort.loadByUsername("admin")).willReturn(profile);
        given(twoFactorTokenPort.generateTwoFactorToken("admin", profile.getPermissions())).willReturn("2fa-token");
        LoginResponse expected = new LoginResponse("2fa-token", "admin", true);
        given(authMapper.toLoginResponse(user, "2fa-token")).willReturn(expected);

        LoginResponse result = service.login(new LoginCommand("admin", "pass"));

        assertThat(result.getTwoFactorRequired()).isTrue();
        verify(accessTokenPort, never()).generateToken("admin", profile.getPermissions());
    }

    @Test
    void login_unknown_user_throws_invalid_credentials() {
        given(userRepository.findByUsername("ghost")).willReturn(Optional.empty());
        var cmd = new LoginCommand("ghost", "pass");

        assertThatThrownBy(() -> service.login(cmd))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_wrong_password_throws_invalid_credentials() {
        User user = userWithout2fa();
        given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
        given(passwordHasher.verify("wrong", "hashed")).willReturn(false);
        var cmd = new LoginCommand("admin", "wrong");

        assertThatThrownBy(() -> service.login(cmd))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    // --- validateToken ---

    @Test
    void validateToken_invalid_token_returns_empty_response() {
        given(accessTokenPort.validateToken("bad")).willReturn(false);
        ValidateTokenResponse empty = new ValidateTokenResponse(null, false, null, Set.of(), Set.of());
        given(authMapper.toValidateTokenResponse(null, Set.of(), Set.of())).willReturn(empty);

        ValidateTokenResponse result = service.validateToken("bad");

        assertThat(result.isValid()).isFalse();
    }

    @Test
    void validateToken_2fa_token_is_rejected_for_security() {
        // Un token temporal de 2FA no debe conceder acceso a /api/**
        given(accessTokenPort.validateToken("2fa-temp")).willReturn(true);
        given(twoFactorTokenPort.isTwoFactorToken("2fa-temp")).willReturn(true);
        ValidateTokenResponse empty = new ValidateTokenResponse(null, false, null, Set.of(), Set.of());
        given(authMapper.toValidateTokenResponse(null, Set.of(), Set.of())).willReturn(empty);

        ValidateTokenResponse result = service.validateToken("2fa-temp");

        assertThat(result.isValid()).isFalse();
    }

    @Test
    void validateToken_valid_token_returns_user_and_permissions() {
        User user = userWithout2fa();
        UserAccessProfile profile = profile("admin");
        given(accessTokenPort.validateToken("good")).willReturn(true);
        given(twoFactorTokenPort.isTwoFactorToken("good")).willReturn(false);
        given(accessTokenPort.extractUsername("good")).willReturn("admin");
        given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
        given(userAccessProfilePort.loadByUsername("admin")).willReturn(profile);
        given(uiViewRepository.findByUserId(user.getId())).willReturn(Set.of());
        ValidateTokenResponse valid = new ValidateTokenResponse("admin", true, 1, Set.of("PERM"), Set.of());
        given(authMapper.toValidateTokenResponse(user, profile.getPermissions(), Set.of())).willReturn(valid);

        ValidateTokenResponse result = service.validateToken("good");

        assertThat(result.isValid()).isTrue();
        assertThat(result.getUsername()).isEqualTo("admin");
    }

    // --- helpers ---

    private User userWithout2fa() {
        Password pwd = Password.fromHash("hashed");
        return User.rehydrate(new UserID(1), "admin", pwd, "Admin User", "admin@test.com", false, null);
    }

    private User userWith2fa() {
        Password pwd = Password.fromHash("hashed");
        return User.rehydrate(new UserID(1), "admin", pwd, "Admin User", "admin@test.com", true, "TOTP_SECRET");
    }

    private UserAccessProfile profile(String username) {
        return new UserAccessProfile(1, username, Set.of("ROLE_ADMIN"), Set.of("PERM_READ"), Set.of("DASHBOARD"));
    }
}
