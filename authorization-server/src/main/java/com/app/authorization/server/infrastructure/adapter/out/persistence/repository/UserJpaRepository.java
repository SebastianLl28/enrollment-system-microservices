package com.app.authorization.server.infrastructure.adapter.out.persistence.repository;

import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alonso
 */
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Integer> {
  
  UserJpaEntity findByUsername(String username);
  
  UserJpaEntity findByEmail(String email);
  
  @EntityGraph(attributePaths = {"roles", "roles.permissions", "roles.views"})
  @Query("""
      SELECT u
      FROM UserJpaEntity u
      WHERE u.username = :username
    """)
  Optional<UserJpaEntity> findByUsernameWithRoles(String username);
  
  @Modifying
  @Query("""
      update UserJpaEntity u
      set u.twoFactorEnabled = :enabled,
          u.twoFactorSecret = :secret
      where u.id = :userId
    """)
  void updateTwoFactor(Integer userId, boolean enabled, String secret);
}
