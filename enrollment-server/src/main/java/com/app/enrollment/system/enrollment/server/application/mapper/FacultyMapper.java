package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class FacultyMapper {
  
  public FacultyResponse toResponse(Faculty faculty) {
    FacultyResponse response = new FacultyResponse();
    response.setId(faculty.getId().getValue());
    response.setName(faculty.getName());
    response.setLocation(faculty.getLocation());
    response.setActive(faculty.isActive());
    response.setDean(faculty.getDean());
    return response;
  }
  
}
