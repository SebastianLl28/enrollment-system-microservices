package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Section;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SectionID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.SectionJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class SectionJpaMapper {

  public Section toDomainEntity(SectionJpaEntity sectionJpaEntity) {
    SectionID sectionID =
      sectionJpaEntity.getId() != null ? new SectionID(sectionJpaEntity.getId()) : null;

    CourseID courseID =
      sectionJpaEntity.getCourseId() != null ? new CourseID(sectionJpaEntity.getCourseId()) : null;

    TermID termID =
      sectionJpaEntity.getTermId() != null ? new TermID(sectionJpaEntity.getTermId()) : null;

    ClassroomID classroomID = sectionJpaEntity.getClassroomId() != null ? new ClassroomID(
      sectionJpaEntity.getClassroomId()) : null;

    return Section.rehydrate(sectionID, courseID, termID, classroomID,
      sectionJpaEntity.getSectionCode(), sectionJpaEntity.isActive(),
      sectionJpaEntity.getCreatedAt());
  }

  public SectionJpaEntity toJpaEntity(Section section) {
    Integer id = section.getId() != null ? section.getId().getValue() : null;

    Integer courseId = section.getCourseId() != null ? section.getCourseId().getValue() : null;

    Integer termId = section.getTermId() != null ? section.getTermId().getValue() : null;

    Integer classroomId =
      section.getClassroomId() != null ? section.getClassroomId().getValue() : null;

    return new SectionJpaEntity(id, courseId, termId, classroomId, section.getSectionCode(),
      section.isActive(), section.getCreatedAt());
  }

}
