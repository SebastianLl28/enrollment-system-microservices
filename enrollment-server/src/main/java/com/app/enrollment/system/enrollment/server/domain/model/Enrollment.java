package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.exception.CannotChangeStatusOfCancelledEnrollmentException;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import java.time.Instant;

/**
 * @author Alonso
 */
public class Enrollment {
  
  private final EnrollmentID id;
  private final StudentID studentID;
  private final Instant enrollmentDate;
  private Instant unenrollmentDate;
  private EnrollmentStatus status;
  private final UserID userID;
  private final CourseOfferingID courseOfferingID;
  
  private Enrollment(EnrollmentID id, StudentID studentID, CourseOfferingID courseOfferingID, Instant enrollmentDate, Instant unenrollmentDate, EnrollmentStatus enrollmentStatus, UserID userID) {
    this.id = id;
    this.studentID = studentID;
    this.enrollmentDate = enrollmentDate;
    this.unenrollmentDate = unenrollmentDate;
    this.status = enrollmentStatus;
    this.userID = userID;
    this.courseOfferingID = courseOfferingID;
  }
  
  public static Enrollment create(StudentID studentID, CourseOfferingID courseOfferingID,
    Instant now, UserID userID) {
    return new
      Enrollment(null, studentID, courseOfferingID, now, null, EnrollmentStatus.PENDING, userID);
  }
  
  public static Enrollment rehydrate(EnrollmentID id, StudentID studentID, CourseOfferingID courseOfferingID,
    Instant enrollmentDate, Instant unenrollmentDate, EnrollmentStatus enrollmentStatus, UserID userID) {
    return new Enrollment(id, studentID, courseOfferingID, enrollmentDate, unenrollmentDate, enrollmentStatus, userID);
  }
  
  public void unenroll(Instant now) {
    if (this.status == EnrollmentStatus.CANCELLED) return;
    this.status = EnrollmentStatus.CANCELLED;
    this.unenrollmentDate = now;
  }
  
  public void updateStatus(EnrollmentStatus newStatus) {
    if (this.status == EnrollmentStatus.CANCELLED) {
      throw new CannotChangeStatusOfCancelledEnrollmentException("Cannot change status of a cancelled enrollment.");
    }
    this.status = newStatus;
  }
  
  public EnrollmentID getID() {
    return id;
  }
  
  public StudentID getStudentID() {
    return studentID;
  }

  public CourseOfferingID getCourseOfferingID() {
    return courseOfferingID;
  }
  
  public Instant getEnrollmentDate() {
    return enrollmentDate;
  }
  
  public Instant getUnenrollmentDate() {
    return unenrollmentDate;
  }
  
  public EnrollmentStatus getStatus() {
    return status;
  }
  
  public EnrollmentID getId() {
    return id;
  }
  
  public UserID getUserID() {
    return userID;
  }
}
