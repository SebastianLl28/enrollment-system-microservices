package com.app.enrollment.system.enrollment.server.application.service;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.FacultyMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetFacultyByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateFacultyUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyAlreadyExistsException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.FacultyID;
import com.app.enrollment.system.enrollment.server.domain.repository.FacultyRepository;
import java.time.Clock;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Alonso
 */
@Service
public class FacultyApplicationService implements GetAllFacultyUseCase, CreateFacultyUseCase,
    UpdateFacultyUseCase, GetFacultyByIdUseCase {
  
  private final FacultyRepository facultyRepository;
  private final FacultyMapper facultyMapper;
  private final Clock clock;
  
  public FacultyApplicationService(FacultyRepository facultyRepository, FacultyMapper facultyMapper,
      Clock clock) {
    this.facultyRepository = facultyRepository;
    this.facultyMapper = facultyMapper;
    this.clock = clock;
  }
  
  @Override
  public List<FacultyResponse> findAll(Boolean includeInactive) {
    List<Faculty> facultyList = includeInactive ? facultyRepository.findAll() : facultyRepository.findAllActive();
    return facultyList.stream().map(facultyMapper::toResponse).toList();
  }
  
  @Override
  public FacultyResponse findById(Integer id) {
    FacultyID facultyID = new FacultyID(id);
    Faculty faculty = facultyRepository.findById(facultyID).orElseThrow(() ->
        new FacultyNotFoundException("Faculty with ID " + id + " not found"));
    return facultyMapper.toResponse(faculty);
  }

  @Override
  public FacultyResponse createFaculty(CreateFacultyCommand command) {
    Faculty faculty = Faculty.create(command.name(), command.description(), command.location(),
        command.dean(), clock);
    Faculty savedFaculty = facultyRepository.save(faculty);
    return facultyMapper.toResponse(savedFaculty);
  }
  
  @Override
  public FacultyResponse updateFaculty(UpdateFacultyCommand updateFacultyCommand,
      Integer facultyId) {
    
    FacultyID facultyID = new FacultyID(facultyId);
    
    Faculty faculty = facultyRepository.findById(facultyID).orElseThrow(() ->
        new FacultyNotFoundException("Faculty with ID " + facultyId + " not found"));
    
    faculty.rename(updateFacultyCommand.name());
    faculty.relocate(updateFacultyCommand.location());
    faculty.assignDean(updateFacultyCommand.dean());
    
    if (updateFacultyCommand.active()) {
      faculty.activate();
    } else {
      faculty.deactivate();
    }
    
    Faculty updatedFaculty = facultyRepository.save(faculty);
    
    return facultyMapper.toResponse(updatedFaculty);
    
  }
  
}
