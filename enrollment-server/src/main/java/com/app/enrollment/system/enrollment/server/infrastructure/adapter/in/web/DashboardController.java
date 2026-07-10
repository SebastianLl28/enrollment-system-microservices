package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.response.DashboardStatsResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.GetDashboardStatsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alonso
 */
@RestController
@RequestMapping(ApiConstants.API_PREFIX)
public class DashboardController {

  private final GetDashboardStatsUseCase getDashboardStatsUseCase;

  public DashboardController(GetDashboardStatsUseCase getDashboardStatsUseCase) {
    this.getDashboardStatsUseCase = getDashboardStatsUseCase;
  }

  @GetMapping("/dashboard/stats")
  public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
    return ResponseEntity.ok(getDashboardStatsUseCase.getDashboardStats());
  }

}
