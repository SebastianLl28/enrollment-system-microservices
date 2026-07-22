package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDocumentNumberException;
import org.junit.jupiter.api.Test;

class DocumentNumberTest {

  @Test
  void acceptsEightCharAlphanumeric() {
    assertThat(new DocumentNumber("12345678").getValue()).isEqualTo("12345678");
  }

  @Test
  void acceptsTwentyCharAlphanumeric() {
    assertThat(new DocumentNumber("12345678901234567890").getValue())
      .isEqualTo("12345678901234567890");
  }

  @Test
  void acceptsMixedAlphanumeric() {
    assertThat(new DocumentNumber("ABC12345").getValue()).isEqualTo("ABC12345");
  }

  @Test
  void normalizesLowercaseToUppercase() {
    assertThat(new DocumentNumber("abc12345").getValue()).isEqualTo("ABC12345");
  }

  @Test
  void removesInternalSpaces() {
    assertThat(new DocumentNumber("1234 5678").getValue()).isEqualTo("12345678");
  }

  @Test
  void rejectsNull() {
    assertThatThrownBy(() -> new DocumentNumber(null))
      .isInstanceOf(InvalidDocumentNumberException.class);
  }

  @Test
  void rejectsTooShort() {
    assertThatThrownBy(() -> new DocumentNumber("1234567"))
      .isInstanceOf(InvalidDocumentNumberException.class);
  }

  @Test
  void rejectsTooLong() {
    assertThatThrownBy(() -> new DocumentNumber("123456789012345678901"))
      .isInstanceOf(InvalidDocumentNumberException.class);
  }

  @Test
  void rejectsSpecialChars() {
    assertThatThrownBy(() -> new DocumentNumber("1234-5678"))
      .isInstanceOf(InvalidDocumentNumberException.class);
  }

  @Test
  void equalityByValue() {
    assertThat(new DocumentNumber("12345678"))
        .isEqualTo(new DocumentNumber("12345678"))
        .hasSameHashCodeAs(new DocumentNumber("12345678"))
        .isNotEqualTo(new DocumentNumber("87654321"));
  }

  @Test
  void ofFactoryEquivalentToConstructor() {
    assertThat(DocumentNumber.of("12345678")).isEqualTo(new DocumentNumber("12345678"));
  }
}
