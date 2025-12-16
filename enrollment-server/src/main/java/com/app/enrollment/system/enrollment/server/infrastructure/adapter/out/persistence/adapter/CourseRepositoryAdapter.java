package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.CourseJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.CourseJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.CourseJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class CourseRepositoryAdapter implements CourseRepository {

  private final CourseJpaMapper courseJpaMapper;
  private final CourseJpaRepository courseJpaRepository;

  public CourseRepositoryAdapter(CourseJpaMapper courseJpaMapper, CourseJpaRepository courseJpaRepository) {
    this.courseJpaMapper = courseJpaMapper;
    this.courseJpaRepository = courseJpaRepository;
  }


  @Override
  public Optional<Course> findById(CourseID courseID) {
    return courseJpaRepository.findById(courseID.getValue()).map(courseJpaMapper::toDomainCourse);
  }

  @Override
  public Course save(Course course) {
    CourseJpaEntity courseJpaEntity = courseJpaMapper.toJpaEntity(course);
    courseJpaEntity = courseJpaRepository.save(courseJpaEntity);
    return courseJpaMapper.toDomainCourse(courseJpaEntity);
  }

//  @Override
//  public boolean existsByCodeAndCareerId(CourseCode code, Integer careerId) {
//    return false;
//  }

//  @Override
//  public void mustExist(CourseID id) {
//
//  }

//  @Override
//  public List<Course> findByCareerId(Integer careerId) {
//    return List.of();
//  }

//  @Override
//  public List<Course> findByCareerIdAndActive(Integer careerId, boolean active) {
//    return List.of();
//  }

//  @Override
//  public List<Course> findByCareerIdAndSemesterLevel(Integer careerId, int level) {
//    return List.of();
//  }

  @Override
  public List<Course> findAll() {
    return courseJpaRepository.findAll().stream().map(courseJpaMapper::toDomainCourse).toList();
  }

  @Override
  public List<Course> findByCareerIdWithActiveCourses(Integer careerId) {
    return courseJpaRepository.findAllByCareerIdAndActiveIsFalse(careerId).stream().map(courseJpaMapper::toDomainCourse).toList();
  }
}
