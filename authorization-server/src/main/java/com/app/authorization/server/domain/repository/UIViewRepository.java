package com.app.authorization.server.domain.repository;

import com.app.authorization.server.domain.model.UIView;
import com.app.authorization.server.domain.model.valueobject.UserID;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alonso
 */
public interface UIViewRepository {
  
  UIView save(UIView view);
  
  Optional<UIView> findByCode(String code);
  
  List<UIView> findAll();
  
  void deleteByCode(String code);
  
  Set<UIView> findByUserId(UserID userId);

}
