package com.app.enrollment.system.event.store.server.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_event")
public class DomainEventEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(nullable = false, length = 100)
  private String serviceName;
  
  @Column(nullable = false, length = 255)
  private String path;
  
  @Column(nullable = false, length = 10)
  private String method;
  
  @Column(nullable = false, length = 500)
  private String uri;
  
  @Column(length = 100)
  private String userId;
  
  @Column
  private Integer status;
  
  @Column(nullable = false)
  private Instant occurredAt;
  
  @Column(nullable = false)
  private Instant receivedAt;
  
  @Column(name = "payload", columnDefinition = "TEXT")
  private String payload;
  
  public DomainEventEntity() {
  }
  
  public DomainEventEntity(String serviceName, String path, String method, String uri,
    String userId, Integer status, Instant occurredAt, Instant receivedAt, String payload) {
    this.serviceName = serviceName;
    this.path = path;
    this.method = method;
    this.uri = uri;
    this.userId = userId;
    this.status = status;
    this.occurredAt = occurredAt;
    this.receivedAt = receivedAt;
    this.payload = payload;
  }
  
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getServiceName() {
    return serviceName;
  }
  
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
  
  public String getPath() {
    return path;
  }
  
  public void setPath(String path) {
    this.path = path;
  }
  
  public String getMethod() {
    return method;
  }
  
  public void setMethod(String method) {
    this.method = method;
  }
  
  public String getUri() {
    return uri;
  }
  
  public void setUri(String uri) {
    this.uri = uri;
  }
  
  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public Integer getStatus() {
    return status;
  }
  
  public void setStatus(Integer status) {
    this.status = status;
  }
  
  public Instant getOccurredAt() {
    return occurredAt;
  }
  
  public void setOccurredAt(Instant occurredAt) {
    this.occurredAt = occurredAt;
  }
  
  public Instant getReceivedAt() {
    return receivedAt;
  }
  
  public void setReceivedAt(Instant receivedAt) {
    this.receivedAt = receivedAt;
  }
  
  public String getPayload() {
    return payload;
  }
  
  public void setPayload(String payload) {
    this.payload = payload;
  }
}
