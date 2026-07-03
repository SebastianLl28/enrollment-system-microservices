package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateTermCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;

/**
 * @author Alonso
 */
public interface UpdateTermUseCase {
  TermResponse updateTerm(UpdateTermCommand command, Integer termId);
}
