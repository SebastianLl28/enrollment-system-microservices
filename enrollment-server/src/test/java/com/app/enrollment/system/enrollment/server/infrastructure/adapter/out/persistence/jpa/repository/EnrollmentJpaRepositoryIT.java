package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerOfferingJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.EnrollmentJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.FacultyJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.StudentJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.TermJpaEntity;
import com.app.enrollment.system.enrollment.server.testsupport.PostgresContainerSupport;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba las queries JPQL custom contra un PostgreSQL real (Testcontainers).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EnrollmentJpaRepositoryIT extends PostgresContainerSupport {

  @Autowired
  private EnrollmentJpaRepository repository;

  @Autowired
  private TestEntityManager entityManager;

  private Integer softwareOfferingId;
  private Integer mechatronicsOfferingId;
  private Integer term1Id;
  private Integer term2Id;
  private Integer careerSoftwareId;
  private Integer studentAnaId;

  @BeforeEach
  void seed() {
    FacultyJpaEntity faculty = new FacultyJpaEntity();
    faculty.setName("Ingeniería");
    faculty.setActive(true);
    faculty = entityManager.persist(faculty);

    CareerJpaEntity software = career(faculty, "Diseño de Software");
    CareerJpaEntity mechatronics = career(faculty, "Mecatrónica");
    careerSoftwareId = software.getCareerId();

    TermJpaEntity term1 = entityManager.persist(new TermJpaEntity(null, "2026-I",
      LocalDate.of(2026, 3, 1), LocalDate.of(2026, 7, 31), true, Instant.now()));
    TermJpaEntity term2 = entityManager.persist(new TermJpaEntity(null, "2026-II",
      LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 20), true, Instant.now()));
    term1Id = term1.getId();
    term2Id = term2.getId();

    CareerOfferingJpaEntity softwareOffering = entityManager.persist(new CareerOfferingJpaEntity(
      null, software.getCareerId(), term1.getId(), 30, 0, true, Instant.now(),
      new BigDecimal("350.00")));
    CareerOfferingJpaEntity mechatronicsOffering = entityManager.persist(
      new CareerOfferingJpaEntity(null, mechatronics.getCareerId(), term2.getId(), 30, 0, true,
        Instant.now(), new BigDecimal("400.00")));
    softwareOfferingId = softwareOffering.getId();
    mechatronicsOfferingId = mechatronicsOffering.getId();

    StudentJpaEntity ana = student("12345678", "ana@example.com", "Ana");
    StudentJpaEntity luis = student("87654321", "luis@example.com", "Luis");
    studentAnaId = ana.getStudentId();

    enrollment(ana, softwareOffering, EnrollmentStatus.PENDING);
    enrollment(luis, softwareOffering, EnrollmentStatus.PAID);
    enrollment(ana, mechatronicsOffering, EnrollmentStatus.CANCELLED);

    entityManager.flush();
    entityManager.clear();
  }

  private CareerJpaEntity career(FacultyJpaEntity faculty, String name) {
    CareerJpaEntity career = new CareerJpaEntity();
    career.setFacultyId(faculty.getFacultyId());
    career.setName(name);
    career.setSemesterLength(6);
    career.setActive(true);
    return entityManager.persist(career);
  }

  private StudentJpaEntity student(String document, String email, String name) {
    StudentJpaEntity student = new StudentJpaEntity();
    student.setDocumentNumber(document);
    student.setName(name);
    student.setLastName("Pérez");
    student.setEmail(email);
    student.setDateOfBirth(LocalDate.of(2000, 1, 15));
    student.setActive(true);
    return entityManager.persist(student);
  }

  private void enrollment(StudentJpaEntity student, CareerOfferingJpaEntity offering,
    EnrollmentStatus status) {
    entityManager.persist(new EnrollmentJpaEntity(null, student.getStudentId(), offering.getId(),
      LocalDateTime.of(2026, 7, 11, 10, 0), null, status, 1));
  }

  @Test
  void findAllWithoutFiltersReturnsEverything() {
    Page<EnrollmentJpaEntity> page =
      repository.findAllByStudentIDAndTermIDAndCareerID(null, null, null, PageRequest.of(0, 10));

    assertThat(page.getTotalElements()).isEqualTo(3);
  }

  @Test
  void findAllFiltersByCareer() {
    Page<EnrollmentJpaEntity> page = repository.findAllByStudentIDAndTermIDAndCareerID(null, null,
      careerSoftwareId, PageRequest.of(0, 10));

    assertThat(page.getContent())
      .hasSize(2)
      .allMatch(e -> e.getCareerOfferingId().equals(softwareOfferingId));
  }

  @Test
  void findAllFiltersByStudentAndTerm() {
    Page<EnrollmentJpaEntity> page = repository.findAllByStudentIDAndTermIDAndCareerID(
      studentAnaId, term2Id, null, PageRequest.of(0, 10));

    assertThat(page.getContent())
      .hasSize(1)
      .allMatch(e -> e.getCareerOfferingId().equals(mechatronicsOfferingId));
  }

  @Test
  void findActiveEnrollmentIgnoresCancelled() {
    Optional<EnrollmentJpaEntity> active = repository.findByStudentIdAndCareerOfferingIdAndStatusIn(
      studentAnaId, mechatronicsOfferingId,
      EnumSet.of(EnrollmentStatus.PENDING, EnrollmentStatus.PAID));

    assertThat(active).isEmpty();

    Optional<EnrollmentJpaEntity> pending =
      repository.findByStudentIdAndCareerOfferingIdAndStatusIn(studentAnaId, softwareOfferingId,
        EnumSet.of(EnrollmentStatus.PENDING, EnrollmentStatus.PAID));

    assertThat(pending).isPresent();
    assertThat(pending.get().getStudentId()).isEqualTo(studentAnaId);
  }

  @Test
  void countGroupByCareerOrdersByEnrollmentsDesc() {
    List<Object[]> rows = repository.countGroupByCareer(PageRequest.of(0, 5));

    assertThat(rows).hasSize(2);
    assertThat(rows.get(0)[0]).isEqualTo("Diseño de Software");
    assertThat(((Number) rows.get(0)[1]).longValue()).isEqualTo(2L);
  }

  @Test
  void countByTermIdCountsEnrollmentsOfThatTerm() {
    assertThat(repository.countByTermId(term1Id)).isEqualTo(2);
    assertThat(repository.countByTermId(term2Id)).isEqualTo(1);
  }

  @Test
  void countGroupByStatusAggregates() {
    List<Object[]> rows = repository.countGroupByStatus();

    assertThat(rows).hasSize(3);
    assertThat(rows).extracting(row -> row[0])
      .containsExactlyInAnyOrder(EnrollmentStatus.PENDING, EnrollmentStatus.PAID,
        EnrollmentStatus.CANCELLED);
  }
}
