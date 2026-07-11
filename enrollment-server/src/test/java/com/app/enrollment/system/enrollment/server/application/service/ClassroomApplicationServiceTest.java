package com.app.enrollment.system.enrollment.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.ClassroomMapper;
import com.app.enrollment.system.enrollment.server.domain.exception.ClassroomNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidClassroomException;
import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import com.app.enrollment.system.enrollment.server.domain.repository.ClassroomRepository;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassroomApplicationServiceTest {

  @Mock
  private ClassroomRepository classroomRepository;

  private ClassroomApplicationService service;

  @BeforeEach
  void setUp() {
    service = new ClassroomApplicationService(classroomRepository, new ClassroomMapper(),
      Mothers.fixedClock());
  }

  @Test
  void createPhysicalClassroomRequiresPositiveCapacity() {
    assertThatThrownBy(
      () -> service.createClassroom(new CreateClassroomCommand("B-201", "Lab", null, false)))
      .isInstanceOf(InvalidClassroomException.class);
    assertThatThrownBy(
      () -> service.createClassroom(new CreateClassroomCommand("B-201", "Lab", 0, false)))
      .isInstanceOf(InvalidClassroomException.class);

    verify(classroomRepository, never()).save(any());
  }

  @Test
  void createVirtualClassroomNormalizesCapacityToNull() {
    when(classroomRepository.save(any())).thenAnswer(inv -> Mothers.virtualClassroom(1, "ZOOM-1"));

    ClassroomResponse response =
      service.createClassroom(new CreateClassroomCommand("ZOOM-1", "Aula Zoom", 999, true));

    ArgumentCaptor<Classroom> captor = ArgumentCaptor.forClass(Classroom.class);
    verify(classroomRepository).save(captor.capture());
    assertThat(captor.getValue().getCapacity()).isNull();
    assertThat(captor.getValue().isVirtual()).isTrue();
    assertThat(response.getVirtual()).isTrue();
  }

  @Test
  void createPhysicalClassroomKeepsCapacity() {
    when(classroomRepository.save(any()))
      .thenAnswer(inv -> Mothers.physicalClassroom(1, "B-201", 30));

    service.createClassroom(new CreateClassroomCommand("B-201", "Lab", 30, false));

    ArgumentCaptor<Classroom> captor = ArgumentCaptor.forClass(Classroom.class);
    verify(classroomRepository).save(captor.capture());
    assertThat(captor.getValue().getCapacity()).isEqualTo(30);
  }

  @Test
  void updateMissingClassroomThrowsNotFound() {
    when(classroomRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.updateClassroom(
      new UpdateClassroomCommand("B-201", "Lab", 30, false, true), 5))
      .isInstanceOf(ClassroomNotFoundException.class);
  }
}
