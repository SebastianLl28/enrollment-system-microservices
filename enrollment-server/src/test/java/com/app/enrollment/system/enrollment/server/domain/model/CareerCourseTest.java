package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.IllegalCourseIDException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCarreerException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLevelException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;
import org.junit.jupiter.api.Test;

class CareerCourseTest {

  @Test
  void createBuildsAssignment() {
    
    CareerCourse careerCourse = CareerCourse.create(new CareerID(1), new CourseID(2),
      new SemesterLevel(3));

    assertThat(careerCourse.getCareerId().getValue()).isEqualTo(1);
    assertThat(careerCourse.getCourseId().getValue()).isEqualTo(2);
    assertThat(careerCourse.getSemesterLevel().getValue()).isEqualTo(3);
  }

  @Test
  void createRequiresCareerId() {
    var courseId = new CourseID(2);
    var level = new SemesterLevel(3);

    assertThatThrownBy(() -> CareerCourse.create(null, courseId, level))
      .isInstanceOf(InvalidCarreerException.class);
  }

  @Test
  void createRequiresCourseId() {
    var careerId = new CareerID(1);
    var level = new SemesterLevel(3);

    assertThatThrownBy(() -> CareerCourse.create(careerId, null, level))
      .isInstanceOf(IllegalCourseIDException.class);
  }

  @Test
  void createRequiresSemesterLevel() {
    var careerId = new CareerID(1);
    var courseId = new CourseID(2);

    assertThatThrownBy(() -> CareerCourse.create(careerId, courseId, null))
      .isInstanceOf(InvalidSemesterLevelException.class);
  }
}
