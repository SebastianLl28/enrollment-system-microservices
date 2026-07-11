package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;

/**
 * @author Alonso
 */
public interface CreateClassroomUseCase {

  ClassroomResponse createClassroom(CreateClassroomCommand command);

}
