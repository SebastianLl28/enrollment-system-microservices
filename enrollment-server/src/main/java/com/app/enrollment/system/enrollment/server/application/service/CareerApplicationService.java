package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCareerCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseWithLevelResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCareerUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCareerUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCareerUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DegreeTitle;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerCourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.FacultyRepository;
import java.time.Clock;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alonso
 */
@UseCase
public class CareerApplicationService implements GetAllCareerUseCase, CreateCareerUseCase, UpdateCareerUseCase {

  private final CareerRepository careerRepository;
  private final FacultyRepository facultyRepository;
  private final CareerMapper careerMapper;
  private final Clock clock;
  private final CourseRepository courseRepository;
  private final CareerCourseRepository careerCourseRepository;
  private final CourseMapper courseMapper;

  public CareerApplicationService(CareerRepository careerRepository, CareerMapper careerMapper,
    FacultyRepository facultyRepository, Clock clock, CourseRepository courseRepository,
    CareerCourseRepository careerCourseRepository, CourseMapper courseMapper) {
    this.careerRepository = careerRepository;
    this.careerMapper = careerMapper;
    this.facultyRepository = facultyRepository;
    this.clock = clock;
    this.courseRepository = courseRepository;
    this.careerCourseRepository = careerCourseRepository;
    this.courseMapper = courseMapper;
  }

  @Override
  public List<CareerResponse> findAll(Boolean includeInactive) {

    List<Career> careerList = includeInactive ? careerRepository.findAll() : careerRepository.findAllActive();

    Set<Integer> facultyIds = careerList.stream().map(c -> c.getFacultyId().getValue())
      .collect(Collectors.toSet());

    List<Faculty> facultyMap = facultyRepository.findAllByIdIn(facultyIds);

    return careerList.stream().map(career -> {

      Faculty faculty = facultyMap.stream()
        .filter(f -> f.getId().getValue().equals(career.getFacultyId().getValue())).findFirst()
        .orElseThrow();

      return careerMapper.toResponse(career, faculty, findCurriculum(career.getId()));
    }).toList();
  }

  @Override
  public CareerResponse updateCareer(UpdateCareerCommand command, Integer careerId) {
    CareerID careerID = new CareerID(careerId);
    Career existing = careerRepository.findById(careerID).orElseThrow(() ->
        new CareerNotFoundException("Career with ID " + careerId + " not found"));

    FacultyID facultyID = new FacultyID(command.facultyId());
    Faculty faculty = facultyRepository.findById(facultyID).orElseThrow(() ->
        new FacultyNotFoundException("Faculty with ID " + command.facultyId() + " not found"));

    DegreeTitle degreeTitle = new DegreeTitle(command.degreeAwarded());
    Career updated = Career.rehydrate(careerID, facultyID, command.name(), command.description(),
        command.semesterLength(), degreeTitle, existing.getRegistrationDate(), command.active());

    Career savedCareer = careerRepository.save(updated);

    return careerMapper.toResponse(savedCareer, faculty, findCurriculum(savedCareer.getId()));
  }

  @Override
  public CareerResponse createCareer(CreateCareerCommand command) {
    FacultyID facultyID = new FacultyID(command.facultyId());
    DegreeTitle degreeTitle = new DegreeTitle(command.degreeAwarded());

    Career career = Career.create(facultyID, command.name(), command.description(),
      command.semesterLength(), degreeTitle, clock.instant());

    Faculty faculty = facultyRepository.findById(facultyID).orElseThrow(
      () -> new FacultyNotFoundException("Faculty with ID " + facultyID.getValue() + " not found"));

    Career savedCareer = careerRepository.save(career);

    return careerMapper.toResponse(savedCareer, faculty, findCurriculum(savedCareer.getId()));
  }

  /**
   * Malla curricular de la carrera: cursos activos con el ciclo en que se dictan.
   */
  private List<CourseWithLevelResponse> findCurriculum(CareerID careerID) {
    return careerCourseRepository.findByCareerId(careerID).stream()
      .map(careerCourse -> {
        Course course = courseRepository.findById(careerCourse.getCourseId()).orElseThrow(() ->
          new CourseNotFoundException(
            "Course with ID " + careerCourse.getCourseId().getValue() + " not found"));
        return courseMapper.toWithLevelResponse(course, careerCourse.getSemesterLevel().getValue());
      })
      .filter(CourseWithLevelResponse::getActive)
      .toList();
  }
}
