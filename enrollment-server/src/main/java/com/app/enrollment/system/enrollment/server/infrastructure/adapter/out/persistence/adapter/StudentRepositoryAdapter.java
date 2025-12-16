package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.adapter;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.StudentJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.StudentJpaRepository;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.mapper.StudentJpaMapper;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
@Adapter
public class StudentRepositoryAdapter implements StudentRepository {

  private final StudentJpaRepository studentJpaRepository;
  private final StudentJpaMapper studentJpaMapper;

  public StudentRepositoryAdapter(StudentJpaRepository studentJpaRepository, StudentJpaMapper studentJpaMapper) {
    this.studentJpaRepository = studentJpaRepository;
    this.studentJpaMapper = studentJpaMapper;
  }

  @Override
  public Optional<Student> findById(StudentID studentID) {
    return studentJpaRepository.findById(studentID.getValue()).map(studentJpaMapper::toDomainStudent);
  }

  @Override
  public Student mustGet(StudentID studentID) {
    return findById(studentID).orElseThrow(() -> new RuntimeException("Student not found with id: " + studentID.getValue()));
  }

  @Override
  public Student save(Student student) {
    StudentJpaEntity studentJpaEntity = studentJpaMapper.toJpaEntity(student);
    studentJpaEntity = studentJpaRepository.save(studentJpaEntity);
    return studentJpaMapper.toDomainStudent(studentJpaEntity);
  }

  @Override
  public List<Student> findAll() {
    return studentJpaRepository.findAll().stream().map(studentJpaMapper::toDomainStudent).toList();
  }

  @Override
  public List<Student> findAllByCourseId(Integer courseId) {
    return studentJpaRepository.findAllByCourseId(courseId).stream().map(studentJpaMapper::toDomainStudent).toList();
  }
}
