package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;

/**
 * @author Alonso
 */
public interface CreateCareerOfferingUseCase {

  CareerOfferingResponse createCareerOffering(CreateCareerOfferingCommand command);

}
