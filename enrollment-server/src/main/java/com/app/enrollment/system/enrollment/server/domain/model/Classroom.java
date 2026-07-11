package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import java.time.Instant;

/**
 * Aula donde se dicta una sección. Puede ser física (con capacidad) o virtual
 * (sin límite de capacidad).
 *
 * @author Alonso
 */
public class Classroom {

  private final ClassroomID id;
  private final String code;
  private final String name;
  private final Integer capacity; // null cuando el aula es virtual
  private final Boolean virtual;
  private final Boolean active;
  private final Instant createdAt;

  private Classroom(ClassroomID id, String code, String name, Integer capacity, Boolean virtual,
    Boolean active, Instant createdAt) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.capacity = capacity;
    this.virtual = virtual;
    this.active = active;
    this.createdAt = createdAt;
  }

  public static Classroom create(String code, String name, Integer capacity, Boolean virtual,
    Instant now) {
    return new Classroom(null, code, name, capacity, virtual, true, now);
  }

  public static Classroom rehydrate(ClassroomID id, String code, String name, Integer capacity,
    Boolean virtual, Boolean active, Instant createdAt) {
    return new Classroom(id, code, name, capacity, virtual, active, createdAt);
  }

  public ClassroomID getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public Boolean isVirtual() {
    return virtual;
  }

  public Boolean isActive() {
    return active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
