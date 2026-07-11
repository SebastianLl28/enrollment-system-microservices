package com.app.enrollment.system.enrollment.server.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateSectionCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.ClassroomMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.SectionMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSectionException;
import com.app.enrollment.system.enrollment.server.domain.model.CareerCourse;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerCourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.ClassroomRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.SectionRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import com.app.enrollment.system.enrollment.server.testsupport.Mothers;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SectionApplicationServiceTest {

  @Mock
  private SectionRepository sectionRepository;
  @Mock
  private CourseRepository courseRepository;
  @Mock
  private TermRepository termRepository;
  @Mock
  private ClassroomRepository classroomRepository;
  @Mock
  private CareerCourseRepository careerCourseRepository;
  @Mock
  private CareerOfferingRepository careerOfferingRepository;

  private SectionApplicationService service;

  @BeforeEach
  void setUp() {
    service = new SectionApplicationService(sectionRepository, courseRepository, termRepository,
      classroomRepository, careerCourseRepository, careerOfferingRepository, new SectionMapper(),
      new CourseMapper(), new TermMapper(), new ClassroomMapper(), Mothers.fixedClock());
  }

  private void stubCatalog() {
    when(courseRepository.findById(any()))
      .thenReturn(Optional.of(Mothers.course(1, "CS101", "Matemática I")));
    when(termRepository.findById(any())).thenReturn(Optional.of(Mothers.term(2, "2026-I")));
    when(classroomRepository.findById(any()))
      .thenReturn(Optional.of(Mothers.physicalClassroom(3, "B-201", 30)));
  }

  private CareerCourse curriculumAssignment(int careerId) {
    return CareerCourse.create(new CareerID(careerId), new CourseID(1), new SemesterLevel(1));
  }

  @Test
  void createSectionSucceedsWhenCourseCareerIsOfferedInTerm() {
    stubCatalog();
    when(careerCourseRepository.findByCourseId(any()))
      .thenReturn(List.of(curriculumAssignment(7)));
    when(careerOfferingRepository.findAllByTermId(new TermID(2)))
      .thenReturn(List.of(Mothers.careerOffering(1, 7, 2)));
    when(sectionRepository.save(any())).thenReturn(Mothers.section(5, 1, 2, 3, "A"));

    SectionResponse response =
      service.createSection(new CreateSectionCommand(1, 2, 3, "A"));

    assertThat(response.getSectionCode()).isEqualTo("A");
    assertThat(response.getClassroom().getVirtual()).isFalse();
    verify(sectionRepository).save(any());
  }

  @Test
  void createSectionRejectsTermWithoutOfferingsForCourseCareers() {
    stubCatalog();
    when(careerCourseRepository.findByCourseId(any()))
      .thenReturn(List.of(curriculumAssignment(7)));
    // El periodo solo tiene ofertada otra carrera (id 99) que no dicta el curso.
    when(careerOfferingRepository.findAllByTermId(new TermID(2)))
      .thenReturn(List.of(Mothers.careerOffering(1, 99, 2)));

    assertThatThrownBy(() -> service.createSection(new CreateSectionCommand(1, 2, 3, "A")))
      .isInstanceOf(InvalidSectionException.class);

    verify(sectionRepository, never()).save(any());
  }

  @Test
  void createSectionIgnoresInactiveOfferings() {
    stubCatalog();
    when(careerCourseRepository.findByCourseId(any()))
      .thenReturn(List.of(curriculumAssignment(7)));
    when(careerOfferingRepository.findAllByTermId(new TermID(2)))
      .thenReturn(List.of(
        Mothers.careerOffering(1, 7, 2, 30, 0, false, new BigDecimal("350.00"))));

    assertThatThrownBy(() -> service.createSection(new CreateSectionCommand(1, 2, 3, "A")))
      .isInstanceOf(InvalidSectionException.class);
  }
}
