package com.app.authorization.server.application.port.in;

import com.app.authorization.server.application.dto.command.CreateUIViewCommand;
import com.app.authorization.server.application.dto.command.UpdateUIViewCommand;
import com.app.authorization.server.application.dto.response.UIViewResponse;
import java.util.List;

/**
 * @author Alonso
 */
public interface RbacViewUseCase {
  
  UIViewResponse createView(CreateUIViewCommand command);
  
  UIViewResponse updateView(UpdateUIViewCommand command);
  
  void deleteView(String code);
  
  UIViewResponse getView(String code);
  
  List<UIViewResponse> listViews();

}
