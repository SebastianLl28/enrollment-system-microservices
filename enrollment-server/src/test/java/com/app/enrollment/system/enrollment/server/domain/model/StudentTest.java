package com.app.enrollment.system.enrollment.server.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDocumentNumberException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentAttributeException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentNameException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DocumentNumber;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Email;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class StudentTest {

  private static final DocumentNumber DNI = new DocumentNumber("12345678");
  private static final Email EMAIL = new Email("ana@example.com");
  private static final LocalDate DOB = LocalDate.of(2000, 1, 15);

  @Test
  void createBuildsActiveStudent() {
    Student student = Student.create("Ana", "Pérez", DNI, EMAIL, "999", DOB, "Av. X", Mothers.NOW);

    assertThat(student.getName()).isEqualTo("Ana");
    assertThat(student.getLastName()).isEqualTo("Pérez");
    assertThat(student.getDocumentNumber()).isEqualTo(DNI);
    assertThat(student.getEmail()).isEqualTo(EMAIL);
    assertThat(student.isActive()).isTrue();
    assertThat(student.getId()).isNull();
    assertThat(student.getCreatedAt()).isEqualTo(Mothers.NOW);
  }

  @Test
  void getFullNameConcatenatesNameAndLastName() {
    Student student = Student.create("Ana", "García", DNI, EMAIL, null, DOB, null, Mothers.NOW);

    assertThat(student.getFullName()).isEqualTo("Ana García");
  }

  @Test
  void createRequiresName() {
    assertThatThrownBy(() -> Student.create("", "Pérez", DNI, EMAIL, null, DOB, null, Mothers.NOW))
      .isInstanceOf(InvalidStudentNameException.class);
  }

  @Test
  void createRequiresNonBlankName() {
    assertThatThrownBy(
      () -> Student.create("   ", "Pérez", DNI, EMAIL, null, DOB, null, Mothers.NOW))
      .isInstanceOf(InvalidStudentNameException.class);
  }

  @Test
  void createRequiresLastName() {
    assertThatThrownBy(() -> Student.create("Ana", "", DNI, EMAIL, null, DOB, null, Mothers.NOW))
      .isInstanceOf(InvalidStudentNameException.class);
  }

  @Test
  void createRequiresDocumentNumber() {
    assertThatThrownBy(() -> Student.create("Ana", "Pérez", null, EMAIL, null, DOB, null, Mothers.NOW))
      .isInstanceOf(InvalidDocumentNumberException.class);
  }

  @Test
  void createRequiresDateOfBirth() {
    assertThatThrownBy(() -> Student.create("Ana", "Pérez", DNI, EMAIL, null, null, null, Mothers.NOW))
      .isInstanceOf(InvalidStudentAttributeException.class);
  }

  @Test
  void createRequiresClock() {
    assertThatThrownBy(() -> Student.create("Ana", "Pérez", DNI, EMAIL, null, DOB, null, null))
      .isInstanceOf(NullPointerException.class);
  }

  @Test
  void createAllowsNullEmailAndPhone() {
    Student student = Student.create("Ana", "Pérez", DNI, null, null, DOB, null, Mothers.NOW);

    assertThat(student.getEmail()).isNull();
    assertThat(student.getPhoneNumber()).isNull();
  }
}
