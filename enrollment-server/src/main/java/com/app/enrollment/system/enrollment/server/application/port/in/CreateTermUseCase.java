package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateTermCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;

/**
 * @author Alonso
 */
public interface CreateTermUseCase {

  TermResponse createTerm(CreateTermCommand command);

}
