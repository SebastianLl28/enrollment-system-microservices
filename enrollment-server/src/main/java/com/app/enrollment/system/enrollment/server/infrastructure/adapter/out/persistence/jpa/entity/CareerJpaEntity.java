package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Alonso
 */

@Entity
@Table(name = "career", uniqueConstraints = {
    @UniqueConstraint(name = "uk_career_name", columnNames = "name")
})
public class CareerJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "career_id")
  private Integer careerId;

  @Column(name = "faculty_id", nullable = false)
  private Integer facultyId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "faculty_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_faculty"), insertable = false, updatable = false)
  private FacultyJpaEntity faculty;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "semester_length", nullable = false)
  private Integer semesterLength;

  @Column(name = "degree_awarded", length = 100)
  private String degreeAwarded;

  @CreationTimestamp
  @Column(name = "registration_date", nullable = false, updatable = false)
  private LocalDateTime registrationDate;

  @Column(nullable = false)
  private Boolean active;

  @OneToMany(mappedBy = "career")
  private List<CourseJpaEntity> courses = new ArrayList<>();

  public CareerJpaEntity() {
  }
  
  public Integer getCareerId() {
    return careerId;
  }
  
  public void setCareerId(Integer careerId) {
    this.careerId = careerId;
  }
  
  public Integer getFacultyId() {
    return facultyId;
  }
  
  public void setFacultyId(Integer facultyId) {
    this.facultyId = facultyId;
  }
  
  public FacultyJpaEntity getFaculty() {
    return faculty;
  }
  
  public void setFaculty(
    FacultyJpaEntity faculty) {
    this.faculty = faculty;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Integer getSemesterLength() {
    return semesterLength;
  }
  
  public void setSemesterLength(Integer semesterLength) {
    this.semesterLength = semesterLength;
  }
  
  public String getDegreeAwarded() {
    return degreeAwarded;
  }
  
  public void setDegreeAwarded(String degreeAwarded) {
    this.degreeAwarded = degreeAwarded;
  }
  
  public LocalDateTime getRegistrationDate() {
    return registrationDate;
  }
  
  public void setRegistrationDate(LocalDateTime registrationDate) {
    this.registrationDate = registrationDate;
  }
  
  public Boolean getActive() {
    return active;
  }
  
  public void setActive(Boolean active) {
    this.active = active;
  }
  
  public List<CourseJpaEntity> getCourses() {
    return courses;
  }
  
  public void setCourses(
    List<CourseJpaEntity> courses) {
    this.courses = courses;
  }
}
