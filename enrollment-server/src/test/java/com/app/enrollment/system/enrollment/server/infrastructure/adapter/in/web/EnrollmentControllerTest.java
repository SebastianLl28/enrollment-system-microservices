package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PageResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllEnrollmentCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetEnrollmentByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UnenrollStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentAlreadyEnrolledException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EnrollmentController.class)
class EnrollmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CreateEnrollmentUseCase createEnrollmentUseCase;
  @MockBean
  private GetAllEnrollmentCourseUseCase getAllEnrollmentCourseUseCase;
  @MockBean
  private GetEnrollmentByIdUseCase getEnrollmentByIdUseCase;
  @MockBean
  private UnenrollStudentUseCase unenrollStudentUseCase;
  @MockBean
  private UpdateEnrollmentUseCase updateEnrollmentUseCase;

  /**
   * WebMvcConfig registra el PermissionInterceptor en /api/**: se mockea para
   * que los tests de controller no dependan del header de permisos (el
   * interceptor tiene su propio test unitario).
   */
  @MockBean
  private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listEnrollmentsReturnsPage() throws Exception {
    when(getAllEnrollmentCourseUseCase.getAllEnrollmentCourses(any()))
      .thenReturn(new PageResponse<>(List.<EnrollmentResponse>of(), 0, 10, 0, 0));

    mockMvc.perform(get("/api/v1/enrollment"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.totalElements").value(0));
  }

  @Test
  void missingEnrollmentMapsTo404() throws Exception {
    when(getEnrollmentByIdUseCase.getEnrollmentById(anyInt(), any()))
      .thenThrow(new EnrollmentNotFoundException("Enrollment not found with id: 99"));

    mockMvc.perform(get("/api/v1/enrollment/99").header("X-User-Id", 1))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Enrollment not found with id: 99"));
  }

  @Test
  void duplicateEnrollmentMapsTo409() throws Exception {
    when(createEnrollmentUseCase.createEnrollment(any(), anyInt()))
      .thenThrow(new StudentAlreadyEnrolledException("already enrolled"));

    mockMvc.perform(post("/api/v1/enrollment")
        .header("X-User-Id", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"studentId\":1,\"careerOfferingId\":1}"))
      .andExpect(status().isConflict());
  }

  @Test
  void missingUserIdHeaderMapsToClientError() throws Exception {
    mockMvc.perform(post("/api/v1/enrollment")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"studentId\":1,\"careerOfferingId\":1}"))
      .andExpect(status().is4xxClientError());
  }
}
