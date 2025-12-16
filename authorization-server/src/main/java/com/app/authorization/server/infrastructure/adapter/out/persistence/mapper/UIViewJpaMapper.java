package com.app.authorization.server.infrastructure.adapter.out.persistence.mapper;

import com.app.authorization.server.domain.model.UIView;
import com.app.authorization.server.infrastructure.adapter.out.persistence.entity.UIViewJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class UIViewJpaMapper {
  
  public UIViewJpaEntity toEntity(UIView view) {
    return new UIViewJpaEntity(
      view.getCode(),
      view.getRoute(),
      view.getLabel(),
      view.getModule(),
      view.getSortOrder(),
      view.isActive()
    );
  }
  
  public UIView toDomain(UIViewJpaEntity entity) {
    return UIView.rehydrate(
      entity.getCode(),
      entity.getRoute(),
      entity.getLabel(),
      entity.getModule(),
      entity.getSortOrder(),
      entity.isActive()
    );
  }
  
}
