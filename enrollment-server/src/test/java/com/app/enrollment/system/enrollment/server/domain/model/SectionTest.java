package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import org.junit.jupiter.api.Test;

class SectionTest {

  @Test
  void createStartsActiveWithoutId() {
    Section section = Section.create(new CourseID(1), new TermID(2), new ClassroomID(3), "A",
      Mothers.NOW);

    assertThat(section.getId()).isNull();
    assertThat(section.isActive()).isTrue();
    assertThat(section.getSectionCode()).isEqualTo("A");
    assertThat(section.getCreatedAt()).isEqualTo(Mothers.NOW);
  }

  @Test
  void rehydrateRestoresState() {
    Section section = Mothers.section(7, 1, 2, 3, "B");

    assertThat(section.getId().getValue()).isEqualTo(7);
    assertThat(section.getCourseId().getValue()).isEqualTo(1);
    assertThat(section.getTermId().getValue()).isEqualTo(2);
    assertThat(section.getClassroomId().getValue()).isEqualTo(3);
    assertThat(section.getSectionCode()).isEqualTo("B");
  }
}
