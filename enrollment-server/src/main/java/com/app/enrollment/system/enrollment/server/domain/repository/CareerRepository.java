package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface CareerRepository {
  Optional<Career> findById(CareerID careerID);
  Career save(Career career);
  List<Career> findAll();
}
