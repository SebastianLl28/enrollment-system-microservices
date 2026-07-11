package com.app.enrollment.system.enrollment.server.application.port.out;

import com.app.enrollment.system.enrollment.server.application.dto.response.LabelCountResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de lectura para los agregados del dashboard. Es un read-model: consulta
 * directamente la persistencia sin pasar por los repositorios de dominio.
 *
 * @author Alonso
 */
public interface DashboardStatsPort {

  long countStudents();

  long countActiveStudents();

  long countCourses();

  long countActiveCareerOfferings();

  long countEnrollments();

  List<LabelCountResponse> countEnrollmentsByStatus();

  List<LabelCountResponse> countEnrollmentsByTerm();

  List<LabelCountResponse> findTopCareersByEnrollments(int limit);

  Optional<Term> findCurrentTerm(LocalDate today);

  long countEnrollmentsByTermId(Integer termId);

}
