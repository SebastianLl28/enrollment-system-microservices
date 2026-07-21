package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetFacultyByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateFacultyUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyAlreadyExistsException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FacultyController.class)
class FacultyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private GetAllFacultyUseCase getAllFacultyUseCase;
  @MockBean private GetFacultyByIdUseCase getFacultyByIdUseCase;
  @MockBean private CreateFacultyUseCase createFacultyUseCase;
  @MockBean private UpdateFacultyUseCase updateFacultyUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listFacultiesReturnsArray() throws Exception {
    FacultyResponse faculty = new FacultyResponse();
    faculty.setId(1);
    faculty.setName("Ingeniería");
    when(getAllFacultyUseCase.findAll(any())).thenReturn(List.of(faculty));

    mockMvc.perform(get("/api/v1/faculty"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].name").value("Ingeniería"));
  }

  @Test
  void getFacultyByIdReturns200() throws Exception {
    FacultyResponse faculty = new FacultyResponse();
    faculty.setId(1);
    faculty.setName("Ingeniería");
    when(getFacultyByIdUseCase.findById(1)).thenReturn(faculty);

    mockMvc.perform(get("/api/v1/faculty/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void missingFacultyMapsTo404() throws Exception {
    when(getFacultyByIdUseCase.findById(99))
      .thenThrow(new FacultyNotFoundException("Faculty not found with id: 99"));

    mockMvc.perform(get("/api/v1/faculty/99"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Faculty not found with id: 99"));
  }

  @Test
  void createFacultyReturns200() throws Exception {
    FacultyResponse faculty = new FacultyResponse();
    faculty.setId(1);
    faculty.setName("Ingeniería");
    when(createFacultyUseCase.createFaculty(any())).thenReturn(faculty);

    mockMvc.perform(post("/api/v1/faculty")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Ingeniería\",\"location\":\"Campus A\",\"dean\":\"Dr. Smith\"}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Ingeniería"));
  }

  @Test
  void duplicateFacultyMapsTo409() throws Exception {
    when(createFacultyUseCase.createFaculty(any()))
      .thenThrow(new FacultyAlreadyExistsException("Faculty already exists: Ingeniería"));

    mockMvc.perform(post("/api/v1/faculty")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Ingeniería\",\"location\":\"Campus A\",\"dean\":\"Dr. Smith\"}"))
      .andExpect(status().isConflict());
  }

  @Test
  void updateFacultyReturns200() throws Exception {
    FacultyResponse faculty = new FacultyResponse();
    faculty.setId(1);
    faculty.setName("Ingeniería Actualizada");
    when(updateFacultyUseCase.updateFaculty(any(), anyInt())).thenReturn(faculty);

    mockMvc.perform(put("/api/v1/faculty/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Ingeniería Actualizada\",\"location\":\"Campus B\","
          + "\"dean\":\"Dr. Jones\",\"active\":true}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Ingeniería Actualizada"));
  }
}
