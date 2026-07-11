package com.app.enrollment.system.enrollment.server.testsupport;

import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.Section;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseCode;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Credits;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DegreeTitle;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DocumentNumber;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Email;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SectionID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Object mothers: construyen agregados de dominio válidos para los tests, con
 * valores por defecto razonables. Cada test sobrescribe solo lo que le importa.
 */
public final class Mothers {

  public static final Instant NOW = Instant.parse("2026-07-11T10:00:00Z");

  private Mothers() {
  }

  public static Clock fixedClock() {
    return Clock.fixed(NOW, ZoneOffset.UTC);
  }

  public static Student student(int id) {
    return Student.rehydrate(new StudentID(id), "Ana", "Pérez", new DocumentNumber("12345678"),
      new Email("ana.perez@example.com"), "999888777", LocalDate.of(2000, 1, 15),
      "Av. Siempre Viva 123", NOW, true);
  }

  public static Career career(int id, String name) {
    return Career.rehydrate(new CareerID(id), new FacultyID(1), name, "descripción", 6,
      new DegreeTitle("Profesional Técnico"), NOW, true);
  }

  public static Term term(int id, String code) {
    return Term.rehydrate(new TermID(id), code, LocalDate.of(2026, 3, 1),
      LocalDate.of(2026, 7, 31), true, NOW);
  }

  public static Course course(int id, String code, String name) {
    return Course.rehydrate(new CourseID(id), new CourseCode(code), name, "descripción",
      new Credits(3), NOW, true);
  }

  public static CareerOffering careerOffering(int id, int careerId, int termId, int capacity,
    int enrolledCount, boolean active, BigDecimal price) {
    return CareerOffering.rehydrate(new CareerOfferingID(id), new CareerID(careerId),
      new TermID(termId), capacity, enrolledCount, active, NOW, price);
  }

  public static CareerOffering careerOffering(int id, int careerId, int termId) {
    return careerOffering(id, careerId, termId, 30, 0, true, new BigDecimal("350.00"));
  }

  public static Enrollment enrollment(int id, int studentId, int careerOfferingId,
    EnrollmentStatus status) {
    return Enrollment.rehydrate(new EnrollmentID(id), new StudentID(studentId),
      new CareerOfferingID(careerOfferingId), NOW, null, status, new UserID(1), null, null, null);
  }

  public static Classroom physicalClassroom(int id, String code, int capacity) {
    return Classroom.rehydrate(new ClassroomID(id), code, "Aula " + code, capacity, false, true,
      NOW);
  }

  public static Classroom virtualClassroom(int id, String code) {
    return Classroom.rehydrate(new ClassroomID(id), code, "Aula Virtual " + code, null, true, true,
      NOW);
  }

  public static Section section(int id, int courseId, int termId, int classroomId, String code) {
    return Section.rehydrate(new SectionID(id), new CourseID(courseId), new TermID(termId),
      new ClassroomID(classroomId), code, true, NOW);
  }
}
