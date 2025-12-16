package com.app.authorization.server.application.dto.response;

/**
 * @author Alonso
 */

public class UIViewResponse {
  
  private String code;
  private String route;
  private String label;
  private String module;
  private Integer sortOrder;
  private boolean active;
  
  public UIViewResponse(String code, String route, String label,
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
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public void setRoute(String route) {
    this.route = route;
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public void setModule(String module) {
    this.module = module;
  }
  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
}
