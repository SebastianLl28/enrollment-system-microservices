package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface EnrollmentRepository {

  Enrollment save(Enrollment enrollment);

  Optional<Enrollment> findByStudentIDAndCareerOfferingIDAndStatusIn(StudentID studentID, CareerOfferingID careerOfferingID, Collection<EnrollmentStatus> statuses);

  PageResult<Enrollment> findAllByStudentIDAndTermIDAndCareerID(StudentID studentID, TermID termID, CareerID careerID, int page, int size);

  Optional<Enrollment> findByEnrollmentID(EnrollmentID enrollmentID);

}
