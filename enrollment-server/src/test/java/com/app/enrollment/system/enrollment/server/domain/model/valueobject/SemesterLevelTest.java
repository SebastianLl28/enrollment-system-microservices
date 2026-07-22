package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLevelException;
import org.junit.jupiter.api.Test;

class SemesterLevelTest {

  @Test
  void acceptsMinBoundary() {
    assertThat(new SemesterLevel(1).getValue()).isEqualTo(1);
  }

  @Test
  void acceptsMaxBoundary() {
    assertThat(new SemesterLevel(20).getValue()).isEqualTo(20);
  }

  @Test
  void rejectsZero() {
    assertThatThrownBy(() -> new SemesterLevel(0)).isInstanceOf(InvalidSemesterLevelException.class);
  }

  @Test
  void rejectsBelowMin() {
    assertThatThrownBy(() -> new SemesterLevel(-1))
      .isInstanceOf(InvalidSemesterLevelException.class);
  }

  @Test
  void rejectsAboveMax() {
    assertThatThrownBy(() -> new SemesterLevel(21))
      .isInstanceOf(InvalidSemesterLevelException.class);
  }

  @Test
  void equalityByValue() {
    assertThat(new SemesterLevel(3))
        .isEqualTo(new SemesterLevel(3))
        .hasSameHashCodeAs(new SemesterLevel(3))
        .isNotEqualTo(new SemesterLevel(4));
  }

  @Test
  void toStringReturnsValue() {
    assertThat(new SemesterLevel(5)).hasToString("5");
  }

  @Test
  void ofFactoryEquivalentToConstructor() {
    assertThat(SemesterLevel.of(2)).isEqualTo(new SemesterLevel(2));
  }
}
