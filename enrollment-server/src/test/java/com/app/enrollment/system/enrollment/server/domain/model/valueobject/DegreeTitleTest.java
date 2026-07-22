package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDegreeTitleException;
import org.junit.jupiter.api.Test;

class DegreeTitleTest {

  @Test
  void acceptsValidTitle() {
    assertThat(new DegreeTitle("Profesional Técnico").getValue()).isEqualTo("Profesional Técnico");
  }

  @Test
  void trimsBlanks() {
    assertThat(new DegreeTitle("  Licenciado  ").getValue()).isEqualTo("Licenciado");
  }

  @Test
  void acceptsExactly150Chars() {
    String title = "A".repeat(150);
    assertThat(new DegreeTitle(title).getValue()).isEqualTo(title);
  }

  @Test
  void rejectsNull() {
    assertThatThrownBy(() -> new DegreeTitle(null)).isInstanceOf(InvalidDegreeTitleException.class);
  }

  @Test
  void rejectsEmpty() {
    assertThatThrownBy(() -> new DegreeTitle("")).isInstanceOf(InvalidDegreeTitleException.class);
  }

  @Test
  void rejectsBlank() {
    assertThatThrownBy(() -> new DegreeTitle("   ")).isInstanceOf(InvalidDegreeTitleException.class);
  }

  @Test
  void rejectsMoreThan150Chars() {
    String tooLong = "A".repeat(151);
    assertThatThrownBy(() -> new DegreeTitle(tooLong))
      .isInstanceOf(InvalidDegreeTitleException.class);
  }

  @Test
  void equalityByValue() {
    assertThat(new DegreeTitle("Técnico"))
        .isEqualTo(new DegreeTitle("Técnico"))
        .hasSameHashCodeAs(new DegreeTitle("Técnico"))
        .isNotEqualTo(new DegreeTitle("Licenciado"));
  }

  @Test
  void ofFactoryEquivalentToConstructor() {
    assertThat(DegreeTitle.of("Técnico")).isEqualTo(new DegreeTitle("Técnico"));
  }
}
