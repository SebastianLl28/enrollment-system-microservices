package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateClassroomUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllClassroomUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateClassroomUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.ClassroomNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ClassroomController.class)
class ClassroomControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private CreateClassroomUseCase createClassroomUseCase;
  @MockBean private GetAllClassroomUseCase getAllClassroomUseCase;
  @MockBean private UpdateClassroomUseCase updateClassroomUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listClassroomsReturnsArray() throws Exception {
    ClassroomResponse classroom = new ClassroomResponse();
    classroom.setId(1);
    classroom.setCode("A101");
    when(getAllClassroomUseCase.getAllClassrooms()).thenReturn(List.of(classroom));

    mockMvc.perform(get("/api/v1/classroom"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].code").value("A101"));
  }

  @Test
  void createClassroomReturns200() throws Exception {
    ClassroomResponse classroom = new ClassroomResponse();
    classroom.setId(1);
    classroom.setCode("A101");
    classroom.setVirtual(false);
    when(createClassroomUseCase.createClassroom(any())).thenReturn(classroom);

    mockMvc.perform(post("/api/v1/classroom")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"A101\",\"capacity\":30,\"virtual\":false}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value("A101"));
  }

  @Test
  void missingClassroomOnUpdateMapsTo404() throws Exception {
    when(updateClassroomUseCase.updateClassroom(any(), anyInt()))
      .thenThrow(new ClassroomNotFoundException("Classroom not found with id: 99"));

    mockMvc.perform(put("/api/v1/classroom/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"A101\",\"capacity\":30,\"virtual\":false,\"active\":true}"))
      .andExpect(status().isNotFound());
  }

  @Test
  void updateClassroomReturns200() throws Exception {
    ClassroomResponse classroom = new ClassroomResponse();
    classroom.setId(1);
    classroom.setCode("A101");
    classroom.setVirtual(false);
    when(updateClassroomUseCase.updateClassroom(any(), anyInt())).thenReturn(classroom);

    mockMvc.perform(put("/api/v1/classroom/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"A101\",\"capacity\":30,\"virtual\":false,\"active\":true}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value("A101"));
  }
}
