package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void acceptsValidEmail() {
    assertThat(new Email("user@example.com").getValue()).isEqualTo("user@example.com");
  }

  @Test
  void acceptsEmailWithSubdomain() {
    assertThat(new Email("user@mail.example.com").getValue()).isEqualTo("user@mail.example.com");
  }

  @Test
  void normalizesUppercaseToLowercase() {
    assertThat(new Email("USER@EXAMPLE.COM").getValue()).isEqualTo("user@example.com");
  }

  @Test
  void trimsBlanks() {
    assertThat(new Email("  user@example.com  ").getValue()).isEqualTo("user@example.com");
  }

  @Test
  void rejectsNull() {
    assertThatThrownBy(() -> new Email(null)).isInstanceOf(InvalidEmailException.class);
  }

  @Test
  void rejectsEmpty() {
    assertThatThrownBy(() -> new Email("")).isInstanceOf(InvalidEmailException.class);
  }

  @Test
  void rejectsMissingAt() {
    assertThatThrownBy(() -> new Email("userexample.com")).isInstanceOf(InvalidEmailException.class);
  }

  @Test
  void rejectsMissingDomain() {
    assertThatThrownBy(() -> new Email("user@")).isInstanceOf(InvalidEmailException.class);
  }

  @Test
  void rejectsMissingTld() {
    assertThatThrownBy(() -> new Email("user@example")).isInstanceOf(InvalidEmailException.class);
  }

  @Test
  void rejectsMissingLocalPart() {
    assertThatThrownBy(() -> new Email("@example.com")).isInstanceOf(InvalidEmailException.class);
  }

  @Test
  void equalityByValue() {
    assertThat(new Email("user@example.com")).isEqualTo(new Email("USER@EXAMPLE.COM"));
    assertThat(new Email("user@example.com")).hasSameHashCodeAs(new Email("user@example.com"));
    assertThat(new Email("a@example.com")).isNotEqualTo(new Email("b@example.com"));
  }

  @Test
  void ofFactoryEquivalentToConstructor() {
    assertThat(Email.of("user@example.com")).isEqualTo(new Email("user@example.com"));
  }
}
