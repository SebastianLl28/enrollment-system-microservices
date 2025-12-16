package com.app.authorization.server.infrastructure.adapter.out.persistence.adpater;

import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.UserRepository;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.RoleJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.mapper.UserJpaMapper;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import com.app.common.annotation.Adapter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * @author Alonso
 */
@Adapter
public class UserRepositoryJpaAdapter implements UserRepository {
  
  private final UserJpaMapper userJpaMapper;
  private final UserJpaRepository userJpaRepository;
  
  public UserRepositoryJpaAdapter(UserJpaMapper userJpaMapper,
    UserJpaRepository userJpaRepository) {
    this.userJpaMapper = userJpaMapper;
    this.userJpaRepository = userJpaRepository;
  }
  
  @Override
  public Optional<User> findByUsername(String username) {
    UserJpaEntity userJpaEntity = userJpaRepository.findByUsername(username);
    if (userJpaEntity == null) {
      return Optional.empty();
    } else {
      return Optional.of(userJpaMapper.toDomain(userJpaEntity));
    }
  }
  
  @Override
  public User save(User user) {
    UserJpaEntity entity = userJpaMapper.toEntity(user);
    UserJpaEntity saved = userJpaRepository.save(entity);
    return userJpaMapper.toDomain(saved);
  }
  
  @Override
  public Optional<User> findById(UserID id) {
    return userJpaRepository.findById(id.getValue()).map(userJpaMapper::toDomain);
  }
  
  @Override
  public Optional<User> findByEmail(String email) {
    UserJpaEntity userJpaEntity = userJpaRepository.findByEmail(email);
    if (userJpaEntity == null) {
      return Optional.empty();
    } else {
      return Optional.of(userJpaMapper.toDomain(userJpaEntity));
    }
  }
  
  @Override
  public List<User> findAll() {
    return userJpaRepository.findAll().stream()
      .map(userJpaMapper::toDomain)
      .toList();
  }
  
  @Override
  public void updateTwoFactor(UserID userID, boolean enabled, String secret) {
    UserJpaEntity userJpaEntity = userJpaRepository.findById(userID.getValue())
      .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userID.getValue()));
    
    userJpaRepository.updateTwoFactor( userJpaEntity.getId(), enabled, secret);
  
  }
}
