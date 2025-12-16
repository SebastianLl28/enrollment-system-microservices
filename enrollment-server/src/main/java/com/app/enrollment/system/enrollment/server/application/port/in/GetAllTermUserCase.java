package com.app.enrollment.system.enrollment.server.application.port.in;

import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface GetAllTermUserCase {
  
  List<TermResponse> getAllTerms();
  
}
