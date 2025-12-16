package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.CourseOfferingResponse;
import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllCourseOfferingUseCase {
  
  List<CourseOfferingResponse> getAllCourseOfferings();
  
}
