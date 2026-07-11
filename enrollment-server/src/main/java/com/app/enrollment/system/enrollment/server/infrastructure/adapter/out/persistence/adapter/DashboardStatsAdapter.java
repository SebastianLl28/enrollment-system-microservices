package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.application.dto.response.LabelCountResponse;
import com.app.enrollment.system.enrollment.server.application.port.out.DashboardStatsPort;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.CareerOfferingJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.CourseJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.EnrollmentJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.StudentJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.TermJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.TermJpaMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alonso
 */
@Adapter
public class DashboardStatsAdapter implements DashboardStatsPort {

  private final StudentJpaRepository studentJpaRepository;
  private final CourseJpaRepository courseJpaRepository;
  private final CareerOfferingJpaRepository careerOfferingJpaRepository;
  private final EnrollmentJpaRepository enrollmentJpaRepository;
  private final TermJpaRepository termJpaRepository;
  private final TermJpaMapper termJpaMapper;

  public DashboardStatsAdapter(StudentJpaRepository studentJpaRepository,
    CourseJpaRepository courseJpaRepository,
    CareerOfferingJpaRepository careerOfferingJpaRepository,
    EnrollmentJpaRepository enrollmentJpaRepository, TermJpaRepository termJpaRepository,
    TermJpaMapper termJpaMapper) {
    this.studentJpaRepository = studentJpaRepository;
    this.courseJpaRepository = courseJpaRepository;
    this.careerOfferingJpaRepository = careerOfferingJpaRepository;
    this.enrollmentJpaRepository = enrollmentJpaRepository;
    this.termJpaRepository = termJpaRepository;
    this.termJpaMapper = termJpaMapper;
  }

  @Override
  public long countStudents() {
    return studentJpaRepository.count();
  }

  @Override
  public long countActiveStudents() {
    return studentJpaRepository.countByActiveTrue();
  }

  @Override
  public long countCourses() {
    return courseJpaRepository.count();
  }

  @Override
  public long countActiveCareerOfferings() {
    return careerOfferingJpaRepository.countByActiveTrue();
  }

  @Override
  public long countEnrollments() {
    return enrollmentJpaRepository.count();
  }

  @Override
  public List<LabelCountResponse> countEnrollmentsByStatus() {
    return toLabelCounts(enrollmentJpaRepository.countGroupByStatus());
  }

  @Override
  public List<LabelCountResponse> countEnrollmentsByTerm() {
    return toLabelCounts(enrollmentJpaRepository.countGroupByTerm());
  }

  @Override
  public List<LabelCountResponse> findTopCareersByEnrollments(int limit) {
    return toLabelCounts(enrollmentJpaRepository.countGroupByCareer(PageRequest.of(0, limit)));
  }

  @Override
  public Optional<Term> findCurrentTerm(LocalDate today) {
    return termJpaRepository.findCurrentTerms(today).stream().findFirst()
      .map(termJpaMapper::toDomainEntity);
  }

  @Override
  public long countEnrollmentsByTermId(Integer termId) {
    return enrollmentJpaRepository.countByTermId(termId);
  }

  private List<LabelCountResponse> toLabelCounts(List<Object[]> rows) {
    return rows.stream()
      .map(row -> new LabelCountResponse(String.valueOf(row[0]), (Long) row[1]))
      .toList();
  }
}
