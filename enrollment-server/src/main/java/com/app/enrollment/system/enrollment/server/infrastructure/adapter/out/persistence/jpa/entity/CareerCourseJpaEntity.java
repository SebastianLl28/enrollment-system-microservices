package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Malla curricular: relación muchos-a-muchos entre carrera y curso con el ciclo
 * (semester_level) en que el curso se dicta en esa carrera.
 *
 * @author Alonso
 */
@Entity
@Table(name = "career_course", indexes = {
  @Index(name = "idx_career_course_career", columnList = "career_id"),
  @Index(name = "idx_career_course_course", columnList = "course_id")
},
  uniqueConstraints = {
    @UniqueConstraint(name = "uq_career_course", columnNames = {"career_id", "course_id"})
  })
public class CareerCourseJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "career_id", nullable = false)
  private Integer careerId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "career_id", insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "fk_career_course_career"))
  private CareerJpaEntity career;

  @Column(name = "course_id", nullable = false)
  private Integer courseId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", insertable = false, updatable = false,
    foreignKey = @ForeignKey(name = "fk_career_course_course"))
  private CourseJpaEntity course;

  @Column(name = "semester_level", nullable = false)
  private Integer semesterLevel;

  public CareerCourseJpaEntity() {
  }

  public CareerCourseJpaEntity(Integer id, Integer careerId, Integer courseId,
    Integer semesterLevel) {
    this.id = id;
    this.careerId = careerId;
    this.courseId = courseId;
    this.semesterLevel = semesterLevel;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public void setCareer(CareerJpaEntity career) {
    this.career = career;
  }

  public Integer getCourseId() {
    return courseId;
  }

  public void setCourseId(Integer courseId) {
    this.courseId = courseId;
  }

  public CourseJpaEntity getCourse() {
    return course;
  }

  public void setCourse(CourseJpaEntity course) {
    this.course = course;
  }

  public Integer getSemesterLevel() {
    return semesterLevel;
  }

  public void setSemesterLevel(Integer semesterLevel) {
    this.semesterLevel = semesterLevel;
  }
}
