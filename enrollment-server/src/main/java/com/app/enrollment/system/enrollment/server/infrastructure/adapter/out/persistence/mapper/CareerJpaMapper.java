package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DegreeTitle;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerJpaEntity;
import java.time.Clock;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CareerJpaMapper {
  
  private final Clock clock;
  
  public CareerJpaMapper(Clock clock) {
    this.clock = clock;
  }

  public Career toDomainCareer(CareerJpaEntity careerJpaEntity) {
    CareerID careerID = new CareerID(careerJpaEntity.getCareerId());
    FacultyID facultyID = new FacultyID(careerJpaEntity.getFacultyId());
    DegreeTitle degreeTitle = new DegreeTitle(careerJpaEntity.getDegreeAwarded());
    Instant registrationDate = careerJpaEntity.getRegistrationDate().atZone(clock.getZone()).toInstant();

    return Career.rehydrate(careerID, facultyID, careerJpaEntity.getName(),
        careerJpaEntity.getDescription(), careerJpaEntity.getSemesterLength(),
        degreeTitle, registrationDate, careerJpaEntity.getActive());
  }

  public CareerJpaEntity toJpaEntity(Career career) {
    CareerJpaEntity careerJpaEntity = new CareerJpaEntity();

    if (career.getId() != null) {
      careerJpaEntity.setCareerId(career.getId().getValue());
    }

    careerJpaEntity.setFacultyId(career.getFacultyId().getValue());
    careerJpaEntity.setName(career.getName());
    careerJpaEntity.setDescription(career.getDescription());
    careerJpaEntity.setSemesterLength(career.getSemesterLength());
    careerJpaEntity.setDegreeAwarded(career.getDegreeAwarded().getValue());
    careerJpaEntity.setRegistrationDate(
        Instant.ofEpochMilli(career.getRegistrationDate().toEpochMilli())
            .atZone(clock.getZone())
            .toLocalDateTime()
    );
    careerJpaEntity.setActive(career.isActive());

    return careerJpaEntity;
  }
}
