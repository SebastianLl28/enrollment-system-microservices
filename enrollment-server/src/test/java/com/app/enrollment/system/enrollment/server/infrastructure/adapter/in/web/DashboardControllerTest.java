package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.DashboardStatsResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.GetDashboardStatsUseCase;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DashboardController.class)
class DashboardControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private GetDashboardStatsUseCase getDashboardStatsUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void getDashboardStatsReturns200() throws Exception {
    DashboardStatsResponse stats = new DashboardStatsResponse(
      100L, 80L, 20L, 5L, 150L,
      List.of(), null, 30L, List.of(), List.of()
    );
    when(getDashboardStatsUseCase.getDashboardStats()).thenReturn(stats);

    mockMvc.perform(get("/api/v1/dashboard/stats"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalStudents").value(100))
      .andExpect(jsonPath("$.totalEnrollments").value(150));
  }
}
