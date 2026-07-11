package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;

/**
 * @author Alonso
 */
public interface UpdateClassroomUseCase {

  ClassroomResponse updateClassroom(UpdateClassroomCommand command, Integer classroomId);

}
