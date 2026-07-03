package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCareerCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;

/**
 * @author Alonso
 */
public interface UpdateCareerUseCase {
  CareerResponse updateCareer(UpdateCareerCommand command, Integer careerId);
}
