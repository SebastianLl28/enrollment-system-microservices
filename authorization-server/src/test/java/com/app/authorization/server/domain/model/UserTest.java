package com.app.authorization.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.authorization.server.domain.exception.InvalidFullNameException;
import com.app.authorization.server.domain.exception.InvalidUsernameException;
import com.app.authorization.server.domain.exception.TwoFactorNotInitializedException;
import com.app.authorization.server.domain.model.valueobject.Password;
import com.app.authorization.server.domain.model.valueobject.UserID;
import org.junit.jupiter.api.Test;

class UserTest {

  private static final Password PWD = Password.fromHash("$2a$10$hash");

  @Test
  void createWithPasswordBuildsUser() {
    User user = User.create("johndoe", PWD, "John Doe", "john@example.com");

    assertThat(user.getUsername()).isEqualTo("johndoe");
    assertThat(user.getFullName()).isEqualTo("John Doe");
    assertThat(user.getEmail()).isEqualTo("john@example.com");
    assertThat(user.getId()).isNull();
  }

  @Test
  void createOAuthBuildsUserWithoutPassword() {
    User user = User.create("johndoe", "John Doe", "john@example.com");

    assertThat(user.getUsername()).isEqualTo("johndoe");
    assertThat(user.hasPassword()).isFalse();
  }

  @Test
  void createRequiresUsername() {
    assertThatThrownBy(() -> User.create("", PWD, "John Doe", "john@example.com"))
      .isInstanceOf(InvalidUsernameException.class);
  }

  @Test
  void createRequiresNonBlankUsername() {
    assertThatThrownBy(() -> User.create("   ", PWD, "John Doe", "john@example.com"))
      .isInstanceOf(InvalidUsernameException.class);
  }

  @Test
  void createRequiresFullName() {
    assertThatThrownBy(() -> User.create("johndoe", PWD, "", "john@example.com"))
      .isInstanceOf(InvalidFullNameException.class);
  }

  @Test
  void createRequiresNonBlankFullName() {
    assertThatThrownBy(() -> User.create("johndoe", PWD, "   ", "john@example.com"))
      .isInstanceOf(InvalidFullNameException.class);
  }

  @Test
  void hasPasswordReturnsTrueWhenPasswordSet() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, false, null);

    assertThat(user.hasPassword()).isTrue();
  }

  @Test
  void hasPasswordReturnsFalseWhenPasswordNull() {
    User user = User.rehydrate(new UserID(1), "johndoe", null, "John Doe", null, false, null);

    assertThat(user.hasPassword()).isFalse();
  }

  @Test
  void initTwoFactorStoresSecretAndDisables2FA() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, false, null);

    user.initTwoFactor("TOTP_SECRET");

    assertThat(user.getTwoFactorSecret()).isEqualTo("TOTP_SECRET");
    assertThat(user.getTwoFactorEnabled()).isFalse();
  }

  @Test
  void enableTwoFactorRequiresSecretInitialized() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, false, null);

    assertThatThrownBy(user::enableTwoFactor)
      .isInstanceOf(TwoFactorNotInitializedException.class);
  }

  @Test
  void enableTwoFactorSetsEnabled() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, false, "TOTP_SECRET");

    user.enableTwoFactor();

    assertThat(user.getTwoFactorEnabled()).isTrue();
  }

  @Test
  void disableTwoFactorClearsSecretAndDisables() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, true, "TOTP_SECRET");

    user.disableTwoFactor();

    assertThat(user.getTwoFactorEnabled()).isFalse();
    assertThat(user.getTwoFactorSecret()).isNull();
  }

  @Test
  void requiresTwoFactorAuthReturnsTrueWhenEnabledAndSecretPresent() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, true, "TOTP_SECRET");

    assertThat(user.requiresTwoFactorAuth()).isTrue();
  }

  @Test
  void requiresTwoFactorAuthReturnsFalseWhenDisabled() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, false, "TOTP_SECRET");

    assertThat(user.requiresTwoFactorAuth()).isFalse();
  }

  @Test
  void requiresTwoFactorAuthReturnsFalseWhenSecretNull() {
    User user = User.rehydrate(new UserID(1), "johndoe", PWD, "John Doe", null, true, null);

    assertThat(user.requiresTwoFactorAuth()).isFalse();
  }
}
