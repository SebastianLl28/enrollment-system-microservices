package com.app.authorization.server.application.dto.response;

import java.util.List;

/**
 * @author Alonso
 */
public class RoleResponse {
  private Integer id;
  private String name;
  private String description;
  private List<Integer> permissionIds;
  private List<String> viewCodes;
  
  public RoleResponse(Integer id, String name, String description,
    List<Integer> permissionIds,
    List<String> viewCodes) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.permissionIds = permissionIds;
    this.viewCodes = viewCodes;
  }
  
  public Integer getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public List<Integer> getPermissionIds() {
    return permissionIds;
  }
  
  public List<String> getViewCodes() {
    return viewCodes;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public void setPermissionIds(List<Integer> permissionIds) {
    this.permissionIds = permissionIds;
  }
  
  public void setViewCodes(List<String> viewCodes) {
    this.viewCodes = viewCodes;
  }
}
