package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Carrera ofertada en un periodo académico: es a lo que se matricula y lo que
 * paga el estudiante.
 *
 * @author Alonso
 */
public class CareerOffering {

  private final CareerOfferingID id;
  private final CareerID careerId;
  private final TermID termId;
  private final Integer capacity;
  private Integer enrolledCount;
  private final Boolean active;
  private final Instant createdAt;
  private final BigDecimal price; // precio de la matrícula para esta carrera-periodo

  private CareerOffering(CareerOfferingID id, CareerID careerId, TermID termId,
    Integer capacity, Integer enrolledCount, Boolean active, Instant createdAt, BigDecimal price) {
    this.id = id;
    this.careerId = careerId;
    this.termId = termId;
    this.capacity = capacity;
    this.enrolledCount = enrolledCount;
    this.active = active;
    this.createdAt = createdAt;
    this.price = price;
  }

  public static CareerOffering create(CareerID careerId, TermID termId, Integer capacity,
    BigDecimal price, Instant now) {
    return new CareerOffering(null, careerId, termId, capacity, 0, true, now, price);
  }

  public static CareerOffering rehydrate(CareerOfferingID id, CareerID careerId, TermID termId,
    Integer capacity, Integer enrolledCount, Boolean active, Instant createdAt, BigDecimal price) {
    return new CareerOffering(id, careerId, termId, capacity, enrolledCount, active,
      createdAt, price);
  }

  public void incrementEnrolledCount() {
    if (enrolledCount == null) {
      enrolledCount = 0;
    }
    if (enrolledCount < capacity) {
      enrolledCount++;
    } else {
      throw new IllegalStateException("Cannot enroll more students than the capacity.");
    }
  }


  public void decrementEnrolledCount() {
    if (enrolledCount == null) {
      enrolledCount = 0;
    }
    if (enrolledCount > 0) {
      enrolledCount--;
    } else {
      throw new IllegalStateException("Enrolled count cannot be negative.");
    }
  }

  public CareerOfferingID getId() {
    return id;
  }

  public CareerID getCareerId() {
    return careerId;
  }

  public TermID getTermId() {
    return termId;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public Integer getEnrolledCount() {
    return enrolledCount;
  }

  public Boolean isActive() {
    return active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Boolean getActive() {
    return active;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
