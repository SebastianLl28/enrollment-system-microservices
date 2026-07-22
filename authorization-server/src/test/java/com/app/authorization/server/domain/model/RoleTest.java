package com.app.authorization.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.authorization.server.domain.exception.InvalidRoleNameException;
import com.app.authorization.server.domain.exception.InvalidViewCodeException;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import com.app.authorization.server.domain.model.valueobject.RoleID;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoleTest {

  @Test
  void createBuildsRoleWithTrimmedName() {
    Role role = Role.create("  ADMIN  ", "Administrador");

    assertThat(role.getName()).isEqualTo("ADMIN");
    assertThat(role.getDescription()).isEqualTo("Administrador");
    assertThat(role.getId()).isNull();
    assertThat(role.getPermissionIds()).isEmpty();
    assertThat(role.getViewCodes()).isEmpty();
  }

  @Test
  void createRequiresName() {
    assertThatThrownBy(() -> Role.create("", "desc"))
      .isInstanceOf(InvalidRoleNameException.class);
  }

  @Test
  void createRequiresNonBlankName() {
    assertThatThrownBy(() -> Role.create("   ", "desc"))
      .isInstanceOf(InvalidRoleNameException.class);
  }

  @Test
  void createRequiresNonNullName() {
    assertThatThrownBy(() -> Role.create(null, "desc"))
      .isInstanceOf(InvalidRoleNameException.class);
  }

  @Test
  void assignPermissionAddsToSet() {
    Role role = Role.create("ADMIN", null);
    PermissionID pid = new PermissionID(1);

    role.assignPermission(pid);

    assertThat(role.hasPermission(pid)).isTrue();
  }

  @Test
  void assignPermissionNullThrows() {
    Role role = Role.create("ADMIN", null);

    assertThatThrownBy(() -> role.assignPermission(null))
      .isInstanceOf(NullPointerException.class);
  }

  @Test
  void removePermissionRemovesFromSet() {
    PermissionID pid = new PermissionID(1);
    Role role = Role.rehydrate(new RoleID(1), "ADMIN", null, Set.of(pid), Set.of());

    role.removePermission(pid);

    assertThat(role.hasPermission(pid)).isFalse();
  }

  @Test
  void removePermissionNullIsNoOp() {
    Role role = Role.create("ADMIN", null);

    role.removePermission(null);

    assertThat(role.getPermissionIds()).isEmpty();
  }

  @Test
  void assignViewAddsViewCode() {
    Role role = Role.create("ADMIN", null);

    role.assignView("DASHBOARD");

    assertThat(role.hasView("DASHBOARD")).isTrue();
  }

  @Test
  void assignViewTrimsCode() {
    Role role = Role.create("ADMIN", null);

    role.assignView("  DASHBOARD  ");

    assertThat(role.hasView("DASHBOARD")).isTrue();
  }

  @Test
  void assignViewNullThrows() {
    Role role = Role.create("ADMIN", null);

    assertThatThrownBy(() -> role.assignView(null))
      .isInstanceOf(InvalidViewCodeException.class);
  }

  @Test
  void assignViewBlankThrows() {
    Role role = Role.create("ADMIN", null);

    assertThatThrownBy(() -> role.assignView("   "))
      .isInstanceOf(InvalidViewCodeException.class);
  }

  @Test
  void removeViewRemovesCode() {
    Role role = Role.rehydrate(new RoleID(1), "ADMIN", null, Set.of(), Set.of("DASHBOARD"));

    role.removeView("DASHBOARD");

    assertThat(role.hasView("DASHBOARD")).isFalse();
  }

  @Test
  void removeViewNullIsNoOp() {
    Role role = Role.create("ADMIN", null);

    role.removeView(null);

    assertThat(role.getViewCodes()).isEmpty();
  }

  @Test
  void hasPermissionReturnsFalseForAbsentPermission() {
    Role role = Role.create("ADMIN", null);

    assertThat(role.hasPermission(new PermissionID(99))).isFalse();
  }

  @Test
  void hasPermissionReturnsFalseForNull() {
    Role role = Role.create("ADMIN", null);

    assertThat(role.hasPermission(null)).isFalse();
  }

  @Test
  void hasViewReturnsFalseForAbsentCode() {
    Role role = Role.create("ADMIN", null);

    assertThat(role.hasView("MISSING")).isFalse();
  }

  @Test
  void rehydrateDefendsAgainstNullCollections() {
    Role role = Role.rehydrate(new RoleID(1), "ADMIN", null, null, null);

    assertThat(role.getPermissionIds()).isEmpty();
    assertThat(role.getViewCodes()).isEmpty();
  }
}
