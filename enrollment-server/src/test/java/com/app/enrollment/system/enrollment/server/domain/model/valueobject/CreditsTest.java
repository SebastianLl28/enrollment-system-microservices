package com.app.enrollment.system.enrollment.server.domain.model.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCreditsException;
import org.junit.jupiter.api.Test;

class CreditsTest {

  @Test
  void acceptsMinBoundary() {
    assertThat(new Credits(1).getValue()).isEqualTo(1);
  }

  @Test
  void acceptsMaxBoundary() {
    assertThat(new Credits(20).getValue()).isEqualTo(20);
  }

  @Test
  void acceptsMiddleValue() {
    assertThat(new Credits(4).getValue()).isEqualTo(4);
  }

  @Test
  void rejectsZero() {
    assertThatThrownBy(() -> new Credits(0)).isInstanceOf(InvalidCreditsException.class);
  }

  @Test
  void rejectsBelowMin() {
    assertThatThrownBy(() -> new Credits(-1)).isInstanceOf(InvalidCreditsException.class);
  }

  @Test
  void rejectsAboveMax() {
    assertThatThrownBy(() -> new Credits(21)).isInstanceOf(InvalidCreditsException.class);
  }

  @Test
  void equalityByValue() {
    assertThat(new Credits(4)).isEqualTo(new Credits(4));
    assertThat(new Credits(4)).hasSameHashCodeAs(new Credits(4));
    assertThat(new Credits(4)).isNotEqualTo(new Credits(5));
  }

  @Test
  void toStringReturnsValue() {
    assertThat(new Credits(4).toString()).isEqualTo("4");
  }

  @Test
  void ofFactoryEquivalentToConstructor() {
    assertThat(Credits.of(3)).isEqualTo(new Credits(3));
  }
}
