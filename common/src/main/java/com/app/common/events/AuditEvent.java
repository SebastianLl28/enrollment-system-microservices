package com.app.common.events;

import java.time.Instant;

/**
 * @author Alonso
 */
public class AuditEvent {
  private Instant timestamp;
  private String serviceId;
  private String path;
  private String method;
  private int status;
  private String userId;
  private String query;
  private String userAgent;
  private String body;
  
  public AuditEvent() {}
  
  public AuditEvent(Instant timestamp, String serviceId, String path, String method, int status, String userId,
    String query, String userAgent, String body) {
    this.timestamp = timestamp;
    this.serviceId = serviceId;
    this.path = path;
    this.method = method;
    this.status = status;
    this.userId = userId;
    this.query = query;
    this.userAgent = userAgent;
    this.body = body;
  }
  
  public Instant getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
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
  
  public int getStatus() {
    return status;
  }
  
  public void setStatus(int status) {
    this.status = status;
  }
  
  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public String getQuery() {
    return query;
  }
  
  public void setQuery(String query) {
    this.query = query;
  }
  
  public String getUserAgent() {
    return userAgent;
  }
  
  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }
  
  public String getServiceId() {
    return serviceId;
  }
  
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }
  
  public String getBody() {
    return body;
  }
  
  public void setBody(String body) {
    this.body = body;
  }
}
