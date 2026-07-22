package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.FacultyLocationRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNameRequiredException;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.time.Clock;
import org.junit.jupiter.api.Test;

class FacultyTest {

  private static final Clock CLOCK = Mothers.fixedClock();

  @Test
  void createBuildsActiveFaculty() {
    Faculty faculty = Faculty.create("Ingeniería", "Descripción", "Campus A", "Dr. Smith", CLOCK);

    assertThat(faculty.getName()).isEqualTo("Ingeniería");
    assertThat(faculty.getLocation()).isEqualTo("Campus A");
    assertThat(faculty.getDean()).isEqualTo("Dr. Smith");
    assertThat(faculty.isActive()).isTrue();
    assertThat(faculty.getId()).isNull();
  }

  @Test
  void createTrimsNameAndLocation() {
    Faculty faculty = Faculty.create("  Ciencias  ", null, "  Campus B  ", null, CLOCK);

    assertThat(faculty.getName()).isEqualTo("Ciencias");
    assertThat(faculty.getLocation()).isEqualTo("Campus B");
  }

  @Test
  void createRequiresName() {
    assertThatThrownBy(() -> Faculty.create("", "desc", "Campus A", null, CLOCK))
      .isInstanceOf(FacultyNameRequiredException.class);
  }

  @Test
  void createRequiresLocation() {
    assertThatThrownBy(() -> Faculty.create("Ingeniería", null, "", null, CLOCK))
      .isInstanceOf(FacultyLocationRequiredException.class);
  }

  @Test
  void createRejectsNameLongerThan100Chars() {
    String longName = "A".repeat(101);
    assertThatThrownBy(() -> Faculty.create(longName, null, "Campus A", null, CLOCK))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void renameChangesName() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);

    faculty.rename("Ciencias");

    assertThat(faculty.getName()).isEqualTo("Ciencias");
  }

  @Test
  void renameRejectsBlankName() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);

    assertThatThrownBy(() -> faculty.rename("   "))
      .isInstanceOf(FacultyNameRequiredException.class);
  }

  @Test
  void relocateChangesLocation() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);

    faculty.relocate("Campus B");

    assertThat(faculty.getLocation()).isEqualTo("Campus B");
  }

  @Test
  void relocateRejectsBlankLocation() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);

    assertThatThrownBy(() -> faculty.relocate(""))
      .isInstanceOf(FacultyLocationRequiredException.class);
  }

  @Test
  void deactivateSetsInactive() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);

    faculty.deactivate();

    assertThat(faculty.isActive()).isFalse();
  }

  @Test
  void deactivateIsIdempotent() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);
    faculty.deactivate();

    faculty.deactivate();

    assertThat(faculty.isActive()).isFalse();
  }

  @Test
  void activateRestoresActive() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);
    faculty.deactivate();

    faculty.activate();

    assertThat(faculty.isActive()).isTrue();
  }

  @Test
  void activateIsIdempotent() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);

    faculty.activate();

    assertThat(faculty.isActive()).isTrue();
  }

  @Test
  void changeDescriptionUpdatesDescription() {
    Faculty faculty = Faculty.create("Ingeniería", "desc original", "Campus A", null, CLOCK);

    faculty.changeDescription("nueva desc");

    assertThat(faculty.getDescription()).isEqualTo("nueva desc");
  }

  @Test
  void changeDescriptionAcceptsNull() {
    Faculty faculty = Faculty.create("Ingeniería", "desc", "Campus A", null, CLOCK);

    faculty.changeDescription(null);

    assertThat(faculty.getDescription()).isNull();
  }

  @Test
  void assignDeanUpdatesDean() {
    Faculty faculty = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);

    faculty.assignDean("Dr. Nuevo");

    assertThat(faculty.getDean()).isEqualTo("Dr. Nuevo");
  }

  @Test
  void equalityByNameIgnoringCase() {
    Faculty a = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);
    Faculty b = Faculty.create("ingeniería", null, "Campus B", null, CLOCK);

    assertThat(a).isEqualTo(b);
    assertThat(a).hasSameHashCodeAs(b);
  }

  @Test
  void inequalityDifferentNames() {
    Faculty a = Faculty.create("Ingeniería", null, "Campus A", null, CLOCK);
    Faculty b = Faculty.create("Ciencias", null, "Campus A", null, CLOCK);

    assertThat(a).isNotEqualTo(b);
  }
}
