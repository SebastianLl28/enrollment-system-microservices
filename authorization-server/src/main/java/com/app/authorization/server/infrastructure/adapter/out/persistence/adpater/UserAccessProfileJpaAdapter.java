package com.app.authorization.server.infrastructure.adapter.out.persistence.adpater;

import com.app.authorization.server.domain.exception.UserNotFoundException;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.RoleJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UIViewJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import com.app.common.annotation.Adapter;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alonso
 */
@Adapter
public class UserAccessProfileJpaAdapter implements UserAccessProfilePort {
  
  private final UserJpaRepository userRepository;
  
  public UserAccessProfileJpaAdapter(UserJpaRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  @Override
  public UserAccessProfile loadByUsername(String username) {
    UserJpaEntity user = userRepository.findByUsernameWithRoles(username)
      .orElseThrow(() -> new UserNotFoundException(username));
    
    Set<String> roles = user.getRoles().stream()
      .map(RoleJpaEntity::getName)
      .collect(Collectors.toSet());
    
    Set<String> permissions = user.getRoles().stream()
      .flatMap(role -> role.getPermissions().stream())
      .map(p -> p.getResource() + ":" + p.getOperation() + ":" + p.getScope())
      .collect(Collectors.toSet());
    
    Set<String> viewCodes = user.getRoles().stream()
      .flatMap(role -> role.getViews().stream())
      .map(UIViewJpaEntity::getCode)
      .collect(Collectors.toSet());
    
    return new UserAccessProfile(
      user.getId(),
      user.getUsername(),
      roles,
      permissions,
      viewCodes
    );
  }
}
