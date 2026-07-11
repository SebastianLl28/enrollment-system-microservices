package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.exception.CannotChangeStatusOfCancelledEnrollmentException;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
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
  private final CareerOfferingID careerOfferingID;
  private String paymentId;
  private String paymentStatus;
  private Instant paidAt;

  private Enrollment(EnrollmentID id, StudentID studentID, CareerOfferingID careerOfferingID, Instant enrollmentDate, Instant unenrollmentDate, EnrollmentStatus enrollmentStatus, UserID userID) {
    this.id = id;
    this.studentID = studentID;
    this.enrollmentDate = enrollmentDate;
    this.unenrollmentDate = unenrollmentDate;
    this.status = enrollmentStatus;
    this.userID = userID;
    this.careerOfferingID = careerOfferingID;
  }

  public static Enrollment create(StudentID studentID, CareerOfferingID careerOfferingID,
    Instant now, UserID userID) {
    return new
      Enrollment(null, studentID, careerOfferingID, now, null, EnrollmentStatus.PENDING, userID);
  }

  public static Enrollment rehydrate(EnrollmentID id, StudentID studentID, CareerOfferingID careerOfferingID,
    Instant enrollmentDate, Instant unenrollmentDate, EnrollmentStatus enrollmentStatus, UserID userID,
    String paymentId, String paymentStatus, Instant paidAt) {
    Enrollment enrollment = new Enrollment(id, studentID, careerOfferingID, enrollmentDate, unenrollmentDate, enrollmentStatus, userID);
    enrollment.paymentId = paymentId;
    enrollment.paymentStatus = paymentStatus;
    enrollment.paidAt = paidAt;
    return enrollment;
  }

  /**
   * Marca la inscripción como pagada. Idempotente: si ya está PAID no vuelve a
   * aplicar el pago y devuelve false.
   */
  public boolean markAsPaid(String paymentId, String paymentStatus, Instant paidAt) {
    if (this.status == EnrollmentStatus.PAID) {
      return false;
    }
    if (this.status == EnrollmentStatus.CANCELLED) {
      throw new CannotChangeStatusOfCancelledEnrollmentException(
        "Cannot mark a cancelled enrollment as paid.");
    }
    this.status = EnrollmentStatus.PAID;
    this.paymentId = paymentId;
    this.paymentStatus = paymentStatus;
    this.paidAt = paidAt;
    return true;
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

  public CareerOfferingID getCareerOfferingID() {
    return careerOfferingID;
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

  public String getPaymentId() {
    return paymentId;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public Instant getPaidAt() {
    return paidAt;
  }
}
