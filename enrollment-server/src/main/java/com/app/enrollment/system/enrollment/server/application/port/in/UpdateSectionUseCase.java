package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateSectionCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;

/**
 * @author Alonso
 */
public interface UpdateSectionUseCase {

  SectionResponse updateSection(UpdateSectionCommand command, Integer sectionId);

}
