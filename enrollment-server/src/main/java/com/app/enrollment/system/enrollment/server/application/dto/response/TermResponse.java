package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.time.Instant;
import java.time.LocalDate;

/**
 * @author Alonso
 */
public class TermResponse {
  
  private Integer id;
  
  private String code;
  
  private LocalDate startDate;
  
  private LocalDate endDate;
  
  private boolean active;
  
  private Instant createdAt;
  
  public TermResponse() {
  }
  
  public TermResponse(Integer id, String code, LocalDate startDate, LocalDate endDate,
    boolean active, Instant createAt) {
    this.id = id;
    this.code = code;
    this.startDate = startDate;
    this.endDate = endDate;
    this.active = active;
    this.createdAt = createAt;
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
