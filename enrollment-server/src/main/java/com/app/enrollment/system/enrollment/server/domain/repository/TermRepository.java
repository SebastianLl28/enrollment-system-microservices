package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface TermRepository {
  
  Term save(Term term);
  
  Optional<Term> findByCode(String code);
  
  Optional<Term> findById(TermID id);
  
  List<Term> findAll();
}
