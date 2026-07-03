package com.app.enrollment.system.enrollment.server.infrastructure.server.security;

import com.app.enrollment.system.enrollment.server.application.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Valida el header X-User-Permissions (inyectado por el api-gateway tras validar el JWT)
 * contra el recurso y la operación del request. El gateway es la única fuente confiable
 * de este header: lo sobreescribe en cada request antes de enrutar.
 *
 * @author Alonso
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);

  private static final String PERMISSIONS_HEADER = "X-User-Permissions";

  private final ObjectMapper objectMapper;

  public PermissionInterceptor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }

    String resource = resolveResource(request.getRequestURI());
    String operation = resolveOperation(request.getMethod());
    String requiredPrefix = resource + ":" + operation + ":";

    Set<String> permissions = extractPermissions(request);

    boolean allowed = permissions.stream().anyMatch(p -> p.startsWith(requiredPrefix));

    if (!allowed) {
      logger.warn("Access denied: user lacks {}{} for {} {}", resource, ":" + operation,
          request.getMethod(), request.getRequestURI());
      writeForbidden(request, response, resource, operation);
      return false;
    }

    return true;
  }

  private Set<String> extractPermissions(HttpServletRequest request) {
    String header = request.getHeader(PERMISSIONS_HEADER);
    if (header == null || header.isBlank()) {
      return Set.of();
    }
    return Arrays.stream(header.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toSet());
  }

  private String resolveResource(String uri) {
    if (uri.startsWith("/api/v1/student")) {
      return "STUDENT";
    }
    if (uri.startsWith("/api/v1/enrollment")) {
      return "ENROLLMENT";
    }
    // Catálogo académico (faculty, career, course, term, course-offering):
    // protegido con el recurso UI_VIEW, igual que en routeProtection del frontend.
    return "UI_VIEW";
  }

  private String resolveOperation(String method) {
    return switch (method.toUpperCase()) {
      case "POST" -> "CREATE";
      case "PUT", "PATCH" -> "UPDATE";
      case "DELETE" -> "DELETE";
      default -> "READ";
    };
  }

  private void writeForbidden(HttpServletRequest request, HttpServletResponse response,
      String resource, String operation) throws Exception {
    ErrorResponse body = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.FORBIDDEN.value(),
        HttpStatus.FORBIDDEN.getReasonPhrase(),
        "No tienes permiso para realizar esta acción (" + resource + ":" + operation + ")",
        request.getRequestURI()
    );
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}
