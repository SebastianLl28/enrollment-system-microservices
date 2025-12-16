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
  private String courseName;
  
  
  public EnrollmentAssignedEvent() {}
  
  public EnrollmentAssignedEvent(String enrollmentId, String studentId, String courseId,
    Instant occurredAt, String studentFullName, String studentEmail, String enrollmentStatus, Integer userId, String courseName) {
    this.enrollmentId = enrollmentId;
    this.occurredAt = occurredAt;
    this.studentFullName = studentFullName;
    this.studentEmail = studentEmail;
    this.enrollmentStatus = EnrollmentStatus.valueOf(enrollmentStatus);
    this.userId = userId;
    this.courseName = courseName;
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
  
  public String getCourseName() {
    return courseName;
  }
  
  public void setCourseName(String courseName) {
    this.courseName = courseName;
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
      ", courseName='" + courseName + '\'' +
      '}';
  }
}
