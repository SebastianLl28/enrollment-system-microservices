package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.domain.model.CareerOffering;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CareerOfferingMapper {

  public CareerOfferingResponse toCareerOfferingResponse(CareerOffering careerOffering,
    CareerSummaryResponse careerSummaryResponse, TermResponse termResponse) {
    return new CareerOfferingResponse(
      careerOffering.getId().getValue(),
      careerSummaryResponse,
      termResponse,
      careerOffering.getCapacity(),
      careerOffering.getEnrolledCount(),
      careerOffering.isActive(),
      careerOffering.getCreatedAt(),
      careerOffering.getPrice()
    );
  }

}
