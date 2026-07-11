package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.ClassroomMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateClassroomUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllClassroomUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateClassroomUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.ClassroomNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidClassroomException;
import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.domain.repository.ClassroomRepository;
import java.time.Clock;
import java.util.List;

/**
 * @author Alonso
 */
@UseCase
public class ClassroomApplicationService implements CreateClassroomUseCase,
  GetAllClassroomUseCase, UpdateClassroomUseCase {

  private final ClassroomRepository classroomRepository;
  private final ClassroomMapper classroomMapper;
  private final Clock clock;

  public ClassroomApplicationService(ClassroomRepository classroomRepository,
    ClassroomMapper classroomMapper, Clock clock) {
    this.classroomRepository = classroomRepository;
    this.classroomMapper = classroomMapper;
    this.clock = clock;
  }

  @Override
  public ClassroomResponse createClassroom(CreateClassroomCommand command) {
    validateCapacity(command.virtual(), command.capacity());

    Classroom classroom = Classroom.create(
      command.code(),
      command.name(),
      normalizedCapacity(command.virtual(), command.capacity()),
      command.virtual(),
      clock.instant()
    );

    Classroom saved = classroomRepository.save(classroom);
    return classroomMapper.toClassroomResponse(saved);
  }

  @Override
  public ClassroomResponse updateClassroom(UpdateClassroomCommand command, Integer classroomId) {
    ClassroomID classroomID = new ClassroomID(classroomId);
    Classroom existing = classroomRepository.findById(classroomID).orElseThrow(
      () -> new ClassroomNotFoundException("Classroom with ID " + classroomId + " not found.")
    );

    validateCapacity(command.virtual(), command.capacity());

    Classroom updated = Classroom.rehydrate(classroomID, command.code(), command.name(),
      normalizedCapacity(command.virtual(), command.capacity()), command.virtual(),
      command.active(), existing.getCreatedAt());

    Classroom saved = classroomRepository.save(updated);
    return classroomMapper.toClassroomResponse(saved);
  }

  @Override
  public List<ClassroomResponse> getAllClassrooms() {
    return classroomRepository.findAll().stream()
      .map(classroomMapper::toClassroomResponse)
      .toList();
  }

  /**
   * Las aulas físicas requieren capacidad positiva; las virtuales no tienen límite.
   */
  private void validateCapacity(Boolean virtual, Integer capacity) {
    if (!Boolean.TRUE.equals(virtual) && (capacity == null || capacity <= 0)) {
      throw new InvalidClassroomException(
        "Un aula física requiere una capacidad mayor a 0.");
    }
  }

  private Integer normalizedCapacity(Boolean virtual, Integer capacity) {
    return Boolean.TRUE.equals(virtual) ? null : capacity;
  }
}
