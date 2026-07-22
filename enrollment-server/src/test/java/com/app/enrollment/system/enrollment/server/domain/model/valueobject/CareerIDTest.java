package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CareerIDTest {

  @Test
  void acceptsPositiveValue() {
    assertThat(new CareerID(5).getValue()).isEqualTo(5);
  }

  @Test
  void rejectsNull() {
    assertThatThrownBy(() -> new CareerID(null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejectsZeroAndNegative() {
    assertThatThrownBy(() -> new CareerID(0)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new CareerID(-1)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void equalityByValue() {
    assertThat(new CareerID(5))
        .isEqualTo(new CareerID(5))
        .hasSameHashCodeAs(new CareerID(5))
        .isNotEqualTo(new CareerID(6));
  }
}
