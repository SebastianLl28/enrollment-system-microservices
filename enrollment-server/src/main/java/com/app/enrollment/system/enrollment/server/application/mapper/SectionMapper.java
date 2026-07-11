package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Section;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class SectionMapper {

  public SectionResponse toSectionResponse(Section section,
    CourseSummaryResponse courseSummaryResponse, TermResponse termResponse,
    ClassroomResponse classroomResponse) {
    return new SectionResponse(
      section.getId().getValue(),
      courseSummaryResponse,
      termResponse,
      classroomResponse,
      section.getSectionCode(),
      section.isActive(),
      section.getCreatedAt()
    );
  }

}
