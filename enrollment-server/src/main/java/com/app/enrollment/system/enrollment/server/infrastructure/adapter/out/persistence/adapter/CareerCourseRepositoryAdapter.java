package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.CareerCourse;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerCourseRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.CareerCourseJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.CareerCourseJpaMapper;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */
@Adapter
public class CareerCourseRepositoryAdapter implements CareerCourseRepository {

  private final CareerCourseJpaRepository careerCourseJpaRepository;
  private final CareerCourseJpaMapper careerCourseJpaMapper;

  public CareerCourseRepositoryAdapter(CareerCourseJpaRepository careerCourseJpaRepository,
    CareerCourseJpaMapper careerCourseJpaMapper) {
    this.careerCourseJpaRepository = careerCourseJpaRepository;
    this.careerCourseJpaMapper = careerCourseJpaMapper;
  }

  @Override
  public List<CareerCourse> findByCourseId(CourseID courseID) {
    return careerCourseJpaRepository.findByCourseId(courseID.getValue()).stream()
      .map(careerCourseJpaMapper::toDomainEntity)
      .toList();
  }

  @Override
  public List<CareerCourse> findByCareerId(CareerID careerID) {
    return careerCourseJpaRepository.findByCareerId(careerID.getValue()).stream()
      .map(careerCourseJpaMapper::toDomainEntity)
      .toList();
  }

  @Override
  @Transactional
  public void replaceForCourse(CourseID courseID, List<CareerCourse> careerCourses) {
    careerCourseJpaRepository.deleteByCourseId(courseID.getValue());
    careerCourseJpaRepository.saveAll(careerCourses.stream()
      .map(careerCourseJpaMapper::toJpaEntity)
      .toList());
  }
}
