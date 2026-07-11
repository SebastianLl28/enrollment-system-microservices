package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseCode;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Credits;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CourseJpaEntity;
import java.time.Clock;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CourseJpaMapper {

  private final Clock clock;

  public CourseJpaMapper(Clock clock) {
    this.clock = clock;
  }

  public Course toDomainCourse(CourseJpaEntity courseJpaEntity) {
    CourseID courseID = new CourseID(courseJpaEntity.getCourseId());
    CourseCode courseCode = new CourseCode(courseJpaEntity.getCode());
    Credits credits = new Credits(courseJpaEntity.getCredits());
    Instant registrationDate = courseJpaEntity.getRegistrationDate().atZone(clock.getZone())
      .toInstant();
    return Course.rehydrate(courseID, courseCode, courseJpaEntity.getName(),
      courseJpaEntity.getDescription(), credits, registrationDate,
      courseJpaEntity.getActive());
  }

  public CourseJpaEntity toJpaEntity(Course course) {
    CourseJpaEntity courseJpaEntity = new CourseJpaEntity();

    if (course.getId() != null) {
      courseJpaEntity.setCourseId(course.getId().getValue());
    }

    courseJpaEntity.setCode(course.getCode().getValue());
    courseJpaEntity.setName(course.getName());
    courseJpaEntity.setDescription(course.getDescription());
    courseJpaEntity.setCredits(course.getCredits().getValue());
    courseJpaEntity.setRegistrationDate(
      Instant.ofEpochMilli(course.getRegistrationDate().toEpochMilli()).atZone(clock.getZone())
        .toLocalDateTime());
    courseJpaEntity.setActive(course.isActive());
    return courseJpaEntity;
  }

}
