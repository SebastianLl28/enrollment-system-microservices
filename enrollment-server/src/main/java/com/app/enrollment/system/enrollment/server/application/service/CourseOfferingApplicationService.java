package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.CourseOfferingMapper;
import com.app.enrollment.system.enrollment.server.application.mapper.TermMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCourseOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCourseOfferingUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.CourseOffering;
import com.app.enrollment.system.enrollment.server.domain.model.Term;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.CourseID;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.TermID;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseOfferingRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.CourseRepository;
import com.app.enrollment.system.enrollment.server.domain.repository.TermRepository;
import java.time.Clock;
import java.util.List;

/**
 * @author Alonso
 */
@UseCase
public class CourseOfferingApplicationService implements CreateCourseOfferingUseCase,
  GetAllCourseOfferingUseCase {
  
  private final CourseRepository courseRepository;
  private final TermRepository termRepository;
  private final CourseOfferingRepository courseOfferingRepository;
  private final CourseOfferingMapper courseOfferingMapper;
  private final CourseMapper courseMapper;
  private final TermMapper termMapper;
  private final Clock clock;
  
  public CourseOfferingApplicationService(CourseRepository courseRepository,
    TermRepository termRepository, CourseOfferingRepository courseOfferingRepository,
    CourseOfferingMapper courseOfferingMapper, CourseMapper courseMapper, TermMapper termMapper, Clock clock) {
    this.courseRepository = courseRepository;
    this.termRepository = termRepository;
    this.courseOfferingRepository = courseOfferingRepository;
    this.courseOfferingMapper = courseOfferingMapper;
    this.courseMapper = courseMapper;
    this.termMapper = termMapper;
    this.clock = clock;
  }
  
  @Override
  public CourseOfferingResponse createCourseOffering(CreateCourseOfferingCommand command) {
    CourseID courseID = new CourseID(command.courseId());
    TermID termID = new TermID(command.termId());
    
    Course course = courseRepository.findById(courseID).orElseThrow(
      () -> new CourseNotFoundException("Course with ID " + command.courseId() + " not found.")
    );
    
    Term term = termRepository.findById(termID).orElseThrow(
      () -> new TermNotFoundException("Term with ID " + command.termId() + " not found.")
    );
    
    CourseOffering courseOffering = CourseOffering.create(
      courseID,
      termID,
      command.sectionCode(),
      command.capacity(),
      clock.instant()
    );
    
    CourseOffering savedCourseOffering = courseOfferingRepository.save(courseOffering);
    
    CourseSummaryResponse courseSummaryResponse = courseMapper.toSummaryResponse(course);
    
    TermResponse termResponse = termMapper.toTermResponse(term);
    
    return courseOfferingMapper.toCourseOfferingResponse(savedCourseOffering, courseSummaryResponse, termResponse);
  }
  
  @Override
  public List<CourseOfferingResponse> getAllCourseOfferings() {
    List<CourseOffering> courseOfferingList = courseOfferingRepository.findAll();
    return courseOfferingList.stream().map(courseOffering -> {
      Course course = courseRepository.findById(courseOffering.getCourseId()).orElseThrow(
        () -> new CourseNotFoundException("Course with ID " + courseOffering.getCourseId().getValue() + " not found.")
      );
      Term term = termRepository.findById(courseOffering.getTermId()).orElseThrow(
        () -> new TermNotFoundException("Term with ID " + courseOffering.getTermId().getValue() + " not found.")
      );
      CourseSummaryResponse courseSummaryResponse = courseMapper.toSummaryResponse(course);
      TermResponse termResponse = termMapper.toTermResponse(term);
      return courseOfferingMapper.toCourseOfferingResponse(courseOffering, courseSummaryResponse, termResponse);
    }).toList();
  }
}
