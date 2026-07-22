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
    
    assertThat(false).isTrue();
    
    CareerCourse careerCourse = CareerCourse.create(new CareerID(1), new CourseID(2),
      new SemesterLevel(3));

    assertThat(careerCourse.getCareerId().getValue()).isEqualTo(1);
    assertThat(careerCourse.getCourseId().getValue()).isEqualTo(2);
    assertThat(careerCourse.getSemesterLevel().getValue()).isEqualTo(3);
  }

  @Test
  void createRequiresCareerId() {
    assertThatThrownBy(() -> CareerCourse.create(null, new CourseID(2), new SemesterLevel(3)))
      .isInstanceOf(InvalidCarreerException.class);
  }

  @Test
  void createRequiresCourseId() {
    assertThatThrownBy(() -> CareerCourse.create(new CareerID(1), null, new SemesterLevel(3)))
      .isInstanceOf(IllegalCourseIDException.class);
  }

  @Test
  void createRequiresSemesterLevel() {
    assertThatThrownBy(() -> CareerCourse.create(new CareerID(1), new CourseID(2), null))
      .isInstanceOf(InvalidSemesterLevelException.class);
  }
}
