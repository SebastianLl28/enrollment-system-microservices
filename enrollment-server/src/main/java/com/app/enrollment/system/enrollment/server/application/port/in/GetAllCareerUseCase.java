package com.app.enrollment.system.enrollment.server.application.port.in;


import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllCareerUseCase {
  
  List<CareerResponse> findAll();
  
}
