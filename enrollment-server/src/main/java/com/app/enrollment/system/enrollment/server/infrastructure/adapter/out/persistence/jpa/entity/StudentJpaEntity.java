package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alonso
 */
@Entity
@Table(name = "student")
public class StudentJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "student_id")
  private Integer studentId;

  @Column(name = "document_number", unique = true, nullable = false, length = 20)
  private String documentNumber;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;

  @Column(length = 200)
  private String address;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private Boolean active;

  @OneToMany(mappedBy = "student")
  private List<EnrollmentJpaEntity> enrollments = new ArrayList<>();

  public StudentJpaEntity() {
  }
  
  public Integer getStudentId() {
    return studentId;
  }
  
  public void setStudentId(Integer studentId) {
    this.studentId = studentId;
  }
  
  public String getDocumentNumber() {
    return documentNumber;
  }
  
  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getPhoneNumber() {
    return phoneNumber;
  }
  
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }
  
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
  
  public Boolean getActive() {
    return active;
  }
  
  public void setActive(Boolean active) {
    this.active = active;
  }
  
  public List<EnrollmentJpaEntity> getEnrollments() {
    return enrollments;
  }
  
  public void setEnrollments(
    List<EnrollmentJpaEntity> enrollments) {
    this.enrollments = enrollments;
  }
}
