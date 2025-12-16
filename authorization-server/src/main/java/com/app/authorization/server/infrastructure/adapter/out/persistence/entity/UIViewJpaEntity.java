package com.app.authorization.server.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Alonso
 */
@Entity
@Table(name = "ui_view")
public class UIViewJpaEntity {
  
  @Id
  @Column(name = "code", length = 50)
  private String code;
  
  @Column(nullable = false, length = 200)
  private String route;
  
  @Column(nullable = false, length = 100)
  private String label;
  
  @Column(length = 100)
  private String module;
  
  @Column(name = "sort_order")
  private Integer sortOrder;
  
  @Column(nullable = false)
  private boolean active = true;
  
  protected UIViewJpaEntity() {
  }
  
  public UIViewJpaEntity(String code, String route, String label,
    String module, Integer sortOrder, boolean active) {
    this.code = code;
    this.route = route;
    this.label = label;
    this.module = module;
    this.sortOrder = sortOrder;
    this.active = active;
  }
  
  public String getCode() {
    return code;
  }
  
  public String getRoute() {
    return route;
  }
  
  public String getLabel() {
    return label;
  }
  
  public String getModule() {
    return module;
  }
  
  public Integer getSortOrder() {
    return sortOrder;
  }
  
  public boolean isActive() {
    return active;
  }
}
