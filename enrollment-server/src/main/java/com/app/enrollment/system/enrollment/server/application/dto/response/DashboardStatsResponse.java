package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.util.List;

/**
 * Estadísticas agregadas para el dashboard.
 *
 * @author Alonso
 */
public record DashboardStatsResponse(
  long totalStudents,
  long activeStudents,
  long totalCourses,
  long activeCourseOfferings,
  long totalEnrollments,
  List<LabelCountResponse> enrollmentsByStatus,
  TermResponse currentTerm,
  long currentTermEnrollments,
  List<LabelCountResponse> enrollmentsByTerm,
  List<LabelCountResponse> topCourses) {

}
