package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.enrollment.system.enrollment.server.application.dto.response.ErrorResponse;
import com.app.enrollment.system.enrollment.server.domain.exception.CannotChangeStatusOfCancelledEnrollmentException;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNameRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CareerOfferingNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.ClassroomNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.CourseNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyAlreadyExistsException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyLocationRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNameRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.FacultyRequiredException;
import com.app.enrollment.system.enrollment.server.domain.exception.IllegalCourseIDException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCareerOfferingException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCarreerException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidClassroomException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseCodeException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCourseNameException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidCreditsException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDegreeTitleException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDocumentNumberException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidEmailException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidFacultyIDException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSectionException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLengthException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidSemesterLevelException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentAttributeException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentIDException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentNameException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidTermException;
import com.app.enrollment.system.enrollment.server.domain.exception.OverlappingTermException;
import com.app.enrollment.system.enrollment.server.domain.exception.SectionNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentAlreadyEnrolledException;
import com.app.enrollment.system.enrollment.server.domain.exception.StudentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.TermNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Alonso
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // ── 404 Not Found ────────────────────────────────────────────────────────────

  @ExceptionHandler({
      FacultyNotFoundException.class,
      CareerNotFoundException.class,
      CareerOfferingNotFoundException.class,
      ClassroomNotFoundException.class,
      CourseNotFoundException.class,
      EnrollmentNotFoundException.class,
      SectionNotFoundException.class,
      StudentNotFoundException.class,
      TermNotFoundException.class
  })
  public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex,
      HttpServletRequest request) {
    logger.warn("Recurso no encontrado: {}", ex.getMessage());
    return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
  }

  // ── 409 Conflict ─────────────────────────────────────────────────────────────

  @ExceptionHandler({
      FacultyAlreadyExistsException.class,
      StudentAlreadyEnrolledException.class,
      CannotChangeStatusOfCancelledEnrollmentException.class,
      OverlappingTermException.class
  })
  public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex,
      HttpServletRequest request) {
    logger.warn("Conflicto de negocio: {}", ex.getMessage());
    return build(HttpStatus.CONFLICT, ex.getMessage(), request);
  }

  // ── 400 Bad Request (domain validation) ──────────────────────────────────────

  @ExceptionHandler({
      CareerNameRequiredException.class,
      FacultyLocationRequiredException.class,
      FacultyNameRequiredException.class,
      FacultyRequiredException.class,
      IllegalCourseIDException.class,
      InvalidCarreerException.class,
      InvalidCourseCodeException.class,
      InvalidCourseNameException.class,
      InvalidCreditsException.class,
      InvalidDegreeTitleException.class,
      InvalidDocumentNumberException.class,
      InvalidEmailException.class,
      InvalidFacultyIDException.class,
      InvalidSemesterLengthException.class,
      InvalidSemesterLevelException.class,
      InvalidStudentAttributeException.class,
      InvalidStudentIDException.class,
      InvalidStudentNameException.class,
      InvalidTermException.class,
      InvalidCareerOfferingException.class,
      InvalidClassroomException.class,
      InvalidSectionException.class
  })
  public ResponseEntity<ErrorResponse> handleDomainValidation(RuntimeException ex,
      HttpServletRequest request) {
    logger.warn("Error de validación de dominio: {}", ex.getMessage());
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  // ── 400 Bean Validation (@Valid) ─────────────────────────────────────────────

  // ── 400 Request malformado (headers/params requeridos ausentes) ─────────────

  @ExceptionHandler({
      MissingRequestHeaderException.class,
      MissingServletRequestParameterException.class
  })
  public ResponseEntity<ErrorResponse> handleMissingRequestParts(Exception ex,
      HttpServletRequest request) {
    logger.warn("Request malformado: {}", ex.getMessage());
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, String> fieldErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String field = ((FieldError) error).getField();
      fieldErrors.put(field, error.getDefaultMessage());
    });
    logger.warn("Error de validación de entrada: {}", fieldErrors);
    return build(HttpStatus.BAD_REQUEST, fieldErrors.toString(), request);
  }

  // ── 500 Fallback ─────────────────────────────────────────────────────────────

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex,
      HttpServletRequest request) {
    logger.error("Error no controlado", ex);
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno en el servidor",
        request);
  }

  // ─────────────────────────────────────────────────────────────────────────────

  private ResponseEntity<ErrorResponse> build(HttpStatus status, String message,
      HttpServletRequest request) {
    ErrorResponse body = new ErrorResponse(LocalDateTime.now(), status.value(),
        status.getReasonPhrase(), message, request.getRequestURI());
    return new ResponseEntity<>(body, status);
  }
}
