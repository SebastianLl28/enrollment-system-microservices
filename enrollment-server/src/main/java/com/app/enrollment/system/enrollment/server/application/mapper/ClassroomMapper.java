package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class ClassroomMapper {

  public ClassroomResponse toClassroomResponse(Classroom classroom) {
    return new ClassroomResponse(
      classroom.getId().getValue(),
      classroom.getCode(),
      classroom.getName(),
      classroom.getCapacity(),
      classroom.isVirtual(),
      classroom.isActive(),
      classroom.getCreatedAt()
    );
  }

}
