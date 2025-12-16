package com.app.authorization.server.infrastructure.adapter.out.persistence.adpater;

import com.app.authorization.server.domain.exception.UserNotFoundException;
import com.app.authorization.server.domain.model.Role;
import com.app.authorization.server.domain.model.valueobject.PermissionID;
import com.app.authorization.server.domain.model.valueobject.RoleID;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.RoleRepository;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.PermissionJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.RoleJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UIViewJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.mapper.RoleJpaMapper;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.JpaPermissionRepository;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.JpaRoleRepository;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.JpaUIViewRepository;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import com.app.common.annotation.Adapter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alonso
 */
@Adapter
public class RoleRepositoryAdapter implements RoleRepository {
  
  private final JpaRoleRepository roleRepository;
  private final JpaPermissionRepository permissionRepository;
  private final UserJpaRepository userJpaRepository;
  private final JpaUIViewRepository viewRepository;
  private final RoleJpaMapper roleJpaMapper;
  
  public RoleRepositoryAdapter(JpaRoleRepository roleRepository,
    JpaPermissionRepository permissionRepository, UserJpaRepository userJpaRepository, JpaUIViewRepository viewRepository,
    RoleJpaMapper roleJpaMapper) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
    this.userJpaRepository = userJpaRepository;
    this.viewRepository = viewRepository;
    this.roleJpaMapper = roleJpaMapper;
  }
  
  @Override
  public Role save(Role role) {
    List<PermissionJpaEntity> permissionEntities = Collections.emptyList();
    if (!role.getPermissionIds().isEmpty()) {
      List<Integer> ids = role.getPermissionIds().stream().map(PermissionID::getValue).toList();
      permissionEntities = permissionRepository.findByIdIn(ids);
    }
    
    List<UIViewJpaEntity> viewEntities = Collections.emptyList();
    if (!role.getViewCodes().isEmpty()) {
      viewEntities = viewRepository.findByCodeIn(role.getViewCodes());
    }
    
    RoleJpaEntity entity = roleJpaMapper.toEntity(role, permissionEntities, viewEntities);
    RoleJpaEntity saved = roleRepository.save(entity);
    
    return roleJpaMapper.toDomain(saved);
  }
  
  @Override
  public Optional<Role> findById(RoleID id) {
    return roleRepository.findById(id.getValue()).map(roleJpaMapper::toDomain);
  }
  
  @Override
  public List<Role> findAll() {
    return roleRepository.findAll().stream().map(roleJpaMapper::toDomain)
      .collect(Collectors.toList());
  }
  
  @Override
  public void deleteById(RoleID id) {
    roleRepository.deleteById(id.getValue());
  }
  
  @Override
  public void updateUserRoles(UserID userID, Set<RoleID> roleIDs) {
    Set<RoleJpaEntity> roles = roleRepository.findAllByIdIn(
      roleIDs.stream().map(RoleID::getValue).collect(Collectors.toSet())
    );
    
    UserJpaEntity user = userJpaRepository.findById(userID.getValue()).orElseThrow(
      () -> new UserNotFoundException("User not found: " + userID.getValue())
    );
    
    user.setRoles(roles);
    
    userJpaRepository.save(user);
    
  }
}
