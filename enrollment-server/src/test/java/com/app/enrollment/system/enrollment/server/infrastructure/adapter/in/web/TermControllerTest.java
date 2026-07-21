package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateTermUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllTermUserCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateTermUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.OverlappingTermException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TermController.class)
class TermControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private GetAllTermUserCase getAllTermUserCase;
  @MockBean private CreateTermUseCase createTermUseCase;
  @MockBean private UpdateTermUseCase updateTermUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listTermsReturnsArray() throws Exception {
    TermResponse term = new TermResponse();
    term.setId(1);
    term.setCode("2024-I");
    when(getAllTermUserCase.getAllTerms()).thenReturn(List.of(term));

    mockMvc.perform(get("/api/v1/term"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].code").value("2024-I"));
  }

  @Test
  void createTermReturns200() throws Exception {
    TermResponse term = new TermResponse();
    term.setId(1);
    term.setCode("2024-I");
    when(createTermUseCase.createTerm(any())).thenReturn(term);

    mockMvc.perform(post("/api/v1/term")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"2024-I\",\"startDate\":\"2024-03-01\","
          + "\"endDate\":\"2024-07-31\",\"active\":true}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value("2024-I"));
  }

  @Test
  void overlappingTermMapsTo409() throws Exception {
    when(createTermUseCase.createTerm(any()))
      .thenThrow(new OverlappingTermException("Term dates overlap with existing term"));

    mockMvc.perform(post("/api/v1/term")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"2024-I\",\"startDate\":\"2024-03-01\","
          + "\"endDate\":\"2024-07-31\",\"active\":true}"))
      .andExpect(status().isConflict());
  }

  @Test
  void missingTermOnUpdateMapsTo404() throws Exception {
    when(updateTermUseCase.updateTerm(any(), anyInt()))
      .thenThrow(new TermNotFoundException("Term not found with id: 99"));

    mockMvc.perform(put("/api/v1/term/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"2024-I\",\"startDate\":\"2024-03-01\","
          + "\"endDate\":\"2024-07-31\",\"active\":true}"))
      .andExpect(status().isNotFound());
  }
}
