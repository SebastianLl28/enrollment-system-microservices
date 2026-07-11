package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerOfferingID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface CareerOfferingRepository {

  CareerOffering save(CareerOffering careerOffering);

  Optional<CareerOffering> findById(CareerOfferingID careerOfferingID);

  List<CareerOffering> findAll();

}
