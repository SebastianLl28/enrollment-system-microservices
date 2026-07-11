package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper;

import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CareerOfferingJpaEntity;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CareerOfferingJpaMapper {

  public CareerOffering toDomainEntity(CareerOfferingJpaEntity careerOfferingJpaEntity) {
    CareerOfferingID careerOfferingID =
      careerOfferingJpaEntity.getId() != null ? new CareerOfferingID(
        careerOfferingJpaEntity.getId()) : null;

    CareerID careerID = careerOfferingJpaEntity.getCareerId() != null ? new CareerID(
      careerOfferingJpaEntity.getCareerId()) : null;

    TermID termID =
      careerOfferingJpaEntity.getTermId() != null ? new TermID(careerOfferingJpaEntity.getTermId())
        : null;

    return CareerOffering.rehydrate(careerOfferingID, careerID, termID,
      careerOfferingJpaEntity.getCapacity(), careerOfferingJpaEntity.getEnrolledCount(),
      careerOfferingJpaEntity.isActive(), careerOfferingJpaEntity.getCreatedAt(),
      careerOfferingJpaEntity.getPrice());
  }

  public CareerOfferingJpaEntity toJpaEntity(CareerOffering careerOffering) {
    Integer id = careerOffering.getId() != null ? careerOffering.getId().getValue() : null;

    Integer careerId =
      careerOffering.getCareerId() != null ? careerOffering.getCareerId().getValue() : null;

    Integer termId =
      careerOffering.getTermId() != null ? careerOffering.getTermId().getValue() : null;

    return new CareerOfferingJpaEntity(id, careerId, termId, careerOffering.getCapacity(),
      careerOffering.getEnrolledCount(), careerOffering.isActive(), careerOffering.getCreatedAt(),
      careerOffering.getPrice());

  }

}
