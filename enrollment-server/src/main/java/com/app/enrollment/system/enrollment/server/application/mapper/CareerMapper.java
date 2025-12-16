package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultySummaryResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CareerMapper {
  
  private final CourseMapper courseMapper;
  
  public CareerMapper(CourseMapper courseMapper) {
    this.courseMapper = courseMapper;
  }
  
  public CareerResponse toResponse(Career career, Faculty faculty, List<Course> courseList) {
    CareerResponse response = new CareerResponse();
    response.setId(career.getId().getValue());
    response.setName(career.getName());
    response.setDescription(career.getDescription());
    response.setSemesterLength(career.getSemesterLength());
    response.setDegreeAwarded(career.getDegreeAwarded().getValue());
    response.setRegistrationDate(career.getRegistrationDate());
    response.setActive(career.isActive());
    
    FacultySummaryResponse facultySummaryResponse = new FacultySummaryResponse();
    facultySummaryResponse.setId(faculty.getId().getValue());
    facultySummaryResponse.setName(faculty.getName());
    
    response.setFaculty(facultySummaryResponse);
    
    List<CourseSummaryResponse> courseSummaryResponseList = courseList.stream().map(courseMapper::toSummaryResponse).toList();
    
    response.setCourseList(courseSummaryResponseList);
    
    return response;
  }
  
}
