package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateSectionCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateSectionCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.ClassroomMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.SectionMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateSectionUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllSectionUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateSectionUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.ClassroomNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSectionException;
import com.app.enrollment.system.enrollment.server.domain.exception.SectionNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Classroom;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Section;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.ClassroomID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SectionID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerCourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.ClassroomRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.SectionRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alonso
 */
@UseCase
public class SectionApplicationService implements CreateSectionUseCase, GetAllSectionUseCase,
  UpdateSectionUseCase {

  private final SectionRepository sectionRepository;
  private final CourseRepository courseRepository;
  private final TermRepository termRepository;
  private final ClassroomRepository classroomRepository;
  private final CareerCourseRepository careerCourseRepository;
  private final CareerOfferingRepository careerOfferingRepository;
  private final SectionMapper sectionMapper;
  private final CourseMapper courseMapper;
  private final TermMapper termMapper;
  private final ClassroomMapper classroomMapper;
  private final Clock clock;

  public SectionApplicationService(SectionRepository sectionRepository,
    CourseRepository courseRepository, TermRepository termRepository,
    ClassroomRepository classroomRepository, CareerCourseRepository careerCourseRepository,
    CareerOfferingRepository careerOfferingRepository, SectionMapper sectionMapper,
    CourseMapper courseMapper, TermMapper termMapper, ClassroomMapper classroomMapper,
    Clock clock) {
    this.sectionRepository = sectionRepository;
    this.courseRepository = courseRepository;
    this.termRepository = termRepository;
    this.classroomRepository = classroomRepository;
    this.careerCourseRepository = careerCourseRepository;
    this.careerOfferingRepository = careerOfferingRepository;
    this.sectionMapper = sectionMapper;
    this.courseMapper = courseMapper;
    this.termMapper = termMapper;
    this.classroomMapper = classroomMapper;
    this.clock = clock;
  }

  @Override
  public SectionResponse createSection(CreateSectionCommand command) {
    CourseID courseID = new CourseID(command.courseId());
    TermID termID = new TermID(command.termId());
    ClassroomID classroomID = new ClassroomID(command.classroomId());

    Course course = findCourse(courseID);
    Term term = findTerm(termID);
    Classroom classroom = findClassroom(classroomID);

    validateCourseOfferedInTerm(courseID, termID);

    Section section = Section.create(courseID, termID, classroomID, command.sectionCode(),
      clock.instant());

    Section saved = sectionRepository.save(section);

    return toResponse(saved, course, term, classroom);
  }

  @Override
  public SectionResponse updateSection(UpdateSectionCommand command, Integer sectionId) {
    SectionID sectionID = new SectionID(sectionId);
    Section existing = sectionRepository.findById(sectionID).orElseThrow(
      () -> new SectionNotFoundException("Section with ID " + sectionId + " not found.")
    );

    CourseID courseID = new CourseID(command.courseId());
    TermID termID = new TermID(command.termId());
    ClassroomID classroomID = new ClassroomID(command.classroomId());

    Course course = findCourse(courseID);
    Term term = findTerm(termID);
    Classroom classroom = findClassroom(classroomID);

    validateCourseOfferedInTerm(courseID, termID);

    Section updated = Section.rehydrate(sectionID, courseID, termID, classroomID,
      command.sectionCode(), command.active(), existing.getCreatedAt());

    Section saved = sectionRepository.save(updated);

    return toResponse(saved, course, term, classroom);
  }

  @Override
  public List<SectionResponse> getAllSections() {
    return sectionRepository.findAll().stream().map(section -> {
      Course course = findCourse(section.getCourseId());
      Term term = findTerm(section.getTermId());
      Classroom classroom = findClassroom(section.getClassroomId());
      return toResponse(section, course, term, classroom);
    }).toList();
  }

  private SectionResponse toResponse(Section section, Course course, Term term,
    Classroom classroom) {
    CourseSummaryResponse courseSummaryResponse = courseMapper.toSummaryResponse(course);
    TermResponse termResponse = termMapper.toTermResponse(term);
    ClassroomResponse classroomResponse = classroomMapper.toClassroomResponse(classroom);
    return sectionMapper.toSectionResponse(section, courseSummaryResponse, termResponse,
      classroomResponse);
  }

  /**
   * Una sección solo tiene sentido en un periodo donde al menos una carrera con
   * el curso en su malla esté ofertada (CareerOffering activa) — de lo contrario
   * no habría alumnos matriculados que puedan cursarla.
   */
  private void validateCourseOfferedInTerm(CourseID courseID, TermID termID) {
    Set<Integer> courseCareerIds = careerCourseRepository.findByCourseId(courseID).stream()
      .map(careerCourse -> careerCourse.getCareerId().getValue())
      .collect(Collectors.toSet());

    Set<Integer> offeredCareerIds = careerOfferingRepository.findAllByTermId(termID).stream()
      .filter(CareerOffering::isActive)
      .map(CareerOffering::getCareerId)
      .map(CareerID::getValue)
      .collect(Collectors.toSet());

    boolean offered = courseCareerIds.stream().anyMatch(offeredCareerIds::contains);
    if (!offered) {
      throw new InvalidSectionException(
        "El curso no tiene ninguna carrera ofertada en el periodo seleccionado.");
    }
  }

  private Course findCourse(CourseID courseID) {
    return courseRepository.findById(courseID).orElseThrow(
      () -> new CourseNotFoundException("Course with ID " + courseID.getValue() + " not found.")
    );
  }

  private Term findTerm(TermID termID) {
    return termRepository.findById(termID).orElseThrow(
      () -> new TermNotFoundException("Term with ID " + termID.getValue() + " not found.")
    );
  }

  private Classroom findClassroom(ClassroomID classroomID) {
    return classroomRepository.findById(classroomID).orElseThrow(
      () -> new ClassroomNotFoundException(
        "Classroom with ID " + classroomID.getValue() + " not found.")
    );
  }
}
