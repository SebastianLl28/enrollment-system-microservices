package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CourseOfferingJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CourseOfferingJpaMapper {
  
  public CourseOffering toDomainEntity(CourseOfferingJpaEntity courseOfferingJpaEntity) {
    CourseOfferingID courseOfferingID =
      courseOfferingJpaEntity.getId() != null ? new CourseOfferingID(
        courseOfferingJpaEntity.getId()) : null;
    
    CourseID courseID = courseOfferingJpaEntity.getCourseId() != null ? new CourseID(
      courseOfferingJpaEntity.getCourseId()) : null;
    
    TermID termID =
      courseOfferingJpaEntity.getTermId() != null ? new TermID(courseOfferingJpaEntity.getTermId())
        : null;
    
    return CourseOffering.rehydrate(courseOfferingID, courseID, termID,
      courseOfferingJpaEntity.getSectionCode(), courseOfferingJpaEntity.getCapacity(),
      courseOfferingJpaEntity.getEnrolledCount(), courseOfferingJpaEntity.isActive(),
      courseOfferingJpaEntity.getCreatedAt());
  }
  
  public CourseOfferingJpaEntity toJpaEntity(CourseOffering courseOffering) {
    Integer id = courseOffering.getId() != null ? courseOffering.getId().getValue() : null;
    
    Integer courseId =
      courseOffering.getCourseId() != null ? courseOffering.getCourseId().getValue() : null;
    
    Integer termId =
      courseOffering.getTermId() != null ? courseOffering.getTermId().getValue() : null;
    
    return new CourseOfferingJpaEntity(id, courseId, termId, courseOffering.getSectionCode(),
      courseOffering.getCapacity(), courseOffering.getEnrolledCount(), courseOffering.isActive(),
      courseOffering.getCreatedAt());
    
  }
  
}
