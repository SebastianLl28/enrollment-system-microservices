package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.ClassroomJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class ClassroomJpaMapper {

  public Classroom toDomainEntity(ClassroomJpaEntity classroomJpaEntity) {
    ClassroomID classroomID =
      classroomJpaEntity.getId() != null ? new ClassroomID(classroomJpaEntity.getId()) : null;

    return Classroom.rehydrate(classroomID, classroomJpaEntity.getCode(),
      classroomJpaEntity.getName(), classroomJpaEntity.getCapacity(),
      classroomJpaEntity.isVirtual(), classroomJpaEntity.isActive(),
      classroomJpaEntity.getCreatedAt());
  }

  public ClassroomJpaEntity toJpaEntity(Classroom classroom) {
    Integer id = classroom.getId() != null ? classroom.getId().getValue() : null;

    return new ClassroomJpaEntity(id, classroom.getCode(), classroom.getName(),
      classroom.getCapacity(), classroom.isVirtual(), classroom.isActive(),
      classroom.getCreatedAt());
  }

}
