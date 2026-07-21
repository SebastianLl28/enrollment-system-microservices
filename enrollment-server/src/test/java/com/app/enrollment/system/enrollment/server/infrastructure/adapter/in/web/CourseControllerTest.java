package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCourseUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CourseController.class)
class CourseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean private GetAllCourseUseCase getAllCourseUseCase;
  @MockBean private CreateCourseUseCase createCourseUseCase;
  @MockBean private UpdateCourseUseCase updateCourseUseCase;
  @MockBean private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void listCoursesReturnsArray() throws Exception {
    CourseResponse course = new CourseResponse();
    course.setId(1);
    course.setName("Programación I");
    course.setCode("CS101");
    when(getAllCourseUseCase.findAll()).thenReturn(List.of(course));

    mockMvc.perform(get("/api/v1/course"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].code").value("CS101"));
  }

  @Test
  void createCourseReturns200() throws Exception {
    CourseResponse course = new CourseResponse();
    course.setId(1);
    course.setCode("CS101");
    course.setName("Programación I");
    when(createCourseUseCase.createCourse(any())).thenReturn(course);

    mockMvc.perform(post("/api/v1/course")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"CS101\",\"name\":\"Programación I\",\"credits\":4,"
          + "\"careers\":[{\"careerId\":1,\"semesterLevel\":1}]}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.code").value("CS101"));
  }

  @Test
  void missingCourseOnUpdateMapsTo404() throws Exception {
    when(updateCourseUseCase.updateCourse(any(), anyInt()))
      .thenThrow(new CourseNotFoundException("Course not found with id: 99"));

    mockMvc.perform(put("/api/v1/course/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"CS101\",\"name\":\"Programación I\",\"credits\":4,"
          + "\"careers\":[{\"careerId\":1,\"semesterLevel\":1}]}"))
      .andExpect(status().isNotFound());
  }
}
