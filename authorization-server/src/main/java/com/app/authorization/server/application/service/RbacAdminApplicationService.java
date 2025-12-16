package com.app.authorization.server.application.service;

import com.app.authorization.server.application.dto.command.CreatePermissionCommand;
import com.app.authorization.server.application.dto.command.CreateRoleCommand;
import com.app.authorization.server.application.dto.command.CreateUIViewCommand;
import com.app.authorization.server.application.dto.command.UpdatePermissionCommand;
import com.app.authorization.server.application.dto.command.UpdateRoleCommand;
import com.app.authorization.server.application.dto.command.UpdateUIViewCommand;
import com.app.authorization.server.application.dto.command.UpdateUserRolesCommand;
import com.app.authorization.server.application.dto.response.PermissionResponse;
import com.app.authorization.server.application.dto.response.RoleResponse;
import com.app.authorization.server.application.dto.response.UIViewResponse;
import com.app.authorization.server.application.dto.response.UserRbacResponse;
import com.app.authorization.server.application.mapper.UserMapper;
import com.app.authorization.server.application.port.in.RbacPermissionUseCase;
import com.app.authorization.server.application.port.in.RbacRoleUseCase;
import com.app.authorization.server.application.port.in.RbacViewUseCase;
import com.app.authorization.server.application.port.in.UpdateUserRolesUseCase;
import com.app.authorization.server.domain.exception.UserNotFoundException;
import com.app.authorization.server.domain.model.Permission;
import com.app.authorization.server.domain.model.Role;
import com.app.authorization.server.domain.model.UIView;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import com.app.authorization.server.domain.model.valueobject.RoleID;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.PermissionRepository;
import com.app.authorization.server.domain.repository.RoleRepository;
import com.app.authorization.server.domain.repository.UIViewRepository;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import com.app.common.annotation.UseCase;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */
@UseCase
@Transactional
public class RbacAdminApplicationService implements RbacPermissionUseCase, RbacRoleUseCase,
  RbacViewUseCase, UpdateUserRolesUseCase {
  
  private final PermissionRepository permissionRepository;
  private final RoleRepository roleRepository;
  private final UIViewRepository viewRepository;
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserAccessProfilePort userAccessProfilePort;
  private final UIViewRepository uIViewRepository;
  
  public RbacAdminApplicationService(PermissionRepository permissionRepository,
    RoleRepository roleRepository, UIViewRepository viewRepository, UserMapper userMapper,
    UserRepository userRepository, UserAccessProfilePort userAccessProfilePort,
    UIViewRepository uIViewRepository) {
    this.permissionRepository = permissionRepository;
    this.roleRepository = roleRepository;
    this.viewRepository = viewRepository;
    this.userMapper = userMapper;
    this.userRepository = userRepository;
    this.userAccessProfilePort = userAccessProfilePort;
    this.uIViewRepository = uIViewRepository;
  }
  
  // =========================================================
  //               PERMISSIONS
  // =========================================================
  
  @Override
  public PermissionResponse createPermission(CreatePermissionCommand command) {
    // 1) Dominio
    Permission permission = Permission.create(command.resource(), command.operation(),
      command.scope(), command.description());
    
    // 2) Persistir
    Permission saved = permissionRepository.save(permission);
    
    // 3) Response
    return toPermissionResponse(saved);
  }
  
  @Override
  public PermissionResponse updatePermission(UpdatePermissionCommand command) {
    PermissionID id = new PermissionID(command.id());
    
    // 1) Asegurar que existe
    Permission existing = permissionRepository.findById(id)
      .orElseThrow(() -> new NoSuchElementException("Permission not found: " + command.id()));
    
    // 2) Reconstruir (o podrías tener setters si quieres mutar)
    Permission updated = Permission.rehydrate(existing.getId(), command.resource(),
      command.operation(), command.scope(), command.description());
    
    // 3) Guardar
    Permission saved = permissionRepository.save(updated);
    
    return toPermissionResponse(saved);
  }
  
  @Override
  public void deletePermission(Integer id) {
    permissionRepository.deleteById(new PermissionID(id));
  }
  
  @Override
  public PermissionResponse getPermission(Integer id) {
    return permissionRepository.findById(new PermissionID(id)).map(this::toPermissionResponse)
      .orElseThrow(() -> new NoSuchElementException("Permission not found: " + id));
  }
  
  @Override
  public List<PermissionResponse> listPermissions() {
    return permissionRepository.findAll().stream().map(this::toPermissionResponse)
      .collect(Collectors.toList());
  }
  
  private PermissionResponse toPermissionResponse(Permission permission) {
    return new PermissionResponse(permission.getId().getValue(), permission.getResource(),
      permission.getOperation(), permission.getScope(), permission.getDescription());
  }
  
  // =========================================================
  //               UI VIEWS
  // =========================================================
  
  @Override
  public UIViewResponse createView(CreateUIViewCommand command) {
    UIView view = UIView.create(command.code(), command.route(), command.label(), command.module(),
      command.sortOrder());
    
    UIView saved = viewRepository.save(view);
    return toUIViewResponse(saved);
  }
  
  @Override
  public UIViewResponse updateView(UpdateUIViewCommand command) {
    // asumo que code es PK
    UIView existing = viewRepository.findByCode(command.code())
      .orElseThrow(() -> new NoSuchElementException("UIView not found: " + command.code()));
    
    UIView updated = UIView.rehydrate(command.code(), command.route(), command.label(),
      command.module(), command.sortOrder(), command.active());
    
    UIView saved = viewRepository.save(updated);
    return toUIViewResponse(saved);
  }
  
  @Override
  public void deleteView(String code) {
    viewRepository.deleteByCode(code);
  }
  
  @Override
  public UIViewResponse getView(String code) {
    UIView view = viewRepository.findByCode(code)
      .orElseThrow(() -> new NoSuchElementException("UIView not found: " + code));
    return toUIViewResponse(view);
  }
  
  @Override
  public List<UIViewResponse> listViews() {
    return viewRepository.findAll().stream().map(this::toUIViewResponse)
      .collect(Collectors.toList());
  }
  
  private UIViewResponse toUIViewResponse(UIView view) {
    return new UIViewResponse(view.getCode(), view.getRoute(), view.getLabel(), view.getModule(),
      view.getSortOrder(), view.isActive());
  }
  
  // =========================================================
  //               ROLES
  // =========================================================
  
  @Override
  public RoleResponse createRole(CreateRoleCommand command) {
    Role role = Role.create(command.name(), command.description());
    
    if (command.permissionIds() != null) {
      Set<PermissionID> permissionIDs = command.permissionIds().stream().map(PermissionID::new)
        .collect(Collectors.toSet());
      permissionIDs.forEach(role::assignPermission);
    }
    
    if (command.viewCodes() != null) {
      command.viewCodes().forEach(role::assignView);
    }
    
    Role saved = roleRepository.save(role);
    
    return toRoleResponse(saved);
  }
  
  @Override
  public RoleResponse updateRole(UpdateRoleCommand command) {
    RoleID roleId = new RoleID(command.id());
    
    Role existing = roleRepository.findById(roleId)
      .orElseThrow(() -> new NoSuchElementException("Role not found: " + command.id()));
    
    Set<PermissionID> permissionIDs =
      (command.permissionIds() != null) ? command.permissionIds().stream().map(PermissionID::new)
        .collect(Collectors.toSet()) : existing.getPermissionIds();
    
    Set<String> viewCodes =
      (command.viewCodes() != null) ? Set.copyOf(command.viewCodes()) : existing.getViewCodes();
    
    Role updated = Role.rehydrate(existing.getId(), command.name(), command.description(),
      permissionIDs, viewCodes);
    
    Role saved = roleRepository.save(updated);
    
    return toRoleResponse(saved);
  }
  
  @Override
  public void deleteRole(Integer id) {
    roleRepository.deleteById(new RoleID(id));
  }
  
  @Override
  public RoleResponse getRole(Integer id) {
    Role role = roleRepository.findById(new RoleID(id))
      .orElseThrow(() -> new NoSuchElementException("Role not found: " + id));
    return toRoleResponse(role);
  }
  
  @Override
  public List<RoleResponse> listRoles() {
    return roleRepository.findAll().stream().map(this::toRoleResponse).collect(Collectors.toList());
  }
  
  private RoleResponse toRoleResponse(Role role) {
    List<Integer> permissionIds = role.getPermissionIds().stream().map(PermissionID::getValue)
      .collect(Collectors.toList());
    
    List<String> viewCodes = role.getViewCodes().stream().toList();
    
    return new RoleResponse(role.getId().getValue(), role.getName(), role.getDescription(),
      permissionIds, viewCodes);
  }
  
  @Override
  public UserRbacResponse updateUserRoles(UpdateUserRolesCommand command) {
    Integer userId = command.userId();
    UserID userID = new UserID(userId);
    Set<RoleID> roleIDSet = command.roleIds().stream().map(RoleID::new).collect(Collectors.toSet());
    
    roleRepository.updateUserRoles(userID, roleIDSet);
    
    User user = userRepository.findById(userID)
      .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    
    UserAccessProfile profile = userAccessProfilePort.loadByUsername(user.getUsername());
    
    Set<UIView> uiViews = uIViewRepository.findByUserId(user.getId());
    
    Set<UIViewResponse> uiViewsResponses = uiViews.stream().map(
      view -> new UIViewResponse(view.getCode(), view.getRoute(), view.getLabel(), view.getModule(),
        view.getSortOrder(), view.isActive())).collect(java.util.stream.Collectors.toSet());
    
    return userMapper.toUserRbacResponse(user, profile.getPermissions(), uiViewsResponses);
  }
}
