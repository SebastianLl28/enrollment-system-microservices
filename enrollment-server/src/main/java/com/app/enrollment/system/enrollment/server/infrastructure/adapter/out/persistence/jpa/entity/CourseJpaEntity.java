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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alonso
 */
@Entity
@Table(name = "course")
public class CourseJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "course_id")
  private Integer courseId;

  @Column(name = "career_id", nullable = false)
  private Integer careerId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "career_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_career"), insertable = false, updatable = false)
  private CareerJpaEntity career;
  
  @Column(unique = true, nullable = false, length = 20)
  private String code;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private Integer credits;

  @Column(name = "semester_level", nullable = false)
  private Integer semesterLevel;

  @Column(name = "registration_date")
  private LocalDateTime registrationDate;

  @Column(nullable = false)
  private Boolean active;

  public CourseJpaEntity() {
  }
  
  public Integer getCourseId() {
    return courseId;
  }
  
  public void setCourseId(Integer courseId) {
    this.courseId = courseId;
  }
  
  public Integer getCareerId() {
    return careerId;
  }
  
  public void setCareerId(Integer careerId) {
    this.careerId = careerId;
  }
  
  public CareerJpaEntity getCareer() {
    return career;
  }
  
  public void setCareer(
    CareerJpaEntity career) {
    this.career = career;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
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
  
  public Integer getCredits() {
    return credits;
  }
  
  public void setCredits(Integer credits) {
    this.credits = credits;
  }
  
  public Integer getSemesterLevel() {
    return semesterLevel;
  }
  
  public void setSemesterLevel(Integer semesterLevel) {
    this.semesterLevel = semesterLevel;
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
  
//  public List<SectionJpaEntity> getSections() {
//    return sections;
//  }
//
//  public void setSections(
//    List<SectionJpaEntity> sections) {
//    this.sections = sections;
//  }

//  public List<PrerequisiteJpaEntity> getPrerequisites() {
//    return prerequisites;
//  }
//
//  public void setPrerequisites(
//    List<PrerequisiteJpaEntity> prerequisites) {
//    this.prerequisites = prerequisites;
//  }
}
