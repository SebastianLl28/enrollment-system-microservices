package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CourseOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CourseOfferingMapper {

  public CourseOfferingResponse toCourseOfferingResponse(CourseOffering courseOffering, CourseSummaryResponse courseSummaryResponse, TermResponse termResponse) {
    return new CourseOfferingResponse(
      courseOffering.getId().getValue(),
      courseSummaryResponse,
      termResponse,
      courseOffering.getSectionCode(),
      courseOffering.getCapacity(),
      courseOffering.getEnrolledCount(),
      courseOffering.isActive(),
      courseOffering.getCreatedAt()
    );
  }
  
}
