package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseCodeException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseNameException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCreditsException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseCode;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Credits;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import org.junit.jupiter.api.Test;

class CourseTest {

  private static final CourseCode CODE = new CourseCode("CS101");
  private static final Credits CREDITS = new Credits(4);

  @Test
  void createBuildsActiveCourse() {
    Course course = Course.create(CODE, "Programación I", "Descripción", CREDITS, Mothers.NOW);

    assertThat(course.getCode()).isEqualTo(CODE);
    assertThat(course.getName()).isEqualTo("Programación I");
    assertThat(course.getCredits()).isEqualTo(CREDITS);
    assertThat(course.isActive()).isTrue();
    assertThat(course.getId()).isNull();
    assertThat(course.getRegistrationDate()).isEqualTo(Mothers.NOW);
  }

  @Test
  void createTrimsName() {
    Course course = Course.create(CODE, "  Matemáticas  ", null, CREDITS, Mothers.NOW);

    assertThat(course.getName()).isEqualTo("Matemáticas");
  }

  @Test
  void createAllowsNullDescription() {
    Course course = Course.create(CODE, "Algebra", null, CREDITS, Mothers.NOW);

    assertThat(course.getDescription()).isNull();
  }

  @Test
  void createTrimsDescription() {
    Course course = Course.create(CODE, "Algebra", "  desc  ", CREDITS, Mothers.NOW);

    assertThat(course.getDescription()).isEqualTo("desc");
  }

  @Test
  void createRequiresCode() {
    assertThatThrownBy(() -> Course.create(null, "Programación", null, CREDITS, Mothers.NOW))
      .isInstanceOf(InvalidCourseCodeException.class);
  }

  @Test
  void createRequiresName() {
    assertThatThrownBy(() -> Course.create(CODE, "", null, CREDITS, Mothers.NOW))
      .isInstanceOf(InvalidCourseNameException.class);
  }

  @Test
  void createRequiresNonBlankName() {
    assertThatThrownBy(() -> Course.create(CODE, "   ", null, CREDITS, Mothers.NOW))
      .isInstanceOf(InvalidCourseNameException.class);
  }

  @Test
  void createRequiresCredits() {
    assertThatThrownBy(() -> Course.create(CODE, "Programación", null, null, Mothers.NOW))
      .isInstanceOf(InvalidCreditsException.class);
  }

  @Test
  void createRequiresClock() {
    assertThatThrownBy(() -> Course.create(CODE, "Programación", null, CREDITS, null))
      .isInstanceOf(NullPointerException.class);
  }
}
