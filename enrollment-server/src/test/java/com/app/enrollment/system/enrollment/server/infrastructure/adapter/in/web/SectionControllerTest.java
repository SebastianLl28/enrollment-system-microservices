package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateSectionUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllSectionUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateSectionUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.SectionNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SectionController.class)
class SectionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private CreateSectionUseCase createSectionUseCase;
  @MockBean private GetAllSectionUseCase getAllSectionUseCase;
  @MockBean private UpdateSectionUseCase updateSectionUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listSectionsReturnsArray() throws Exception {
    SectionResponse section = new SectionResponse();
    section.setId(1);
    section.setSectionCode("SEC-A");
    when(getAllSectionUseCase.getAllSections()).thenReturn(List.of(section));

    mockMvc.perform(get("/api/v1/section"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].sectionCode").value("SEC-A"));
  }

  @Test
  void createSectionReturns200() throws Exception {
    SectionResponse section = new SectionResponse();
    section.setId(1);
    section.setSectionCode("SEC-A");
    when(createSectionUseCase.createSection(any())).thenReturn(section);

    mockMvc.perform(post("/api/v1/section")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"courseId\":1,\"termId\":1,\"classroomId\":1,\"sectionCode\":\"SEC-A\"}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.sectionCode").value("SEC-A"));
  }

  @Test
  void missingSectionOnUpdateMapsTo404() throws Exception {
    when(updateSectionUseCase.updateSection(any(), anyInt()))
      .thenThrow(new SectionNotFoundException("Section not found with id: 99"));

    mockMvc.perform(put("/api/v1/section/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"courseId\":1,\"termId\":1,\"classroomId\":1,\"sectionCode\":\"SEC-A\",\"active\":true}"))
      .andExpect(status().isNotFound());
  }

  @Test
  void updateSectionReturns200() throws Exception {
    SectionResponse section = new SectionResponse();
    section.setId(1);
    section.setSectionCode("SEC-B");
    when(updateSectionUseCase.updateSection(any(), anyInt())).thenReturn(section);

    mockMvc.perform(put("/api/v1/section/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"courseId\":1,\"termId\":1,\"classroomId\":2,\"sectionCode\":\"SEC-B\",\"active\":true}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.sectionCode").value("SEC-B"));
  }
}
