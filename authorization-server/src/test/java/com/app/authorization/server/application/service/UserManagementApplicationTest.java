package com.app.authorization.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.app.authorization.server.application.dto.command.RegisterUserCommand;
import com.app.authorization.server.application.dto.response.RegisterUserResponse;
import com.app.authorization.server.application.dto.response.UserResponse;
import com.app.authorization.server.application.dto.response.UserSummaryResponse;
import com.app.authorization.server.application.mapper.UserMapper;
import com.app.authorization.server.domain.exception.UserAlreadyExistsException;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.valueobject.Password;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.PasswordHasher;
import com.app.authorization.server.domain.repository.UIViewRepository;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserManagementApplicationTest {

  @Mock UserRepository userRepository;
  @Mock PasswordHasher passwordHasher;
  @Mock UserMapper userMapper;
  @Mock UserAccessProfilePort userAccessProfilePort;
  @Mock UIViewRepository uiViewRepository;

  private UserManagementApplication service;

  @BeforeEach
  void setUp() {
    service = new UserManagementApplication(
      userRepository, passwordHasher, userMapper, userAccessProfilePort, uiViewRepository);
  }

  // --- register ---

  @Test
  void register_username_already_exists_throws() {
    User existing = user(1, "johndoe");
    given(userRepository.findByUsername("johndoe")).willReturn(Optional.of(existing));

    RegisterUserCommand cmd = new RegisterUserCommand("johndoe", "secret123", "John Doe", "j@x.com");

    assertThatThrownBy(() -> service.register(cmd))
      .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  void register_valid_saves_and_returns_response() {
    given(userRepository.findByUsername("newuser")).willReturn(Optional.empty());
    given(userRepository.findByEmail("newuser")).willReturn(Optional.empty());
    given(passwordHasher.hash("secret123")).willReturn("hashed");
    User savedUser = user(2, "newuser");
    given(userRepository.save(any())).willReturn(savedUser);
    RegisterUserResponse expected = new RegisterUserResponse("newuser");
    given(userMapper.toRegisterUserResponse("newuser")).willReturn(expected);

    RegisterUserCommand cmd = new RegisterUserCommand("newuser", "secret123", "New User", "n@x.com");
    RegisterUserResponse response = service.register(cmd);

    assertThat(response.getUsername()).isEqualTo("newuser");
    verify(userRepository).save(any());
  }

  // --- findByUsername ---

  @Test
  void findByUsername_not_found_throws() {
    given(userRepository.findByUsername("ghost")).willReturn(Optional.empty());

    assertThatThrownBy(() -> service.findByUsername("ghost"))
      .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  void findByUsername_found_returns_response() {
    User existing = user(1, "johndoe");
    given(userRepository.findByUsername("johndoe")).willReturn(Optional.of(existing));
    UserResponse expected = new UserResponse(1, "johndoe", "j@x.com", "John Doe", false, true);
    given(userMapper.toUserResponse(existing)).willReturn(expected);

    UserResponse response = service.findByUsername("johndoe");

    assertThat(response.getUsername()).isEqualTo("johndoe");
  }

  // --- findByUserId ---

  @Test
  void findByUserId_not_found_throws() {
    given(userRepository.findById(any())).willReturn(Optional.empty());

    assertThatThrownBy(() -> service.findByUserId(99))
      .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  void findByUserId_found_returns_summary() {
    User existing = user(1, "johndoe");
    given(userRepository.findById(any())).willReturn(Optional.of(existing));
    UserSummaryResponse expected = new UserSummaryResponse(1, "johndoe", "John Doe");
    given(userMapper.toUserSummaryResponse(existing)).willReturn(expected);

    UserSummaryResponse response = service.findByUserId(1);

    assertThat(response.getId()).isEqualTo(1);
  }

  // --- helpers ---

  private User user(int id, String username) {
    return User.rehydrate(new UserID(id), username, Password.fromHash("hashed"),
      "John Doe", username + "@x.com", false, null);
  }
}
