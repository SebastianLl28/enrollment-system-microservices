package com.app.authorization.server.infrastructure.adapter.out.persistence.repository;

import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UIViewJpaEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alonso
 */

public interface JpaUIViewRepository extends JpaRepository<UIViewJpaEntity, String> {
  
  List<UIViewJpaEntity> findByCodeIn(Collection<String> codes);
  
  @Query(value = """
    select * from ui_view uv inner join	\s
    role_view rv on uv.code = rv.view_code
    inner join role r on rv.role_id  = r.id
    inner join user_role ur on r.id = ur.role_id
    inner join "user" u  on ur.user_id = u.id
    where u.id = :userId;
    """, nativeQuery = true)
  List<UIViewJpaEntity> findByUserId(Integer userId);
}
