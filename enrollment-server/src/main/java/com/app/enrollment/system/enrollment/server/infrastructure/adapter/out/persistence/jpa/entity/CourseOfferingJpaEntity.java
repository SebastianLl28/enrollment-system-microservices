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
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Alonso
 */
@Entity
@Table(name = "course_offering", indexes = {
  @Index(name = "idx_offering_course", columnList = "course_id"),
  @Index(name = "idx_offering_term", columnList = "term_id"),
  @Index(name = "idx_offering_active", columnList = "active")
},
  uniqueConstraints = {
    // Un offering por (course, term, sectionCode)
    @UniqueConstraint(name = "uq_offering_course_term_section",
      columnNames = {"course_id", "term_id", "section_code"})
  })
public class CourseOfferingJpaEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(name = "course_id", nullable = false)
  private Integer courseId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="course_id", insertable=false, updatable=false,
    foreignKey=@ForeignKey(name="fk_offering_course"))
  private CourseJpaEntity course;
  
  @Column(name = "term_id", nullable = false)
  private Integer termId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="term_id", insertable=false, updatable=false,
    foreignKey=@ForeignKey(name="fk_offering_term"))
  private TermJpaEntity term;
  
  @Column(name = "section_code", nullable = false, length = 10)
  private String sectionCode; // A, B
  
  @Column(nullable = false)
  private int capacity;
  
  @Column(name = "enrolled_count", nullable = false)
  private int enrolledCount;
  
  @Column(nullable = false)
  private boolean active;
  
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;
  
  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
  
  public CourseOfferingJpaEntity() {
  }
  
  public CourseOfferingJpaEntity(Integer id, Integer courseId, Integer termId, String sectionCode,
    Integer capacity, Integer enrolledCount, Boolean active,
    Instant createdAt) {
    this.id = id;
    this.courseId = courseId;
    this.termId = termId;
    this.sectionCode = sectionCode;
    this.capacity = capacity;
    this.enrolledCount = enrolledCount;
    this.active = active;
    this.createdAt = createdAt;
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
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
  
  public void setCourse(
    CourseJpaEntity course) {
    this.course = course;
  }
  
  public Integer getTermId() {
    return termId;
  }
  
  public void setTermId(Integer termId) {
    this.termId = termId;
  }
  
  public TermJpaEntity getTerm() {
    return term;
  }
  
  public void setTerm(
    TermJpaEntity term) {
    this.term = term;
  }
  
  public String getSectionCode() {
    return sectionCode;
  }
  
  public void setSectionCode(String sectionCode) {
    this.sectionCode = sectionCode;
  }
  
  public int getCapacity() {
    return capacity;
  }
  
  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
  
  public int getEnrolledCount() {
    return enrolledCount;
  }
  
  public void setEnrolledCount(int enrolledCount) {
    this.enrolledCount = enrolledCount;
  }
  
  public boolean isActive() {
    return active;
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
  
  public Instant getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
  
  public Instant getUpdatedAt() {
    return updatedAt;
  }
  
  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
