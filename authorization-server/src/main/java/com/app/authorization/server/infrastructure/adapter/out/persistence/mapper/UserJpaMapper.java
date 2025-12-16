package com.app.authorization.server.infrastructure.adapter.out.persistence.mapper;

import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.valueobject.Password;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class UserJpaMapper {
  
  public UserJpaEntity toEntity(User user) {
    return new UserJpaEntity(user.getId() != null ? user.getId().getValue() : null,
      user.getUsername(), user.getPassword() != null ? user.getPassword().getValue() : null,
      user.getFullName(), user.getEmail(), user.requiresTwoFactorAuth(), user.getTwoFactorSecret());
  }
  
  public User toDomain(UserJpaEntity userJpaEntity) {
    UserID userID = new UserID(userJpaEntity.getId());
    return User.rehydrate(userID, userJpaEntity.getUsername(),
      userJpaEntity.getPassword() != null ? Password.fromHash(userJpaEntity.getPassword()) : null,
      userJpaEntity.getFullName(), userJpaEntity.getEmail(), userJpaEntity.getTwoFactorEnabled(),
      userJpaEntity.getTwoFactorSecret());
  }
}
