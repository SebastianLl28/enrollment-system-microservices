package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerOfferingNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CareerOfferingController.class)
class CareerOfferingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private CreateCareerOfferingUseCase createCareerOfferingUseCase;
  @MockBean private GetAllCareerOfferingUseCase getAllCareerOfferingUseCase;
  @MockBean private UpdateCareerOfferingUseCase updateCareerOfferingUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listCareerOfferingsReturnsArray() throws Exception {
    CareerOfferingResponse offering = new CareerOfferingResponse();
    offering.setId(1);
    offering.setCapacity(50);
    offering.setPrice(BigDecimal.valueOf(500));
    when(getAllCareerOfferingUseCase.getAllCareerOfferings()).thenReturn(List.of(offering));

    mockMvc.perform(get("/api/v1/career-offering"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].capacity").value(50));
  }

  @Test
  void createCareerOfferingReturns200() throws Exception {
    CareerOfferingResponse offering = new CareerOfferingResponse();
    offering.setId(1);
    offering.setCapacity(50);
    offering.setPrice(BigDecimal.valueOf(500));
    when(createCareerOfferingUseCase.createCareerOffering(any())).thenReturn(offering);

    mockMvc.perform(post("/api/v1/career-offering")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"careerId\":1,\"termId\":1,\"capacity\":50,\"price\":500.00}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.capacity").value(50));
  }

  @Test
  void missingCareerOfferingOnUpdateMapsTo404() throws Exception {
    when(updateCareerOfferingUseCase.updateCareerOffering(any(), anyInt()))
      .thenThrow(new CareerOfferingNotFoundException("CareerOffering not found with id: 99"));

    mockMvc.perform(put("/api/v1/career-offering/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"careerId\":1,\"termId\":1,\"capacity\":50,\"active\":true,\"price\":500.00}"))
      .andExpect(status().isNotFound());
  }
}
