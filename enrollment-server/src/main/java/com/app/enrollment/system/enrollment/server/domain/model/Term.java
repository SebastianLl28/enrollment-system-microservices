package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.time.Instant;
import java.time.LocalDate;

/**
 * @author Alonso
 */
public class Term {
  private final TermID id;
  private final String code; // "2025-2"
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final boolean active;
  private final Instant createAt;
  
  private Term(TermID id, String code, LocalDate startDate, LocalDate endDate, boolean active, Instant createAt) {
    this.id = id;
    this.code = code;
    this.startDate = startDate;
    this.endDate = endDate;
    this.active = active;
    this.createAt = createAt;
  }
  
  public static Term create(
    String code,
    LocalDate startDate,
    LocalDate endDate,
    Instant now
  ) {
    return new Term(
      null,
      code,
      startDate,
      endDate,
      true,
      now
    );
  }
  
  public static Term rehydrate(
    TermID id,
    String code,
    LocalDate startDate,
    LocalDate endDate,
    boolean active,
    Instant createAt
  ) {
    return new Term(
      id,
      code,
      startDate,
      endDate,
      active,
      createAt
    );
  }
  
  
  public TermID getId() {
    return id;
  }
  
  public String getCode() {
    return code;
  }
  
  public LocalDate getStartDate() {
    return startDate;
  }
  
  public LocalDate getEndDate() {
    return endDate;
  }
  
  public boolean isActive() {
    return active;
  }
  
  public Instant getCreateAt() {
    return createAt;
  }
}
