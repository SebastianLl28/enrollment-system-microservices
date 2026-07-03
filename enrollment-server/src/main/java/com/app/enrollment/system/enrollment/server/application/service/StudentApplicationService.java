package com.app.enrollment.system.enrollment.server.application.service;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.StudentMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetStudentByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateStudentUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DocumentNumber;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Email;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Alonso
 */
@Service
public class StudentApplicationService implements CreateStudentUseCase, GetAllStudentUseCase,
    UpdateStudentUseCase, GetStudentByIdUseCase {

  private final Clock clock;
  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  public StudentApplicationService(Clock clock, StudentRepository studentRepository, StudentMapper studentMapper) {
    this.clock = clock;
    this.studentRepository = studentRepository;
    this.studentMapper = studentMapper;
  }

  @Override
  public StudentResponse createStudent(CreateStudentCommand command) {
    DocumentNumber documentNumber = new DocumentNumber(command.documentNumber());
    Email email = new Email(command.email());
    LocalDate birthDate = LocalDate.parse(command.birthDate());
    Student student = Student.create(command.name(), command.lastName(), documentNumber,
        email, command.phoneNumber(), birthDate, command.address(), clock.instant());
    student = studentRepository.save(student);
    return studentMapper.toStudentResponse(student);
  }

  @Override
  public List<StudentResponse> findAll() {
    return studentRepository.findAll().stream().map(
        studentMapper::toStudentResponse
    ).toList();
  }

  @Override
  public StudentResponse findById(Integer studentId) {
    StudentID studentID = new StudentID(studentId);
    Student student = studentRepository.findById(studentID).orElseThrow(
        () -> new StudentNotFoundException("Student not found with id: " + studentId));
    return studentMapper.toStudentResponse(student);
  }

  @Override
  public StudentResponse updateStudent(UpdateStudentCommand command, Integer studentId) {
    StudentID studentID = new StudentID(studentId);
    Student existing = studentRepository.findById(studentID).orElseThrow(
        () -> new StudentNotFoundException("Student not found with id: " + studentId));

    DocumentNumber documentNumber = new DocumentNumber(command.documentNumber());
    Email email = new Email(command.email());
    LocalDate birthDate = LocalDate.parse(command.birthDate());

    Student updated = Student.rehydrate(studentID, command.name(), command.lastName(),
        documentNumber, email, command.phoneNumber(), birthDate, command.address(),
        existing.getCreatedAt(), command.active());

    Student saved = studentRepository.save(updated);
    return studentMapper.toStudentResponse(saved);
  }
}
