package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;

/**
 * @author Alonso
 */
public interface UpdateCareerOfferingUseCase {
  CareerOfferingResponse updateCareerOffering(UpdateCareerOfferingCommand command,
    Integer careerOfferingId);
}
