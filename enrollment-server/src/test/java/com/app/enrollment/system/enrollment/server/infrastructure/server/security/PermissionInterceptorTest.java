package com.app.enrollment.system.enrollment.server.infrastructure.server.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class PermissionInterceptorTest {

  private static final String PERMISSIONS_HEADER = "X-User-Permissions";

  private PermissionInterceptor interceptor;

  @BeforeEach
  void setUp() {
    // Igual que el ObjectMapper de producción: con soporte de java.time.
    interceptor =
      new PermissionInterceptor(new ObjectMapper().registerModule(new JavaTimeModule()));
  }

  private MockHttpServletRequest request(String method, String uri, String permissions) {
    MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
    if (permissions != null) {
      request.addHeader(PERMISSIONS_HEADER, permissions);
    }
    return request;
  }

  @Test
  void allowsRequestWithMatchingPermission() throws Exception {
    MockHttpServletRequest req =
      request("GET", "/api/v1/enrollment", "ENROLLMENT:READ:ALL");

    boolean allowed = interceptor.preHandle(req, new MockHttpServletResponse(), new Object());

    assertThat(allowed).isTrue();
  }

  @Test
  void deniesRequestWithoutHeader() throws Exception {
    MockHttpServletResponse response = new MockHttpServletResponse();

    boolean allowed =
      interceptor.preHandle(request("POST", "/api/v1/student", null), response, new Object());

    assertThat(allowed).isFalse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    assertThat(response.getContentAsString()).contains("STUDENT:CREATE");
  }

  @Test
  void catalogEndpointsResolveToUiViewResource() throws Exception {
    boolean allowed = interceptor.preHandle(
      request("GET", "/api/v1/career-offering", "UI_VIEW:READ:ALL"),
      new MockHttpServletResponse(), new Object());

    assertThat(allowed).isTrue();
  }

  @Test
  void httpMethodMapsToOperation() throws Exception {
    // PUT exige UPDATE: tener solo READ no basta.
    MockHttpServletResponse response = new MockHttpServletResponse();
    boolean allowed = interceptor.preHandle(
      request("PUT", "/api/v1/enrollment/1", "ENROLLMENT:READ:ALL"), response, new Object());

    assertThat(allowed).isFalse();
    assertThat(response.getContentAsString()).contains("ENROLLMENT:UPDATE");
  }

  @Test
  void optionsRequestsAlwaysPass() throws Exception {
    boolean allowed = interceptor.preHandle(request("OPTIONS", "/api/v1/enrollment", null),
      new MockHttpServletResponse(), new Object());

    assertThat(allowed).isTrue();
  }
}
