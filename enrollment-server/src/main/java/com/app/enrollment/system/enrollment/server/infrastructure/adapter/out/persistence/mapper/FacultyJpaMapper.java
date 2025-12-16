package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.FacultyJpaEntity;
import java.time.Clock;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class FacultyJpaMapper {
  
  private final Clock clock;
  
  public FacultyJpaMapper(Clock clock) {
    this.clock = clock;
  }

  public Faculty toDomainFaculty(FacultyJpaEntity facultyJpaEntity) {
    FacultyID facultyID = new FacultyID(facultyJpaEntity.getFacultyId());
    Instant registrationDate = facultyJpaEntity.getRegistrationDate().atZone(clock.getZone()).toInstant();
    return Faculty.rehydrate(facultyID, facultyJpaEntity.getName(), facultyJpaEntity.getDescription(), facultyJpaEntity.getLocation(), facultyJpaEntity.getDean(), registrationDate, facultyJpaEntity.getActive());
  }

  public FacultyJpaEntity toJpaEntity(Faculty faculty) {
    FacultyJpaEntity facultyJpaEntity = new FacultyJpaEntity();

    if (faculty.getId() != null) {
      facultyJpaEntity.setFacultyId(faculty.getId().getValue());
    }

    facultyJpaEntity.setName(faculty.getName());
    facultyJpaEntity.setDescription(faculty.getDescription());
    facultyJpaEntity.setLocation(faculty.getLocation());
    facultyJpaEntity.setDean(faculty.getDean());
    facultyJpaEntity.setRegistrationDate(
        Instant.ofEpochMilli(faculty.getRegistrationDate().toEpochMilli())
            .atZone(clock.getZone())
            .toLocalDateTime()
    );
    facultyJpaEntity.setActive(faculty.isActive());
    return facultyJpaEntity;
  }
}
