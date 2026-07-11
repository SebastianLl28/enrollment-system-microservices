package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllCareerOfferingUseCase {

  List<CareerOfferingResponse> getAllCareerOfferings();

}
