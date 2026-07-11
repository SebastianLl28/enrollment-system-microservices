package com.app.common.events;

import com.app.common.enums.EnrollmentStatus;
import java.time.Instant;

/**
 * @author Alonso
 */
public class EnrollmentAssignedEvent {
  private String enrollmentId;
  private Instant occurredAt;
  private String studentFullName;
  private String studentEmail;
  private EnrollmentStatus enrollmentStatus;
  private Integer userId;
  private String careerName;
  private String termCode;
  private String paymentUrl;


  public EnrollmentAssignedEvent() {}

  public EnrollmentAssignedEvent(String enrollmentId, Instant occurredAt, String studentFullName,
    String studentEmail, String enrollmentStatus, Integer userId, String careerName, String termCode) {
    this.enrollmentId = enrollmentId;
    this.occurredAt = occurredAt;
    this.studentFullName = studentFullName;
    this.studentEmail = studentEmail;
    this.enrollmentStatus = EnrollmentStatus.valueOf(enrollmentStatus);
    this.userId = userId;
    this.careerName = careerName;
    this.termCode = termCode;
  }

  public String getEnrollmentId() {
    return enrollmentId;
  }

  public void setEnrollmentId(String enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  public Instant getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(Instant occurredAt) {
    this.occurredAt = occurredAt;
  }

  public String getStudentFullName() {
    return studentFullName;
  }

  public void setStudentFullName(String studentFullName) {
    this.studentFullName = studentFullName;
  }

  public String getStudentEmail() {
    return studentEmail;
  }

  public void setStudentEmail(String studentEmail) {
    this.studentEmail = studentEmail;
  }

  public EnrollmentStatus getEnrollmentStatus() {
    return enrollmentStatus;
  }

  public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
    this.enrollmentStatus = enrollmentStatus;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getCareerName() {
    return careerName;
  }

  public void setCareerName(String careerName) {
    this.careerName = careerName;
  }

  public String getTermCode() {
    return termCode;
  }

  public void setTermCode(String termCode) {
    this.termCode = termCode;
  }

  public String getPaymentUrl() {
    return paymentUrl;
  }

  public void setPaymentUrl(String paymentUrl) {
    this.paymentUrl = paymentUrl;
  }

  @Override
  public String toString() {
    return "EnrollmentAssignedEvent{" +
      "enrollmentId='" + enrollmentId + '\'' +
      ", occurredAt=" + occurredAt +
      ", studentFullName='" + studentFullName + '\'' +
      ", studentEmail='" + studentEmail + '\'' +
      ", enrollmentStatus=" + enrollmentStatus +
      ", userId=" + userId +
      ", careerName='" + careerName + '\'' +
      ", termCode='" + termCode + '\'' +
      ", paymentUrl='" + paymentUrl + '\'' +
      '}';
  }
}
