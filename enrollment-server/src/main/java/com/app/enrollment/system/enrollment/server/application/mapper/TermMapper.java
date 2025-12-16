package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class TermMapper {
  
  public TermResponse toTermResponse(Term term) {
    return new TermResponse(
      term.getId().getValue(),
      term.getCode(),
      term.getStartDate(),
      term.getEndDate(),
      term.isActive(),
      term.getCreateAt()
    );
  }
}
