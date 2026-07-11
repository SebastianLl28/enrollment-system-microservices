package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SectionID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import java.time.Instant;

/**
 * Sección de un curso en un periodo, dictada en un aula. La capacidad la
 * define el aula (las virtuales no tienen límite).
 *
 * @author Alonso
 */
public class Section {

  private final SectionID id;
  private final CourseID courseId;
  private final TermID termId;
  private final ClassroomID classroomId;
  private final String sectionCode;
  private final Boolean active;
  private final Instant createdAt;

  private Section(SectionID id, CourseID courseId, TermID termId, ClassroomID classroomId,
    String sectionCode, Boolean active, Instant createdAt) {
    this.id = id;
    this.courseId = courseId;
    this.termId = termId;
    this.classroomId = classroomId;
    this.sectionCode = sectionCode;
    this.active = active;
    this.createdAt = createdAt;
  }

  public static Section create(CourseID courseId, TermID termId, ClassroomID classroomId,
    String sectionCode, Instant now) {
    return new Section(null, courseId, termId, classroomId, sectionCode, true, now);
  }

  public static Section rehydrate(SectionID id, CourseID courseId, TermID termId,
    ClassroomID classroomId, String sectionCode, Boolean active, Instant createdAt) {
    return new Section(id, courseId, termId, classroomId, sectionCode, active, createdAt);
  }

  public SectionID getId() {
    return id;
  }

  public CourseID getCourseId() {
    return courseId;
  }

  public TermID getTermId() {
    return termId;
  }

  public ClassroomID getClassroomId() {
    return classroomId;
  }

  public String getSectionCode() {
    return sectionCode;
  }

  public Boolean isActive() {
    return active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
