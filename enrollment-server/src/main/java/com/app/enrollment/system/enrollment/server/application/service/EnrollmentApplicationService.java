package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.common.enums.SourceServiceType;
import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.SendEnrollmentEvent;
import com.app.enrollment.system.enrollment.server.application.dto.command.UnenrollStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.query.EnrollmentQuery;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.UserSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.EnrollmentMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllEnrollmentCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetEnrollmentByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UnenrollStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.out.OutboxEventPort;
import com.app.enrollment.system.enrollment.server.domain.event.EventType;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseOfferingNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentAlreadyEnrolledException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Enrollment;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseOfferingID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.EnrollmentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.EnrollmentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.StudentRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */
@UseCase
public class EnrollmentApplicationService implements CreateEnrollmentUseCase,
  UnenrollStudentUseCase, GetAllEnrollmentCourseUseCase, GetEnrollmentByIdUseCase,
  UpdateEnrollmentUseCase {
  
  private final EnrollmentRepository enrollmentRepository;
  private final Clock clock;
  private final EnrollmentMapper enrollmentMapper;
  private final CourseOfferingRepository courseOfferingRepository;
  private final StudentRepository studentRepository;
  private final OutboxEventPort outboxEventPort;
  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final TermRepository termRepository;
  private final TermMapper termMapper;
  
  public EnrollmentApplicationService(EnrollmentRepository enrollmentRepository, Clock clock,
    EnrollmentMapper enrollmentMapper, CourseOfferingRepository courseOfferingRepository,
    StudentRepository studentRepository, OutboxEventPort outboxEventPort,
    CourseRepository courseRepository, CourseMapper courseMapper, TermRepository termRepository,
    TermMapper termMapper) {
    this.enrollmentRepository = enrollmentRepository;
    this.clock = clock;
    this.enrollmentMapper = enrollmentMapper;
    this.courseOfferingRepository = courseOfferingRepository;
    this.studentRepository = studentRepository;
    this.outboxEventPort = outboxEventPort;
    this.courseRepository = courseRepository;
    this.courseMapper = courseMapper;
    this.termRepository = termRepository;
    this.termMapper = termMapper;
  }
  
  @Override
  @Transactional
  public EnrollmentResponse createEnrollment(CreateEnrollmentCommand command, Integer userId) {
    
    StudentID studentID = new StudentID(command.studentId());
    CourseOfferingID courseOfferingID = new CourseOfferingID(command.courseOfferingId());
    Instant now = clock.instant();
    
    Optional<Enrollment> findEnrollment = enrollmentRepository.findByStudentIDAndCourseOfferingIDAndStatus(
      studentID, courseOfferingID, EnrollmentStatus.PENDING);
    
    if (findEnrollment.isPresent()) {
      Enrollment existingEnrollment = findEnrollment.get();
      if (!existingEnrollment.getStatus().equals(EnrollmentStatus.CANCELLED)) {
        throw new StudentAlreadyEnrolledException(
          "Student with id: " + studentID.getValue() + " is already enrolled in course with id: "
            + courseOfferingID.getValue());
      }
    }
    
    CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingID).orElseThrow(
      () -> new CourseOfferingNotFoundException(
        "Course offering not found with id: " + courseOfferingID.getValue()));
    
    Student student = studentRepository.findById(studentID).orElseThrow(
      () -> new StudentNotFoundException("Student not found with id: " + studentID.getValue()));
    
    UserID userID = new UserID(userId);
    
    Enrollment enrollment = Enrollment.create(studentID, courseOfferingID, now, userID);
    
    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
    
    courseOffering.incrementEnrolledCount();
    
    courseOfferingRepository.save(courseOffering);
    
    Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Course not found with id: " + courseOffering.getCourseId().getValue()));
    
    saveOutboxEvent(savedEnrollment, userID, now, student, course);
    
    return getEnrollmentResponse(courseOffering, student, savedEnrollment);
    
  }
  
  private EnrollmentResponse getEnrollmentResponse(CourseOffering courseOffering, Student student,
    Enrollment savedEnrollment) {
    Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Course not found with id: " + courseOffering.getCourseId().getValue()));
    
    CourseSummaryResponse courseSummaryResponse = courseMapper.toSummaryResponse(course);
    
    Term term = termRepository.findById(courseOffering.getTermId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Term not found with id: " + courseOffering.getTermId().getValue()));
    
    TermResponse termResponse = termMapper.toTermResponse(term);
    
    return enrollmentMapper.toEnrollmentResponse(savedEnrollment, student, courseOffering, courseSummaryResponse, termResponse);
  }
  
  
  private void saveOutboxEvent(Enrollment savedEnrollment, UserID userID, Instant now, Student student, Course course) {
    
    CourseOffering courseOffering = courseOfferingRepository.findById(
      savedEnrollment.getCourseOfferingID()).orElseThrow(
      () -> new CourseOfferingNotFoundException(
        "Course offering not found with id: " + savedEnrollment.getCourseOfferingID().getValue()));
    
    EnrollmentAssignedEvent event = new EnrollmentAssignedEvent(
      savedEnrollment.getID().getValue().toString(),
      savedEnrollment.getStudentID().getValue().toString(),
      courseOffering.getCourseId().toString(), now,
      student.getFullName(), student.getEmail().getValue(),
      savedEnrollment.getStatus().toString(),
      userID.getValue(),
      course.getName()
    );
    
    SendEnrollmentEvent sendEnrollmentEvent = new SendEnrollmentEvent(
      SourceServiceType.ENROLLMENT_SERVICE.name(),
      savedEnrollment.getID().getValue().toString(),
      EventType.ENROLLMENT_EVENT,
      event,
      userID,
      savedEnrollment.getStatus(),
      student,
      course
    );
    
    outboxEventPort.saveEvent(sendEnrollmentEvent);
    
  }
  
  
  @Override
  public EnrollmentResponse unenrollStudent(UnenrollStudentCommand command, UserID userID) {
    StudentID studentID = new StudentID(command.studentId());
    CourseOfferingID courseOfferingID = new CourseOfferingID(command.courseOfferingId());
    
    Enrollment enrollment = enrollmentRepository.findByStudentIDAndCourseOfferingIDAndStatus(studentID,
      courseOfferingID, EnrollmentStatus.COMPLETED).orElseThrow(() -> new EnrollmentNotFoundException(
      "Enrollment not found for student id: " + studentID.getValue() + " and course id: "
        + courseOfferingID.getValue()));
    
    enrollment.unenroll(clock.instant());
    
    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
    
    Student student = studentRepository.findById(studentID).orElseThrow(
      () -> new StudentNotFoundException("Student not found with id: " + studentID.getValue()));
    
    CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingID).orElseThrow(
      () -> new CourseOfferingNotFoundException(
        "Course offering not found with id: " + courseOfferingID.getValue()));
    
    courseOffering.decrementEnrolledCount();
    
    courseOfferingRepository.save(courseOffering);
    
    Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Course not found with id: " + courseOffering.getCourseId().getValue()));
    
    saveOutboxEvent(enrollment, userID, clock.instant(), student, course);
    
    return getEnrollmentResponse(courseOffering, student, updatedEnrollment);
    
  }
  
  @Override
  public List<EnrollmentResponse> getAllEnrollmentCourses(EnrollmentQuery query) {
    StudentID studentID = new StudentID(query.studentId());
    TermID termID = new TermID(query.termId());
    CourseID courseID = new CourseID(query.courseId());
    
    List<Enrollment> enrollmentList = enrollmentRepository.findAllByStudentIDAndTermIDAndCourseID(
      studentID, termID, courseID);
    
    return enrollmentList.stream().map(enrollment -> {
      
      Student student = studentRepository.findById(enrollment.getStudentID()).orElseThrow(
        () -> new StudentNotFoundException(
          "Student not found with id: " + enrollment.getStudentID().getValue()));
      
      CourseOffering courseOffering = courseOfferingRepository.findById(
        enrollment.getCourseOfferingID()).orElseThrow(
        () -> new CourseOfferingNotFoundException(
          "Course offering not found with id: " + enrollment.getCourseOfferingID().getValue()));
      
      Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
        () -> new CourseNotFoundException(
          "Course not found with id: " + courseOffering.getCourseId().getValue()));
      
      CourseSummaryResponse courseSummaryResponse = courseMapper.toSummaryResponse(course);
      
      Term term = termRepository.findById(courseOffering.getTermId()).orElseThrow(
        () -> new CourseNotFoundException(
          "Term not found with id: " + courseOffering.getTermId().getValue()));
      
      TermResponse termResponse = termMapper.toTermResponse(term);
      
      return enrollmentMapper.toEnrollmentResponse(enrollment, student, courseOffering, courseSummaryResponse, termResponse);
    }).toList();
  }
  
  @Override
  public EnrollmentResponse getEnrollmentById(Integer enrollmentId, UserID userID) {
    EnrollmentID enrollmentID = new EnrollmentID(enrollmentId);
    
    Enrollment enrollment = enrollmentRepository.findByEnrollmentID(enrollmentID).orElseThrow(
      () -> new EnrollmentNotFoundException(
        "Enrollment not found with id: " + enrollmentID.getValue()));
    
    Student student = studentRepository.findById(enrollment.getStudentID()).orElseThrow(
      () -> new StudentNotFoundException(
        "Student not found with id: " + enrollment.getStudentID().getValue()));
    
    CourseOffering courseOffering = courseOfferingRepository.findById(
      enrollment.getCourseOfferingID()).orElseThrow(
      () -> new CourseOfferingNotFoundException(
        "Course offering not found with id: " + enrollment.getCourseOfferingID().getValue()));
    
    Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Course not found with id: " + courseOffering.getCourseId().getValue()));
    
    CourseSummaryResponse courseSummaryResponse = courseMapper.toSummaryResponse(course);
    
    Term term = termRepository.findById(courseOffering.getTermId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Term not found with id: " + courseOffering.getTermId().getValue()));
    
    TermResponse termResponse = termMapper.toTermResponse(term);
    
    return enrollmentMapper.toEnrollmentResponse(enrollment, student, courseOffering, courseSummaryResponse, termResponse);
  }
  
  @Override
  public EnrollmentResponse updateEnrollment(Integer enrollmentId,
    UpdateEnrollmentCommand command, UserID userID) {
    
    EnrollmentID enrollmentID = new EnrollmentID(enrollmentId);
    
    Enrollment enrollment = enrollmentRepository.findByEnrollmentID(enrollmentID).orElseThrow(
      () -> new EnrollmentNotFoundException(
        "Enrollment not found with id: " + enrollmentID.getValue()));
    
    Student student = studentRepository.findById(enrollment.getStudentID()).orElseThrow(
      () -> new StudentNotFoundException(
        "Student not found with id: " + enrollment.getStudentID().getValue()));
    
    CourseOffering courseOffering = courseOfferingRepository.findById(
      enrollment.getCourseOfferingID()).orElseThrow(
      () -> new CourseOfferingNotFoundException(
        "Course offering not found with id: " + enrollment.getCourseOfferingID().getValue()));
    
    if (command.status() == EnrollmentStatus.CANCELLED) {
      enrollment.unenroll(clock.instant());
      courseOffering.decrementEnrolledCount();
    } else {
      enrollment.updateStatus(command.status());
    }
    
    courseOfferingRepository.save(courseOffering);
    
    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
    
    Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
      () -> new CourseNotFoundException(
        "Course not found with id: " + courseOffering.getCourseId().getValue()));
    
    saveOutboxEvent(updatedEnrollment, userID, clock.instant(), student, course);
    
    return getEnrollmentResponse(courseOffering, student, updatedEnrollment);
    
  }
}
