package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Alonso
 */
public class CareerOfferingResponse {

  private Integer id;
  private CareerSummaryResponse career;
  private TermResponse term;
  private Integer capacity;
  private Integer enrolledCount;
  private Boolean active;
  private Instant createdAt;
  private BigDecimal price;

  public CareerOfferingResponse() {
  }

  public CareerOfferingResponse(Integer id, CareerSummaryResponse career, TermResponse term,
    Integer capacity, Integer enrolledCount, Boolean active, Instant createdAt, BigDecimal price) {
    this.id = id;
    this.career = career;
    this.term = term;
    this.capacity = capacity;
    this.enrolledCount = enrolledCount;
    this.active = active;
    this.createdAt = createdAt;
    this.price = price;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public CareerSummaryResponse getCareer() {
    return career;
  }

  public void setCareer(CareerSummaryResponse career) {
    this.career = career;
  }

  public TermResponse getTerm() {
    return term;
  }

  public void setTerm(TermResponse term) {
    this.term = term;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Integer getEnrolledCount() {
    return enrolledCount;
  }

  public void setEnrolledCount(Integer enrolledCount) {
    this.enrolledCount = enrolledCount;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
