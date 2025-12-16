package com.app.enrollment.system.enrollment.server.application.service;

import com.app.common.annotation.UseCase;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.CareerMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCareerUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCareerUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DegreeTitle;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
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
public class CareerApplicationService implements GetAllCareerUseCase, CreateCareerUseCase {
  
  private final CareerRepository careerRepository;
  private final FacultyRepository facultyRepository;
  private final CareerMapper careerMapper;
  private final Clock clock;
  private final CourseRepository courseRepository;
  
  public CareerApplicationService(CareerRepository careerRepository, CareerMapper careerMapper,
    FacultyRepository facultyRepository, Clock clock, CourseRepository courseRepository) {
    this.careerRepository = careerRepository;
    this.careerMapper = careerMapper;
    this.facultyRepository = facultyRepository;
    this.clock = clock;
    this.courseRepository = courseRepository;
  }
  
  @Override
  public List<CareerResponse> findAll() {
    
    List<Career> careerList = careerRepository.findAll();
    
    Set<Integer> facultyIds = careerList.stream().map(c -> c.getFacultyId().getValue())
      .collect(Collectors.toSet());
    
    List<Faculty> facultyMap = facultyRepository.findAllByIdIn(facultyIds);
    
    return careerList.stream().map(career -> {
      
      Faculty faculty = facultyMap.stream()
        .filter(f -> f.getId().getValue().equals(career.getFacultyId().getValue())).findFirst()
        .orElseThrow();
      
      List<Course> courseList = courseRepository.findByCareerIdWithActiveCourses(
        career.getId().getValue());
      
      return careerMapper.toResponse(career, faculty, courseList);
    }).toList();
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
    
    List<Course> courseList = courseRepository.findByCareerIdWithActiveCourses(
      savedCareer.getId().getValue());
    
    return careerMapper.toResponse(savedCareer, faculty, courseList);
  }
}
