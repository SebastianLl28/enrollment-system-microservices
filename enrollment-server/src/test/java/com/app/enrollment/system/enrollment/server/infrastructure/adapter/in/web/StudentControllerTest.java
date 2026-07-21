package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetStudentByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateStudentUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private GetAllStudentUseCase getAllStudentUseCase;
  @MockBean private CreateStudentUseCase createStudentUseCase;
  @MockBean private GetStudentByIdUseCase getStudentByIdUseCase;
  @MockBean private UpdateStudentUseCase updateStudentUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listStudentsReturnsArray() throws Exception {
    StudentResponse student = new StudentResponse();
    student.setId(1);
    student.setName("Ana");
    student.setLastName("García");
    when(getAllStudentUseCase.findAll()).thenReturn(List.of(student));

    mockMvc.perform(get("/api/v1/student"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].name").value("Ana"));
  }

  @Test
  void getStudentByIdReturns200() throws Exception {
    StudentResponse student = new StudentResponse();
    student.setId(1);
    student.setName("Ana");
    when(getStudentByIdUseCase.findById(1)).thenReturn(student);

    mockMvc.perform(get("/api/v1/student/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void missingStudentMapsTo404() throws Exception {
    when(getStudentByIdUseCase.findById(99))
      .thenThrow(new StudentNotFoundException("Student not found with id: 99"));

    mockMvc.perform(get("/api/v1/student/99"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Student not found with id: 99"));
  }

  @Test
  void createStudentReturns200() throws Exception {
    StudentResponse student = new StudentResponse();
    student.setId(1);
    student.setName("Ana");
    when(createStudentUseCase.createStudent(any())).thenReturn(student);

    mockMvc.perform(post("/api/v1/student")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Ana\",\"lastName\":\"García\",\"email\":\"ana@example.com\","
          + "\"documentNumber\":\"12345678\",\"birthDate\":\"2000-01-15\"}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Ana"));
  }

  @Test
  void updateStudentReturns200() throws Exception {
    StudentResponse student = new StudentResponse();
    student.setId(1);
    student.setName("Ana Actualizada");
    when(updateStudentUseCase.updateStudent(any(), anyInt())).thenReturn(student);

    mockMvc.perform(put("/api/v1/student/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Ana Actualizada\",\"lastName\":\"García\","
          + "\"email\":\"ana@example.com\",\"documentNumber\":\"12345678\","
          + "\"birthDate\":\"2000-01-15\",\"active\":true}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Ana Actualizada"));
  }
}
