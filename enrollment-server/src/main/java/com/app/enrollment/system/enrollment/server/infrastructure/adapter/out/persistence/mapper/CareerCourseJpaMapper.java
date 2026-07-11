package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.CareerCourse;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerCourseJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CareerCourseJpaMapper {

  public CareerCourse toDomainEntity(CareerCourseJpaEntity entity) {
    return CareerCourse.create(
      new CareerID(entity.getCareerId()),
      new CourseID(entity.getCourseId()),
      new SemesterLevel(entity.getSemesterLevel())
    );
  }

  public CareerCourseJpaEntity toJpaEntity(CareerCourse careerCourse) {
    return new CareerCourseJpaEntity(
      null,
      careerCourse.getCareerId().getValue(),
      careerCourse.getCourseId().getValue(),
      careerCourse.getSemesterLevel().getValue()
    );
  }

}
