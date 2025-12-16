package com.app.authorization.server.domain.model;

import com.app.authorization.server.domain.exception.InvalidRoleNameException;
import com.app.authorization.server.domain.exception.InvalidViewCodeException;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import com.app.authorization.server.domain.model.valueobject.RoleID;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Alonso
 */
public final class Role {
  
  private RoleID id;
  
  private String name;
  
  private String description;
  
  private Set<PermissionID> permissionIds;
  
  private Set<String> viewCodes;
  
  private Role() {
  }
  
  private static void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new InvalidRoleNameException("Role name cannot be null or empty");
    }
  }
  
  public static Role create(String name, String description) {
    validateName(name);
    Role role = new Role();
    role.name = name.trim();
    role.description = description;
    role.permissionIds = new HashSet<>();
    role.viewCodes = new HashSet<>();
    return role;
  }
  
  public static Role rehydrate(RoleID id, String name, String description,
    Set<PermissionID> permissionIds,
    Set<String> viewCodes) {
    Role role = new Role();
    role.id = id;
    role.name = name;
    role.description = description;
    role.permissionIds = (permissionIds != null)
      ? new HashSet<>(permissionIds)
      : new HashSet<>();
    role.viewCodes = (viewCodes != null)
      ? new HashSet<>(viewCodes)
      : new HashSet<>();
    return role;
  }
  
  // ---- Comportamiento de dominio ----
  
  public void assignPermission(PermissionID permissionId) {
    Objects.requireNonNull(permissionId, "permissionId cannot be null");
    this.permissionIds.add(permissionId);
  }
  
  public void removePermission(PermissionID permissionId) {
    if (permissionId != null) {
      this.permissionIds.remove(permissionId);
    }
  }
  
  public void assignView(String viewCode) {
    if (viewCode == null || viewCode.trim().isEmpty()) {
      throw new InvalidViewCodeException("viewCode cannot be null or empty");
    }
    this.viewCodes.add(viewCode.trim());
  }
  
  public void removeView(String viewCode) {
    if (viewCode != null) {
      this.viewCodes.remove(viewCode);
    }
  }
  
  public boolean hasPermission(PermissionID permissionId) {
    return permissionId != null && this.permissionIds.contains(permissionId);
  }
  
  public boolean hasView(String viewCode) {
    return viewCode != null && this.viewCodes.contains(viewCode);
  }
  
  // ---- Getters ----
  
  public RoleID getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public Set<PermissionID> getPermissionIds() {
    return Collections.unmodifiableSet(permissionIds);
  }
  
  public Set<String> getViewCodes() {
    return Collections.unmodifiableSet(viewCodes);
  }
}
