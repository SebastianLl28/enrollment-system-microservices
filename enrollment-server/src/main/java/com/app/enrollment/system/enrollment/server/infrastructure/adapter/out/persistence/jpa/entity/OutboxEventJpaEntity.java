package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import com.app.enrollment.system.enrollment.server.domain.event.EventType;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.enums.OutboxStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * @author Alonso
 */
@Entity
@Table(name = "outbox_event")
public class OutboxEventJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(name = "user_id", nullable = false)
  private Integer userId;
  
  @Column(nullable = false)
  private String aggregateType;
  
  @Column(nullable = false)
  private String aggregateId;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EventType eventType;
  
  @Column(nullable = false, columnDefinition = "TEXT")
  private String payload;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OutboxStatusType status = OutboxStatusType.PENDING;
  
  @Column(nullable = false)
  private Instant createdAt = Instant.now();
  
  private Instant processedAt;
  
  private Integer retryCount = 0;
  
  private Integer studentId;
  
  private Integer courseId;
  
  @Enumerated(EnumType.STRING)
  private EnrollmentStatus enrollmentStatus;
  
  public OutboxEventJpaEntity() {
  }
  
  public EnrollmentStatus getEnrollmentStatus() {
    return enrollmentStatus;
  }
  
  public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
    this.enrollmentStatus = enrollmentStatus;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getAggregateType() {
    return aggregateType;
  }
  
  public void setAggregateType(String aggregateType) {
    this.aggregateType = aggregateType;
  }
  
  public String getAggregateId() {
    return aggregateId;
  }
  
  public void setAggregateId(String aggregateId) {
    this.aggregateId = aggregateId;
  }
  
  public EventType getEventType() {
    return eventType;
  }
  
  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }
  
  public String getPayload() {
    return payload;
  }
  
  public void setPayload(String payload) {
    this.payload = payload;
  }
  
  public OutboxStatusType getStatus() {
    return status;
  }
  
  public Instant getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
  
  public Instant getProcessedAt() {
    return processedAt;
  }
  
  public void setProcessedAt(Instant processedAt) {
    this.processedAt = processedAt;
  }
  
  public Integer getUserId() {
    return userId;
  }
  
  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  
  public void setStatus(
    OutboxStatusType status) {
    this.status = status;
  }
  
  public Integer getRetryCount() {
    return retryCount;
  }
  
  public void setRetryCount(Integer retryCount) {
    this.retryCount = retryCount;
  }
  
  public Integer getStudentId() {
    return studentId;
  }
  
  public void setStudentId(Integer studentId) {
    this.studentId = studentId;
  }
  
  public Integer getCourseId() {
    return courseId;
  }
  
  public void setCourseId(Integer courseId) {
    this.courseId = courseId;
  }
}
