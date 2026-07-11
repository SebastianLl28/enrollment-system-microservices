package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import org.junit.jupiter.api.Test;

class ClassroomTest {

  @Test
  void createStartsActive() {
    Classroom classroom = Classroom.create("B-201", "Laboratorio 1", 30, false, Mothers.NOW);

    assertThat(classroom.isActive()).isTrue();
    assertThat(classroom.getId()).isNull();
    assertThat(classroom.getCreatedAt()).isEqualTo(Mothers.NOW);
    assertThat(classroom.isVirtual()).isFalse();
    assertThat(classroom.getCapacity()).isEqualTo(30);
  }

  @Test
  void virtualClassroomHasNoCapacity() {
    Classroom classroom = Classroom.create("ZOOM-1", "Aula Zoom", null, true, Mothers.NOW);

    assertThat(classroom.isVirtual()).isTrue();
    assertThat(classroom.getCapacity()).isNull();
  }

  @Test
  void rehydrateRestoresState() {
    Classroom classroom = Mothers.physicalClassroom(5, "A-101", 25);

    assertThat(classroom.getId().getValue()).isEqualTo(5);
    assertThat(classroom.getCode()).isEqualTo("A-101");
    assertThat(classroom.getCapacity()).isEqualTo(25);
    assertThat(classroom.isVirtual()).isFalse();
  }
}
