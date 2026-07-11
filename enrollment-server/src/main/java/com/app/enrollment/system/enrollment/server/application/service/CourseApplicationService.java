package com.app.enrollment.system.enrollment.server.application.service;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerAssignmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCourseUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.CareerCourse;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseCode;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Credits;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerCourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CareerRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import java.time.Clock;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */
@Service
public class CourseApplicationService implements GetAllCourseUseCase, CreateCourseUseCase, UpdateCourseUseCase {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final Clock clock;
  private final CareerCourseRepository careerCourseRepository;
  private final CareerRepository careerRepository;

  public CourseApplicationService(CourseRepository courseRepository, CourseMapper courseMapper,
    Clock clock, CareerCourseRepository careerCourseRepository, CareerRepository careerRepository) {
    this.courseRepository = courseRepository;
    this.courseMapper = courseMapper;
    this.clock = clock;
    this.careerCourseRepository = careerCourseRepository;
    this.careerRepository = careerRepository;
  }

  @Override
  public List<CourseResponse> findAll() {
    List<Course> courseList = courseRepository.findAll();

    return courseList.stream()
        .map(course -> courseMapper.toResponse(course, findCareerAssignments(course.getId())))
        .toList();
  }

  @Override
  @Transactional
  public CourseResponse updateCourse(UpdateCourseCommand command, Integer courseId) {
    CourseID courseID = new CourseID(courseId);
    Course existing = courseRepository.findById(courseID).orElseThrow(() ->
        new CourseNotFoundException("Course with ID " + courseId + " not found"));

    CourseCode courseCode = new CourseCode(command.code());
    Credits credits = new Credits(command.credits());

    Course updated = Course.rehydrate(courseID, courseCode, command.name(),
        command.description(), credits, existing.getRegistrationDate(), command.active());

    Course savedCourse = courseRepository.save(updated);

    replaceCareerAssignments(savedCourse.getId(), command.careers().stream()
        .map(assignment -> toCareerCourse(savedCourse.getId(), assignment.careerId(),
            assignment.semesterLevel()))
        .toList());

    return courseMapper.toResponse(savedCourse, findCareerAssignments(savedCourse.getId()));
  }

  @Override
  @Transactional
  public CourseResponse createCourse(CreateCourseCommand createCourseCommand) {
    CourseCode courseCode = new CourseCode(createCourseCommand.code());
    Credits credits = new Credits(createCourseCommand.credits());

    Course course = Course.create(courseCode, createCourseCommand.name(),
        createCourseCommand.description(), credits, clock.instant());

    Course savedCourse = courseRepository.save(course);

    replaceCareerAssignments(savedCourse.getId(), createCourseCommand.careers().stream()
        .map(assignment -> toCareerCourse(savedCourse.getId(), assignment.careerId(),
            assignment.semesterLevel()))
        .toList());

    return courseMapper.toResponse(savedCourse, findCareerAssignments(savedCourse.getId()));
  }

  private CareerCourse toCareerCourse(CourseID courseID, Integer careerId, Integer semesterLevel) {
    CareerID careerID = new CareerID(careerId);
    // Valida que la carrera exista antes de asignarla a la malla.
    careerRepository.findById(careerID).orElseThrow(() ->
        new CareerNotFoundException("Career with ID " + careerId + " not found"));
    return CareerCourse.create(careerID, courseID, new SemesterLevel(semesterLevel));
  }

  private void replaceCareerAssignments(CourseID courseID, List<CareerCourse> careerCourses) {
    careerCourseRepository.replaceForCourse(courseID, careerCourses);
  }

  private List<CareerAssignmentResponse> findCareerAssignments(CourseID courseID) {
    return careerCourseRepository.findByCourseId(courseID).stream()
        .map(careerCourse -> {
          Career career = careerRepository.findById(careerCourse.getCareerId()).orElseThrow(() ->
              new CareerNotFoundException(
                  "Career with ID " + careerCourse.getCareerId().getValue() + " not found"));
          return new CareerAssignmentResponse(
              career.getId().getValue(),
              career.getName(),
              careerCourse.getSemesterLevel().getValue());
        })
        .toList();
  }
}
