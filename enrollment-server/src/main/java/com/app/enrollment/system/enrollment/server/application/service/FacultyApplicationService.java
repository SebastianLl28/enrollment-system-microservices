package com.app.enrollment.system.enrollment.server.application.service;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;
import com.app.enrollment.system.enrollment.server.application.mapper.FacultyMapper;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllFacultyUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyAlreadyExistsException;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import com.app.enrollment.system.enrollment.server.domain.repository.FacultyRepository;
import java.time.Clock;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Alonso
 */
@Service
public class FacultyApplicationService implements GetAllFacultyUseCase, CreateFacultyUseCase {
  
  private final FacultyRepository facultyRepository;
  private final FacultyMapper facultyMapper;
  private final Clock clock;

  public FacultyApplicationService(FacultyRepository facultyRepository, FacultyMapper facultyMapper, Clock clock) {
    this.facultyRepository = facultyRepository;
    this.facultyMapper = facultyMapper;
    this.clock = clock;
  }

  @Override
  public List<FacultyResponse> findAll() {
    List<Faculty> facultyList = facultyRepository.findAll();
    return facultyList.stream().map(facultyMapper::toResponse).toList();
  }

  @Override
  public FacultyResponse createFaculty(CreateFacultyCommand command) {
    Faculty faculty = Faculty.create(command.name(), command.description(), command.location(), command.dean(), clock);
    Faculty savedFaculty = facultyRepository.save(faculty);
    return facultyMapper.toResponse(savedFaculty);
  }

//  @Override
//  public FacultyResponse updateFaculty(UpdateFacultyCommand updateFacultyCommand, Integer facultyId) {
//    Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(() -> NotFoundException.ofFaculty(facultyId));
//    enforceUniqueName(updateFacultyCommand.getName(), facultyId);
//    faculty = faculty.update(updateFacultyCommand.getName(), updateFacultyCommand.getDescription(), updateFacultyCommand.getLocation(), updateFacultyCommand.getDean(), updateFacultyCommand.getActive());
//    Faculty updatedFaculty = facultyRepository.save(faculty);
//    return facultyMapper.toResponse(updatedFaculty);
//  }
}
