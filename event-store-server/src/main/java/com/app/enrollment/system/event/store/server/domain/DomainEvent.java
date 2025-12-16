package com.app.enrollment.system.event.store.server.domain;

import java.time.Instant;

public class DomainEvent {
  
  private final String serviceName;
  private final String path;
  private final String method;
  private final String uri;
  private final String userId;
  private final Integer status;
  private final Instant occurredAt;
  private final Instant receivedAt;
  private final String payloadJson;
  
  public DomainEvent(String serviceName,
    String path,
    String method,
    String uri,
    String userId,
    Integer status,
    Instant occurredAt,
    Instant receivedAt,
    String payloadJson) {
    
    this.serviceName = serviceName;
    this.path = path;
    this.method = method;
    this.uri = uri;
    this.userId = userId;
    this.status = status;
    this.occurredAt = occurredAt;
    this.receivedAt = receivedAt;
    this.payloadJson = payloadJson;
  }
  
  public String getServiceName() { return serviceName; }
  public String getPath() { return path; }
  public String getMethod() { return method; }
  public String getUri() { return uri; }
  public String getUserId() { return userId; }
  public Integer getStatus() { return status; }
  public Instant getOccurredAt() { return occurredAt; }
  public Instant getReceivedAt() { return receivedAt; }
  public String getPayloadJson() { return payloadJson; }
}
