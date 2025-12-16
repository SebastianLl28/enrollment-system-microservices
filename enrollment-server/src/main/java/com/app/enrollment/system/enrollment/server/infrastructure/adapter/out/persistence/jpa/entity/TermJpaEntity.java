package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Alonso
 */
@Entity
@Table(name = "term", indexes = {
  @Index(name = "idx_term_code", columnList = "code", unique = true),
  @Index(name = "idx_term_active", columnList = "active")
})
public class TermJpaEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(nullable = false, length = 20, unique = true)
  private String code; // 2025-2
  
  @Column(nullable = false)
  private LocalDate startDate;
  
  @Column(nullable = false)
  private LocalDate endDate;
  
  @Column(nullable = false)
  private boolean active;
  
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;
  
  public TermJpaEntity() {
  }
  
  public TermJpaEntity(Integer id, String code, LocalDate startDate, LocalDate endDate, boolean active,
    Instant createdAt) {
    this.id = id;
    this.code = code;
    this.startDate = startDate;
    this.endDate = endDate;
    this.active = active;
    this.createdAt = createdAt;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public LocalDate getStartDate() {
    return startDate;
  }
  
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }
  
  public LocalDate getEndDate() {
    return endDate;
  }
  
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
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
}
