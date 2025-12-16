package com.app.authorization.server.domain.model;

/**
 * @author Alonso
 */
public final class UIView {
  
  private String code;
  
  private String route;
  
  private String label;
  
  private String module;
  
  private Integer sortOrder;
  
  private boolean active;
  
  private UIView() {
  }
  
  private static void validateRequiredFields(String code, String route, String label) {
    if (code == null || code.trim().isEmpty()) {
      throw new IllegalArgumentException("UIView code cannot be null or empty");
    }
    if (route == null || route.trim().isEmpty()) {
      throw new IllegalArgumentException("UIView route cannot be null or empty");
    }
    if (label == null || label.trim().isEmpty()) {
      throw new IllegalArgumentException("UIView label cannot be null or empty");
    }
  }
  
  public static UIView create(String code, String route, String label,
    String module, Integer sortOrder) {
    validateRequiredFields(code, route, label);
    UIView view = new UIView();
    view.code = code.trim();
    view.route = route.trim();
    view.label = label.trim();
    view.module = (module != null ? module.trim() : null);
    view.sortOrder = sortOrder;
    view.active = true;
    return view;
  }
  
  public static UIView rehydrate(String code, String route, String label,
    String module, Integer sortOrder,
    boolean active) {
    UIView view = new UIView();
    view.code = code;
    view.route = route;
    view.label = label;
    view.module = module;
    view.sortOrder = sortOrder;
    view.active = active;
    return view;
  }
  
  // ---- Comportamiento ----
  
  public void deactivate() {
    this.active = false;
  }
  
  public void activate() {
    this.active = true;
  }
  
  // ---- Getters ----
  
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
