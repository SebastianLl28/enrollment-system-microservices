package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.response.DashboardStatsResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.GetDashboardStatsUseCase;
import com.app.enrollment.system.enrollment.server.application.port.out.DashboardStatsPort;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */
@UseCase
public class DashboardApplicationService implements GetDashboardStatsUseCase {

  private static final int TOP_COURSES_LIMIT = 5;

  private final DashboardStatsPort dashboardStatsPort;
  private final TermMapper termMapper;
  private final Clock clock;

  public DashboardApplicationService(DashboardStatsPort dashboardStatsPort, TermMapper termMapper,
    Clock clock) {
    this.dashboardStatsPort = dashboardStatsPort;
    this.termMapper = termMapper;
    this.clock = clock;
  }

  @Override
  @Transactional(readOnly = true)
  public DashboardStatsResponse getDashboardStats() {
    LocalDate today = LocalDate.now(clock);

    Optional<Term> currentTerm = dashboardStatsPort.findCurrentTerm(today);
    TermResponse currentTermResponse = currentTerm.map(termMapper::toTermResponse).orElse(null);
    long currentTermEnrollments = currentTerm
      .map(term -> dashboardStatsPort.countEnrollmentsByTermId(term.getId().getValue()))
      .orElse(0L);

    return new DashboardStatsResponse(
      dashboardStatsPort.countStudents(),
      dashboardStatsPort.countActiveStudents(),
      dashboardStatsPort.countCourses(),
      dashboardStatsPort.countActiveCourseOfferings(),
      dashboardStatsPort.countEnrollments(),
      dashboardStatsPort.countEnrollmentsByStatus(),
      currentTermResponse,
      currentTermEnrollments,
      dashboardStatsPort.countEnrollmentsByTerm(),
      dashboardStatsPort.findTopCoursesByEnrollments(TOP_COURSES_LIMIT)
    );
  }
}
