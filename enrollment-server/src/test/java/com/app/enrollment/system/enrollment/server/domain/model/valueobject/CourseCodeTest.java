package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseCodeException;
import org.junit.jupiter.api.Test;

class CourseCodeTest {

  @Test
  void acceptsValidCodeWithoutHyphen() {
    assertThat(new CourseCode("CS101").getValue()).isEqualTo("CS101");
  }

  @Test
  void acceptsValidCodeWithHyphen() {
    assertThat(new CourseCode("MAT-205").getValue()).isEqualTo("MAT-205");
  }

  @Test
  void acceptsLongPrefixAndFourDigits() {
    assertThat(new CourseCode("ABCDEF1234").getValue()).isEqualTo("ABCDEF1234");
  }

  @Test
  void normalizesLowercaseToUppercase() {
    assertThat(new CourseCode("cs101").getValue()).isEqualTo("CS101");
  }

  @Test
  void trimsBlanks() {
    assertThat(new CourseCode("  INF300  ").getValue()).isEqualTo("INF300");
  }

  @Test
  void rejectsNull() {
    assertThatThrownBy(() -> new CourseCode(null))
      .isInstanceOf(InvalidCourseCodeException.class);
  }

  @Test
  void rejectsPrefixTooShort() {
    assertThatThrownBy(() -> new CourseCode("C101"))
      .isInstanceOf(InvalidCourseCodeException.class);
  }

  @Test
  void rejectsPrefixTooLong() {
    assertThatThrownBy(() -> new CourseCode("ABCDEFG101"))
      .isInstanceOf(InvalidCourseCodeException.class);
  }

  @Test
  void rejectsNoDigits() {
    assertThatThrownBy(() -> new CourseCode("ABCD"))
      .isInstanceOf(InvalidCourseCodeException.class);
  }

  @Test
  void rejectsOnlyDigits() {
    assertThatThrownBy(() -> new CourseCode("12345"))
      .isInstanceOf(InvalidCourseCodeException.class);
  }

  @Test
  void equalityByValue() {
    assertThat(new CourseCode("CS101")).isEqualTo(new CourseCode("cs101"));
    assertThat(new CourseCode("CS101")).hasSameHashCodeAs(new CourseCode("cs101"));
    assertThat(new CourseCode("CS101")).isNotEqualTo(new CourseCode("MAT205"));
  }

  @Test
  void toStringReturnsValue() {
    assertThat(new CourseCode("INF300").toString()).isEqualTo("INF300");
  }

  @Test
  void ofFactoryEquivalentToConstructor() {
    assertThat(CourseCode.of("CS101")).isEqualTo(new CourseCode("CS101"));
  }
}
