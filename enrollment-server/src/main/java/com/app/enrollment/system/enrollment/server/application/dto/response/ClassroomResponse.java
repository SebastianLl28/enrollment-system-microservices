package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.time.Instant;

/**
 * @author Alonso
 */
public class ClassroomResponse {

  private Integer id;
  private String code;
  private String name;
  private Integer capacity;
  private Boolean virtual;
  private Boolean active;
  private Instant createdAt;

  public ClassroomResponse() {
  }

  public ClassroomResponse(Integer id, String code, String name, Integer capacity,
    Boolean virtual, Boolean active, Instant createdAt) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.capacity = capacity;
    this.virtual = virtual;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Boolean getVirtual() {
    return virtual;
  }

  public void setVirtual(Boolean virtual) {
    this.virtual = virtual;
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
}
