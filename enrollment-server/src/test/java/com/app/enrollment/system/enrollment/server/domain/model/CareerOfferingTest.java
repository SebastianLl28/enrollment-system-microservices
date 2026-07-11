package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CareerOfferingTest {

  @Test
  void createStartsActiveWithZeroEnrolled() {
    CareerOffering offering = CareerOffering.create(new CareerID(1), new TermID(2), 30,
      new BigDecimal("350.00"), Mothers.NOW);

    assertThat(offering.getEnrolledCount()).isZero();
    assertThat(offering.isActive()).isTrue();
    assertThat(offering.getPrice()).isEqualByComparingTo("350.00");
  }

  @Test
  void incrementEnrolledCountIncrementsWhileUnderCapacity() {
    CareerOffering offering = Mothers.careerOffering(1, 1, 1, 2, 0, true,
      new BigDecimal("350.00"));

    offering.incrementEnrolledCount();
    offering.incrementEnrolledCount();

    assertThat(offering.getEnrolledCount()).isEqualTo(2);
  }

  @Test
  void incrementEnrolledCountRejectsWhenCapacityReached() {
    CareerOffering offering = Mothers.careerOffering(1, 1, 1, 2, 2, true,
      new BigDecimal("350.00"));

    assertThatThrownBy(offering::incrementEnrolledCount)
      .isInstanceOf(IllegalStateException.class);
    assertThat(offering.getEnrolledCount()).isEqualTo(2);
  }

  @Test
  void decrementEnrolledCountRejectsWhenZero() {
    CareerOffering offering = Mothers.careerOffering(1, 1, 1, 30, 0, true,
      new BigDecimal("350.00"));

    assertThatThrownBy(offering::decrementEnrolledCount)
      .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void decrementEnrolledCountDecrements() {
    CareerOffering offering = Mothers.careerOffering(1, 1, 1, 30, 5, true,
      new BigDecimal("350.00"));

    offering.decrementEnrolledCount();

    assertThat(offering.getEnrolledCount()).isEqualTo(4);
  }

  @Test
  void enrolledCountNullIsTreatedAsZero() {
    CareerOffering offering = CareerOffering.rehydrate(new CareerOfferingID(1), new CareerID(1),
      new TermID(1), 10, null, true, Mothers.NOW, new BigDecimal("350.00"));

    offering.incrementEnrolledCount();

    assertThat(offering.getEnrolledCount()).isEqualTo(1);
  }
}
