package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.CareerNameRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDegreeTitleException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLengthException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DegreeTitle;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import org.junit.jupiter.api.Test;

class CareerTest {

  private static final FacultyID FACULTY = new FacultyID(1);
  private static final DegreeTitle DEGREE = new DegreeTitle("Profesional Técnico");

  @Test
  void createBuildsActiveCareer() {
    Career career = Career.create(FACULTY, "Sistemas", null, 6, DEGREE, Mothers.NOW);

    assertThat(career.getName()).isEqualTo("Sistemas");
    assertThat(career.getFacultyId()).isEqualTo(FACULTY);
    assertThat(career.getSemesterLength()).isEqualTo(6);
    assertThat(career.getDegreeAwarded()).isEqualTo(DEGREE);
    assertThat(career.isActive()).isTrue();
    assertThat(career.getId()).isNull();
    assertThat(career.getRegistrationDate()).isEqualTo(Mothers.NOW);
  }

  @Test
  void createTrimsName() {
    Career career = Career.create(FACULTY, "  Sistemas  ", null, 6, DEGREE, Mothers.NOW);

    assertThat(career.getName()).isEqualTo("Sistemas");
  }

  @Test
  void createAcceptsBoundarySemesterLength() {
    Career minCareer = Career.create(FACULTY, "Min", null, 1, DEGREE, Mothers.NOW);
    Career maxCareer = Career.create(FACULTY, "Max", null, 20, DEGREE, Mothers.NOW);

    assertThat(minCareer.getSemesterLength()).isEqualTo(1);
    assertThat(maxCareer.getSemesterLength()).isEqualTo(20);
  }

  @Test
  void createRequiresFacultyId() {
    assertThatThrownBy(() -> Career.create(null, "Sistemas", null, 6, DEGREE, Mothers.NOW))
      .isInstanceOf(FacultyRequiredException.class);
  }

  @Test
  void createRequiresName() {
    assertThatThrownBy(() -> Career.create(FACULTY, "", null, 6, DEGREE, Mothers.NOW))
      .isInstanceOf(CareerNameRequiredException.class);
  }

  @Test
  void createRequiresNonBlankName() {
    assertThatThrownBy(() -> Career.create(FACULTY, "   ", null, 6, DEGREE, Mothers.NOW))
      .isInstanceOf(CareerNameRequiredException.class);
  }

  @Test
  void createRejectsSemesterLengthBelowOne() {
    assertThatThrownBy(() -> Career.create(FACULTY, "Sistemas", null, 0, DEGREE, Mothers.NOW))
      .isInstanceOf(InvalidSemesterLengthException.class);
  }

  @Test
  void createRejectsSemesterLengthAboveTwenty() {
    assertThatThrownBy(() -> Career.create(FACULTY, "Sistemas", null, 21, DEGREE, Mothers.NOW))
      .isInstanceOf(InvalidSemesterLengthException.class);
  }

  @Test
  void createRequiresDegreeTitle() {
    assertThatThrownBy(() -> Career.create(FACULTY, "Sistemas", null, 6, null, Mothers.NOW))
      .isInstanceOf(InvalidDegreeTitleException.class);
  }

  @Test
  void createRequiresClock() {
    assertThatThrownBy(() -> Career.create(FACULTY, "Sistemas", null, 6, DEGREE, null))
      .isInstanceOf(NullPointerException.class);
  }
}
