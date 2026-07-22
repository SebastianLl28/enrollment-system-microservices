package com.app.authorization.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class UIViewTest {

  @Test
  void createBuildsActiveView() {
    UIView view = UIView.create("DASHBOARD", "/dashboard", "Panel", "main", 1);

    assertThat(view.getCode()).isEqualTo("DASHBOARD");
    assertThat(view.getRoute()).isEqualTo("/dashboard");
    assertThat(view.getLabel()).isEqualTo("Panel");
    assertThat(view.getModule()).isEqualTo("main");
    assertThat(view.getSortOrder()).isEqualTo(1);
    assertThat(view.isActive()).isTrue();
  }

  @Test
  void createTrimsFields() {
    UIView view = UIView.create("  STUDENTS  ", "  /students  ", "  Alumnos  ", "  admin  ", 2);

    assertThat(view.getCode()).isEqualTo("STUDENTS");
    assertThat(view.getRoute()).isEqualTo("/students");
    assertThat(view.getLabel()).isEqualTo("Alumnos");
    assertThat(view.getModule()).isEqualTo("admin");
  }

  @Test
  void createAllowsNullModule() {
    UIView view = UIView.create("DASHBOARD", "/dashboard", "Panel", null, null);

    assertThat(view.getModule()).isNull();
    assertThat(view.getSortOrder()).isNull();
  }

  @Test
  void createRequiresCode() {
    assertThatThrownBy(() -> UIView.create("", "/dashboard", "Panel", null, null))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createRequiresNonBlankCode() {
    assertThatThrownBy(() -> UIView.create("   ", "/dashboard", "Panel", null, null))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createRequiresNullCode() {
    assertThatThrownBy(() -> UIView.create(null, "/dashboard", "Panel", null, null))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createRequiresRoute() {
    assertThatThrownBy(() -> UIView.create("DASHBOARD", "", "Panel", null, null))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createRequiresLabel() {
    assertThatThrownBy(() -> UIView.create("DASHBOARD", "/dashboard", "  ", null, null))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void deactivateSetsInactive() {
    UIView view = UIView.create("DASHBOARD", "/dashboard", "Panel", null, null);

    view.deactivate();

    assertThat(view.isActive()).isFalse();
  }

  @Test
  void activateRestoresActive() {
    UIView view = UIView.rehydrate("DASHBOARD", "/dashboard", "Panel", null, null, false);

    view.activate();

    assertThat(view.isActive()).isTrue();
  }
}
