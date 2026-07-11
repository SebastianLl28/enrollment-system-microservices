package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateSectionCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;

/**
 * @author Alonso
 */
public interface CreateSectionUseCase {

  SectionResponse createSection(CreateSectionCommand command);

}
