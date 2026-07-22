package com.app.authorization.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.authorization.server.domain.exception.InvalidOperationException;
import com.app.authorization.server.domain.exception.InvalidResourceException;
import com.app.authorization.server.domain.model.enums.OperationType;
import com.app.authorization.server.domain.model.enums.ResourceType;
import com.app.authorization.server.domain.model.enums.ScopeType;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import org.junit.jupiter.api.Test;

class PermissionTest {

  @Test
  void createBuildsPermission() {
    Permission p = Permission.create(ResourceType.STUDENT, OperationType.READ, ScopeType.ALL, "desc");

    assertThat(p.getResource()).isEqualTo(ResourceType.STUDENT);
    assertThat(p.getOperation()).isEqualTo(OperationType.READ);
    assertThat(p.getScope()).isEqualTo(ScopeType.ALL);
    assertThat(p.getDescription()).isEqualTo("desc");
    assertThat(p.getId()).isNull();
  }

  @Test
  void createAllowsNullScope() {
    Permission p = Permission.create(ResourceType.ENROLLMENT, OperationType.CREATE, null, null);

    assertThat(p.getScope()).isNull();
  }

  @Test
  void createRequiresResource() {
    assertThatThrownBy(() -> Permission.create(null, OperationType.READ, ScopeType.ALL, null))
      .isInstanceOf(InvalidResourceException.class);
  }

  @Test
  void createRequiresOperation() {
    assertThatThrownBy(() -> Permission.create(ResourceType.STUDENT, null, ScopeType.ALL, null))
      .isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void matchesReturnsTrueForExactMatch() {
    Permission p = Permission.create(ResourceType.STUDENT, OperationType.READ, ScopeType.ALL, null);

    assertThat(p.matches(ResourceType.STUDENT, OperationType.READ, ScopeType.ALL)).isTrue();
  }

  @Test
  void matchesReturnsFalseForDifferentResource() {
    Permission p = Permission.create(ResourceType.STUDENT, OperationType.READ, ScopeType.ALL, null);

    assertThat(p.matches(ResourceType.ENROLLMENT, OperationType.READ, ScopeType.ALL)).isFalse();
  }

  @Test
  void matchesReturnsFalseForDifferentOperation() {
    Permission p = Permission.create(ResourceType.STUDENT, OperationType.READ, ScopeType.ALL, null);

    assertThat(p.matches(ResourceType.STUDENT, OperationType.CREATE, ScopeType.ALL)).isFalse();
  }

  @Test
  void matchesReturnsFalseForDifferentScope() {
    Permission p = Permission.create(ResourceType.STUDENT, OperationType.READ, ScopeType.ALL, null);

    assertThat(p.matches(ResourceType.STUDENT, OperationType.READ, ScopeType.SELF)).isFalse();
  }

  @Test
  void matchesNullScopeRequiresBothNull() {
    Permission p = Permission.create(ResourceType.STUDENT, OperationType.READ, null, null);

    assertThat(p.matches(ResourceType.STUDENT, OperationType.READ, null)).isTrue();
    assertThat(p.matches(ResourceType.STUDENT, OperationType.READ, ScopeType.ALL)).isFalse();
  }

  @Test
  void rehydrateSetsId() {
    PermissionID pid = new PermissionID(5);
    Permission p = Permission.rehydrate(pid, ResourceType.UI_VIEW, OperationType.UPDATE,
      ScopeType.SELF, "desc");

    assertThat(p.getId()).isEqualTo(pid);
    assertThat(p.getResource()).isEqualTo(ResourceType.UI_VIEW);
  }
}
