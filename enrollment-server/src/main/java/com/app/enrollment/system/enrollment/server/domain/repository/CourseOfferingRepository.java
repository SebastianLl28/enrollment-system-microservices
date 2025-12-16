package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface CourseOfferingRepository {
  
  CourseOffering save(CourseOffering courseOffering);
  
  Optional<CourseOffering> findById(CourseOfferingID courseOfferingID);
  
  List<CourseOffering> findAll();
  
}
