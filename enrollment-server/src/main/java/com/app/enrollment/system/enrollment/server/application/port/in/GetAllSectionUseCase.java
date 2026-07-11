package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllSectionUseCase {

  List<SectionResponse> getAllSections();

}
