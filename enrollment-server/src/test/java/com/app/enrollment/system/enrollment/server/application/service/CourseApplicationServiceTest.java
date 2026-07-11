package com.app.enrollment.system.enrollment.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.enrollment.system.enrollment.server.application.dto.command.CareerCourseAssignmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.CareerCourse;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerCourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseApplicationServiceTest {

  @Mock
  private CourseRepository courseRepository;
  @Mock
  private CareerCourseRepository careerCourseRepository;
  @Mock
  private CareerRepository careerRepository;

  private CourseApplicationService service;

  @BeforeEach
  void setUp() {
    service = new CourseApplicationService(courseRepository, new CourseMapper(),
      Mothers.fixedClock(), careerCourseRepository, careerRepository);
  }

  @Test
  void createCourseAssignsCurriculumWithLevelPerCareer() {
    when(courseRepository.save(any()))
      .thenReturn(Mothers.course(1, "CS101", "Matemática I"));
    when(careerRepository.findById(new CareerID(7)))
      .thenReturn(Optional.of(Mothers.career(7, "Diseño de Software")));
    when(careerRepository.findById(new CareerID(8)))
      .thenReturn(Optional.of(Mothers.career(8, "Mecatrónica")));
    when(careerCourseRepository.findByCourseId(new CourseID(1)))
      .thenReturn(List.of(
        CareerCourse.create(new CareerID(7), new CourseID(1), new SemesterLevel(1)),
        CareerCourse.create(new CareerID(8), new CourseID(1), new SemesterLevel(3))));

    CourseResponse response = service.createCourse(new CreateCourseCommand("CS101",
      "Matemática I", "desc", 3,
      List.of(new CareerCourseAssignmentCommand(7, 1), new CareerCourseAssignmentCommand(8, 3))));

    // La malla se reemplaza con las dos asignaciones y el ciclo varía por carrera.
    ArgumentCaptor<List<CareerCourse>> captor = ArgumentCaptor.forClass(List.class);
    verify(careerCourseRepository).replaceForCourse(eq(new CourseID(1)), captor.capture());
    assertThat(captor.getValue()).hasSize(2);

    assertThat(response.getCareers()).hasSize(2);
    assertThat(response.getCareers().get(0).getSemesterLevel()).isEqualTo(1);
    assertThat(response.getCareers().get(1).getSemesterLevel()).isEqualTo(3);
  }

  @Test
  void createCourseRejectsUnknownCareerInCurriculum() {
    when(courseRepository.save(any()))
      .thenReturn(Mothers.course(1, "CS101", "Matemática I"));
    when(careerRepository.findById(new CareerID(7))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.createCourse(new CreateCourseCommand("CS101",
      "Matemática I", "desc", 3, List.of(new CareerCourseAssignmentCommand(7, 1)))))
      .isInstanceOf(CareerNotFoundException.class);

    verify(careerCourseRepository, never()).replaceForCourse(any(), anyList());
  }
}
