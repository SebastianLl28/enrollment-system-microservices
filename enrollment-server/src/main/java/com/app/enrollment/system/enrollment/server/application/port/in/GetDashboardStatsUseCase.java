package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.DashboardStatsResponse;

/**
 * @author Alonso
 */
public interface GetDashboardStatsUseCase {

  DashboardStatsResponse getDashboardStats();

}
