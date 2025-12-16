package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;

/**
 * @author Alonso
 */
public interface CreateCareerUseCase {

  CareerResponse createCareer(CreateCareerCommand command);

}
