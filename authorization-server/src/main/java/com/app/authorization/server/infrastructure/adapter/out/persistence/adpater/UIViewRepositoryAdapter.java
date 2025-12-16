package com.app.authorization.server.infrastructure.adapter.out.persistence.adpater;

import com.app.authorization.server.domain.model.UIView;
import com.app.authorization.server.domain.model.valueobject.UserID;
import com.app.authorization.server.domain.repository.UIViewRepository;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UIViewJpaEntity;
import com.app.authorization.server.infrastructure.adapter.out.persistence.mapper.UIViewJpaMapper;
import com.app.authorization.server.infrastructure.adapter.out.persistence.repository.JpaUIViewRepository;
import com.app.common.annotation.Adapter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alonso
 */
@Adapter
public class UIViewRepositoryAdapter implements UIViewRepository {
  
  private final JpaUIViewRepository jpaRepository;
  private final UIViewJpaMapper mapper;
  
  public UIViewRepositoryAdapter(JpaUIViewRepository jpaRepository, UIViewJpaMapper mapper) {
    this.jpaRepository = jpaRepository;
    this.mapper = mapper;
  }
  
  @Override
  public UIView save(UIView view) {
    UIViewJpaEntity entity = mapper.toEntity(view);
    UIViewJpaEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }
  
  @Override
  public Optional<UIView> findByCode(String code) {
    return jpaRepository.findById(code)
      .map(mapper::toDomain);
  }
  
  @Override
  public List<UIView> findAll() {
    return jpaRepository.findAll().stream()
      .map(mapper::toDomain)
      .collect(Collectors.toList());
  }
  
  @Override
  public void deleteByCode(String code) {
    jpaRepository.deleteById(code);
  }
  
  @Override
  public Set<UIView> findByUserId(UserID userId) {
    return jpaRepository.findByUserId(userId.getValue()).stream()
      .map(mapper::toDomain)
      .collect(Collectors.toSet());
  }
  
}
