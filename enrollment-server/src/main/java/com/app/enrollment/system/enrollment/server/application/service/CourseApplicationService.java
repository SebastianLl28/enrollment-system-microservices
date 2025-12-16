package com.app.enrollment.system.enrollment.server.application.service;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCourseUseCase;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CareerID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseCode;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Credits;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SemesterLevel;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import java.time.Clock;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Alonso
 */
@Service
public class CourseApplicationService implements GetAllCourseUseCase, CreateCourseUseCase {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final Clock clock;
  private final StudentRepository studentRepository;
  
  public CourseApplicationService(CourseRepository courseRepository, CourseMapper courseMapper, Clock clock, StudentRepository studentRepository) {
    this.courseRepository = courseRepository;
    this.courseMapper = courseMapper;
    this.clock = clock;
    this.studentRepository = studentRepository;
  }

  @Override
  public List<CourseResponse> findAll() {
    List<Course> courseList = courseRepository.findAll();

    return courseList.stream()
        .map(course -> {
          List<Student> enrolledStudents = studentRepository.findAllByCourseId
          (course.getId().getValue());
          return courseMapper.toResponse(course, enrolledStudents);
        })
        .toList();
  }

  @Override
  public CourseResponse createCourse(CreateCourseCommand createCourseCommand) {
    CareerID careerID = new CareerID(createCourseCommand.careerId());
    CourseCode courseCode = new CourseCode(createCourseCommand.code());
    Credits credits = new Credits(createCourseCommand.credits());
    SemesterLevel semesterLevel = new SemesterLevel(createCourseCommand.semesterLevel());

    Course course = Course.create(careerID, courseCode, createCourseCommand.name(), createCourseCommand.description(), credits, semesterLevel, clock.instant());

    Course savedCourse = courseRepository.save(course);
    return courseMapper.toResponse(savedCourse, List.of());
  }
}
