package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Alonso
 */
@Entity
@Table(name = "enrollment", indexes = {
  @Index(name = "idx_enroll_student", columnList = "student_id"),
  @Index(name = "idx_enroll_offering", columnList = "course_offering_id"),
  @Index(name = "idx_enroll_status", columnList = "status")
})
public class EnrollmentJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "enrollment_id")
  private Integer enrollmentId;

  @Column(name = "student_id", nullable = false)
  private Integer studentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", nullable = false, insertable = false, updatable = false,
      foreignKey = @ForeignKey(name = "fk_student"))
  private StudentJpaEntity student;

  @Column(name = "enrollment_date", nullable = false)
  private LocalDateTime enrollmentDate;
  
  @Column(name = "unenrollment_date")
  private LocalDateTime unenrollmentDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private EnrollmentStatus status;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
  
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
  
  @Column(name = "user_id", nullable = false)
  private Integer userId;
  
  @Column(name = "course_offering_id", nullable = false)
  private Integer courseOfferingId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "course_offering_id",
    nullable = false,
    insertable = false,
    updatable = false,
    foreignKey = @ForeignKey(name = "fk_enrollment_offering")
  )
  private CourseOfferingJpaEntity courseOffering;
  
  public EnrollmentJpaEntity() {
  }
  
  public EnrollmentJpaEntity(Integer enrollmentId, Integer studentId, Integer courseOfferingId,
    LocalDateTime enrollmentDate, LocalDateTime unenrollmentDate, EnrollmentStatus status, Integer userId) {
    this.enrollmentId = enrollmentId;
    this.studentId = studentId;
    this.enrollmentDate = enrollmentDate;
    this.unenrollmentDate = unenrollmentDate;
    this.status = status;
    this.userId = userId;
    this.courseOfferingId = courseOfferingId;
  }
  
  public Integer getEnrollmentId() {
    return enrollmentId;
  }
  
  public void setEnrollmentId(Integer enrollmentId) {
    this.enrollmentId = enrollmentId;
  }
  
  public Integer getStudentId() {
    return studentId;
  }
  
  public void setStudentId(Integer studentId) {
    this.studentId = studentId;
  }
  
  public StudentJpaEntity getStudent() {
    return student;
  }
  
  public void setStudent(
    StudentJpaEntity student) {
    this.student = student;
  }
  
  public LocalDateTime getEnrollmentDate() {
    return enrollmentDate;
  }
  
  public void setEnrollmentDate(LocalDateTime enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }
  
  public LocalDateTime getUnenrollmentDate() {
    return unenrollmentDate;
  }
  
  public void setUnenrollmentDate(LocalDateTime unenrollmentDate) {
    this.unenrollmentDate = unenrollmentDate;
  }
  
  public EnrollmentStatus getStatus() {
    return status;
  }
  
  public void setStatus(
    EnrollmentStatus status) {
    this.status = status;
  }
  
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
  
  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
  
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
  
  public Integer getUserId() {
    return userId;
  }
  
  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  
  public Integer getCourseOfferingId() {
    return courseOfferingId;
  }
  
  public void setCourseOfferingId(Integer courseOfferingId) {
    this.courseOfferingId = courseOfferingId;
  }
}
