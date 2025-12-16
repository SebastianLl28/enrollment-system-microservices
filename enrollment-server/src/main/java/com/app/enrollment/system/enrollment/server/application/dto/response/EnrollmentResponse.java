package com.app.enrollment.system.enrollment.server.application.dto.response;

import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import java.time.Instant;

/**
 * @author Alonso
 */
public class EnrollmentResponse {
  
  private Integer id;
  private StudentSummaryResponse student;
  private CourseOfferingResponse courseOffering;
  private Instant enrollmentDate;
  private Instant unenrollmentDate;
  private EnrollmentStatus status;
  private String createBy;
  
  public EnrollmentResponse(Integer id, StudentSummaryResponse student,
    CourseOfferingResponse courseOfferingResponse,
    Instant enrollmentDate, Instant unenrollmentDate, EnrollmentStatus status) {
    this.id = id;
    this.student = student;
    this.enrollmentDate = enrollmentDate;
    this.unenrollmentDate = unenrollmentDate;
    this.status = status;
    this.courseOffering = courseOfferingResponse;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public StudentSummaryResponse getStudent() {
    return student;
  }
  
  public void setStudent(
    StudentSummaryResponse student) {
    this.student = student;
  }
  
  public CourseOfferingResponse getCourseOffering() {
    return courseOffering;
  }
  
  public void setCourseOffering(
    CourseOfferingResponse courseOffering) {
    this.courseOffering = courseOffering;
  }
  
  public Instant getEnrollmentDate() {
    return enrollmentDate;
  }
  
  public void setEnrollmentDate(Instant enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }
  
  public Instant getUnenrollmentDate() {
    return unenrollmentDate;
  }
  
  public void setUnenrollmentDate(Instant unenrollmentDate) {
    this.unenrollmentDate = unenrollmentDate;
  }
  
  public EnrollmentStatus getStatus() {
    return status;
  }
  
  public void setStatus(
    EnrollmentStatus status) {
    this.status = status;
  }
  
  public String getCreateBy() {
    return createBy;
  }
  
  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }
  
  
}
