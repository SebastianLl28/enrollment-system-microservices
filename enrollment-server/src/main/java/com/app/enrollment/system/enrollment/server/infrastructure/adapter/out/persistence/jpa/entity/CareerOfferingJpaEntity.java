package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Carrera ofertada en un periodo: la unidad a la que se matricula y paga el estudiante.
 *
 * @author Alonso
 */
@Entity
@Table(name = "career_offering", indexes = {
  @Index(name = "idx_career_offering_career", columnList = "career_id"),
  @Index(name = "idx_career_offering_term", columnList = "term_id"),
  @Index(name = "idx_career_offering_active", columnList = "active")
},
  uniqueConstraints = {
    // Una oferta por (career, term)
    @UniqueConstraint(name = "uq_career_offering_career_term",
      columnNames = {"career_id", "term_id"})
  })
public class CareerOfferingJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "career_id", nullable = false)
  private Integer careerId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "career_id", insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "fk_career_offering_career"))
  private CareerJpaEntity career;

  @Column(name = "term_id", nullable = false)
  private Integer termId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "term_id", insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "fk_career_offering_term"))
  private TermJpaEntity term;

  @Column(nullable = false)
  private int capacity;

  @Column(name = "enrolled_count", nullable = false)
  private int enrolledCount;

  @Column(nullable = false)
  private boolean active;

  @Column(precision = 10, scale = 2)
  private BigDecimal price;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public CareerOfferingJpaEntity() {
  }

  public CareerOfferingJpaEntity(Integer id, Integer careerId, Integer termId, Integer capacity,
    Integer enrolledCount, Boolean active, Instant createdAt, BigDecimal price) {
    this.id = id;
    this.careerId = careerId;
    this.termId = termId;
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

  public Integer getCareerId() {
    return careerId;
  }

  public void setCareerId(Integer careerId) {
    this.careerId = careerId;
  }

  public CareerJpaEntity getCareer() {
    return career;
  }

  public void setCareer(CareerJpaEntity career) {
    this.career = career;
  }

  public Integer getTermId() {
    return termId;
  }

  public void setTermId(Integer termId) {
    this.termId = termId;
  }

  public TermJpaEntity getTerm() {
    return term;
  }

  public void setTerm(TermJpaEntity term) {
    this.term = term;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public int getEnrolledCount() {
    return enrolledCount;
  }

  public void setEnrolledCount(int enrolledCount) {
    this.enrolledCount = enrolledCount;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
