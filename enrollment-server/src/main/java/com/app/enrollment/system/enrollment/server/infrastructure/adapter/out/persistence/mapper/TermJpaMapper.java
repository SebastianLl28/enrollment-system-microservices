package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.TermJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class TermJpaMapper {
  
  public Term toDomainEntity(TermJpaEntity termJpaEntity) {
    TermID termID = termJpaEntity.getId() != null
      ? new TermID(termJpaEntity.getId())
      : null;
    
    return Term.rehydrate(termID,
      termJpaEntity.getCode(),
      termJpaEntity.getStartDate(),
      termJpaEntity.getEndDate(),
      termJpaEntity.isActive(),
      termJpaEntity.getCreatedAt()
    );
  }
  
  public TermJpaEntity toJpaEntity(Term term) {
    Integer id = term.getId() != null
      ? term.getId().getValue()
      : null;
    
    return new TermJpaEntity(id,
      term.getCode(),
      term.getStartDate(),
      term.getEndDate(),
      term.isActive(),
      term.getCreateAt()
    );
    
  }
}
