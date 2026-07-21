package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCareerUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCareerUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCareerUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CareerController.class)
class CareerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private GetAllCareerUseCase getAllCareerUseCase;
  @MockBean private CreateCareerUseCase createCareerUseCase;
  @MockBean private UpdateCareerUseCase updateCareerUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listCareersReturnsArray() throws Exception {
    CareerResponse career = new CareerResponse();
    career.setId(1);
    career.setName("Sistemas");
    when(getAllCareerUseCase.findAll(any())).thenReturn(List.of(career));

    mockMvc.perform(get("/api/v1/career"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].name").value("Sistemas"));
  }

  @Test
  void createCareerReturns200() throws Exception {
    CareerResponse career = new CareerResponse();
    career.setId(1);
    career.setName("Sistemas");
    when(createCareerUseCase.createCareer(any())).thenReturn(career);

    mockMvc.perform(post("/api/v1/career")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"facultyId\":1,\"name\":\"Sistemas\",\"semesterLength\":6,\"degreeAwarded\":\"Técnico\"}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Sistemas"));
  }

  @Test
  void missingCareerOnUpdateMapsTo404() throws Exception {
    when(updateCareerUseCase.updateCareer(any(), anyInt()))
      .thenThrow(new CareerNotFoundException("Career not found with id: 99"));

    mockMvc.perform(put("/api/v1/career/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"facultyId\":1,\"name\":\"Sistemas\",\"semesterLength\":6,"
          + "\"degreeAwarded\":\"Técnico\",\"active\":true}"))
      .andExpect(status().isNotFound());
  }

  @Test
  void updateCareerReturns200() throws Exception {
    CareerResponse career = new CareerResponse();
    career.setId(1);
    career.setName("Sistemas Actualizado");
    when(updateCareerUseCase.updateCareer(any(), anyInt())).thenReturn(career);

    mockMvc.perform(put("/api/v1/career/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"facultyId\":1,\"name\":\"Sistemas Actualizado\",\"semesterLength\":6,"
          + "\"degreeAwarded\":\"Técnico\",\"active\":true}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Sistemas Actualizado"));
  }
}
