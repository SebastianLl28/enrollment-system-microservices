package com.app.authorization.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.app.authorization.server.application.dto.command.TwoFactorValidateCommand;
import com.app.authorization.server.application.dto.command.VerifyTwoFactorCommand;
import com.app.authorization.server.application.dto.response.TwoFactorConfirmResponse;
import com.app.authorization.server.application.dto.response.TwoFactorInitResponse;
import com.app.authorization.server.application.dto.response.VerifyTwoFactorResponse;
import com.app.authorization.server.domain.exception.InvalidOperationException;
import com.app.authorization.server.domain.exception.InvalidTwoFactorCodeException;
import com.app.authorization.server.domain.exception.InvalidTwoFactorTokenException;
import com.app.authorization.server.domain.exception.TwoFactorNotInitiatedException;
import com.app.authorization.server.domain.exception.UserNotFoundException;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.model.valueobject.Password;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.AccessTokenPort;
import com.app.authorization.server.domain.repository.TwoFactorPort;
import com.app.authorization.server.domain.repository.TwoFactorTokenPort;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TwoFactorApplicationServiceTest {

  @Mock UserRepository userRepository;
  @Mock TwoFactorPort twoFactorPort;
  @Mock TwoFactorTokenPort twoFactorTokenPort;
  @Mock AccessTokenPort accessTokenPort;
  @Mock UserAccessProfilePort userAccessProfilePort;

  private TwoFactorApplicationService service;

  @BeforeEach
  void setUp() {
    service = new TwoFactorApplicationService(
      userRepository, twoFactorPort, twoFactorTokenPort, accessTokenPort, userAccessProfilePort);
  }

  // --- initiate ---

  @Test
  void initiate_user_not_found_throws() {
    given(userRepository.findByUsername("ghost")).willReturn(Optional.empty());

    assertThatThrownBy(() -> service.initiate("ghost"))
      .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void initiate_already_enabled_throws_invalid_operation() {
    User user = userWith2fa("admin", true, "SECRET");
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));

    assertThatThrownBy(() -> service.initiate("admin"))
      .isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void initiate_read_only_user_throws_invalid_operation() {
    User user = userWithout2fa("admin");
    UserAccessProfile readOnlyProfile = new UserAccessProfile(1, "admin",
      Set.of("ROLE_VIEWER"), Set.of("STUDENT:READ:ALL"), Set.of());
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
    given(userAccessProfilePort.loadByUsername("admin")).willReturn(readOnlyProfile);

    assertThatThrownBy(() -> service.initiate("admin"))
      .isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void initiate_valid_user_generates_secret_and_url() {
    User user = userWithout2fa("admin");
    UserAccessProfile profile = new UserAccessProfile(1, "admin",
      Set.of("ROLE_ADMIN"), Set.of("STUDENT:CREATE:ALL"), Set.of());
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
    given(userAccessProfilePort.loadByUsername("admin")).willReturn(profile);
    given(twoFactorPort.generateSecret("admin")).willReturn("TOTP_SECRET");
    given(twoFactorPort.buildOtpAuthUrl("admin", "TOTP_SECRET")).willReturn("otpauth://totp/...");

    TwoFactorInitResponse response = service.initiate("admin");

    assertThat(response.getSecret()).isEqualTo("TOTP_SECRET");
    assertThat(response.getOtpauthUrl()).isEqualTo("otpauth://totp/...");
    verify(userRepository).updateTwoFactor(user.getId(), false, "TOTP_SECRET");
  }

  // --- validate (confirmar código al activar 2FA) ---

  @Test
  void validate_user_not_found_throws() {
    given(userRepository.findByUsername("ghost")).willReturn(Optional.empty());

    assertThatThrownBy(() ->
      service.validate("ghost", new TwoFactorValidateCommand("123456")))
      .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void validate_secret_not_initiated_throws() {
    User user = userWithout2fa("admin");
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));

    assertThatThrownBy(() ->
      service.validate("admin", new TwoFactorValidateCommand("123456")))
      .isInstanceOf(TwoFactorNotInitiatedException.class);
  }

  @Test
  void validate_wrong_code_throws_invalid_code() {
    User user = userWith2fa("admin", false, "SECRET");
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
    given(twoFactorPort.verifyCode("SECRET", "000000")).willReturn(false);

    assertThatThrownBy(() ->
      service.validate("admin", new TwoFactorValidateCommand("000000")))
      .isInstanceOf(InvalidTwoFactorCodeException.class);
  }

  @Test
  void validate_correct_code_enables_2fa() {
    User user = userWith2fa("admin", false, "SECRET");
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
    given(twoFactorPort.verifyCode("SECRET", "123456")).willReturn(true);

    TwoFactorConfirmResponse response = service.validate("admin",
      new TwoFactorValidateCommand("123456"));

    assertThat(response.isTwoFactorEnabled()).isTrue();
    verify(userRepository).updateTwoFactor(user.getId(), true, "SECRET");
  }

  // --- verify (login con código 2FA) ---

  @Test
  void verify_invalid_temp_token_throws() {
    given(twoFactorTokenPort.isTwoFactorToken("bad-token")).willReturn(false);

    assertThatThrownBy(() ->
      service.verify(new VerifyTwoFactorCommand("123456", "bad-token")))
      .isInstanceOf(InvalidTwoFactorTokenException.class);
  }

  @Test
  void verify_user_not_found_throws() {
    given(twoFactorTokenPort.isTwoFactorToken("temp")).willReturn(true);
    given(accessTokenPort.extractUsername("temp")).willReturn("ghost");
    given(userRepository.findByUsername("ghost")).willReturn(Optional.empty());

    assertThatThrownBy(() ->
      service.verify(new VerifyTwoFactorCommand("123456", "temp")))
      .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void verify_wrong_code_throws_invalid_code() {
    User user = userWith2fa("admin", true, "SECRET");
    given(twoFactorTokenPort.isTwoFactorToken("temp")).willReturn(true);
    given(accessTokenPort.extractUsername("temp")).willReturn("admin");
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
    given(twoFactorPort.verifyCode("SECRET", "000000")).willReturn(false);

    assertThatThrownBy(() ->
      service.verify(new VerifyTwoFactorCommand("000000", "temp")))
      .isInstanceOf(InvalidTwoFactorCodeException.class);
  }

  @Test
  void verify_valid_code_returns_access_token() {
    User user = userWith2fa("admin", true, "SECRET");
    UserAccessProfile profile = new UserAccessProfile(1, "admin",
      Set.of("ROLE_ADMIN"), Set.of("STUDENT:READ:ALL"), Set.of());
    given(twoFactorTokenPort.isTwoFactorToken("temp")).willReturn(true);
    given(accessTokenPort.extractUsername("temp")).willReturn("admin");
    given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));
    given(twoFactorPort.verifyCode("SECRET", "123456")).willReturn(true);
    given(userAccessProfilePort.loadByUsername("admin")).willReturn(profile);
    given(accessTokenPort.generateToken("admin", profile.getPermissions())).willReturn("jwt");

    VerifyTwoFactorResponse response = service.verify(new VerifyTwoFactorCommand("123456", "temp"));

    assertThat(response.getAccessToken()).isEqualTo("jwt");
    assertThat(response.getUsername()).isEqualTo("admin");
    verify(accessTokenPort, never()).validateToken("temp");
  }

  // --- helpers ---

  private User userWithout2fa(String username) {
    return User.rehydrate(new UserID(1), username, Password.fromHash("hashed"),
      "Full Name", username + "@test.com", false, null);
  }

  private User userWith2fa(String username, boolean enabled, String secret) {
    return User.rehydrate(new UserID(1), username, Password.fromHash("hashed"),
      "Full Name", username + "@test.com", enabled, secret);
  }
}
