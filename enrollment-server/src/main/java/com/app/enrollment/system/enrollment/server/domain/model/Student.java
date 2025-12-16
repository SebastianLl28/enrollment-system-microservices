package com.app.enrollment.system.enrollment.server.domain.model;

import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDocumentNumberException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentAttributeException;
import com.app.enrollment.system.enrollment.server.domain.exception.InvalidStudentNameException;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.DocumentNumber;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.Email;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.StudentID;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Alonso
 */
public class Student {
  
  private final StudentID id;
  private final DocumentNumber documentNumber;
  private final String name;
  private final String lastName;
  private final Email email;
  private final String phoneNumber;
  private final LocalDate dateOfBirth;
  private final String address;
  private final Instant createdAt;
  private final boolean active;
  
  private Student(StudentID id, DocumentNumber documentNumber, String name, String lastName,
    Email email, String phoneNumber, LocalDate dateOfBirth, String address, Instant createdAt,
    boolean active) {
    this.id = id;
    this.documentNumber = documentNumber;
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
    this.createdAt = createdAt;
    this.active = active;
  }
  
  public static Student create(String name, String lastName, DocumentNumber documentNumber,
    Email email, String phoneNumber, LocalDate dateOfBirth, String address, Instant now) {
    Objects.requireNonNull(now, "clock");
    validateRequired(name, lastName, documentNumber, dateOfBirth);
    return new Student(null, documentNumber, name, lastName, email, phoneNumber, dateOfBirth,
      address, now, true);
  }
  
  private static void validateRequired(String name, String lastName, DocumentNumber documentNumber,
    LocalDate dateOfBirth) {
    if (isBlank(name)) {
      throw new InvalidStudentNameException("name es obligatorio.");
    }
    if (isBlank(lastName)) {
      throw new InvalidStudentNameException("lastName es obligatorio.");
    }
    
    if (documentNumber == null) {
      throw new InvalidDocumentNumberException("documentNumber es obligatorio.");
    }
    
    if (dateOfBirth == null) {
      throw new InvalidStudentAttributeException("dateOfBirth es obligatorio.");
    }
  }
  
  public static Student rehydrate(StudentID studentID, String name, String lastName,
    DocumentNumber documentNumber, Email email, String phoneNumber, LocalDate dateOfBirth,
    String address, Instant instant, Boolean active) {
    return new Student(studentID, documentNumber, name, lastName, email, phoneNumber,
      dateOfBirth, address, instant, active);
  }
  
  public String getFullName() {
    return String.format("%s %s", name, lastName);
  }
  
  public StudentID getId() {
    return id;
  }
  
  public DocumentNumber getDocumentNumber() {
    return documentNumber;
  }
  
  public String getName() {
    return name;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public Email getEmail() {
    return email;
  }
  
  public String getPhoneNumber() {
    return phoneNumber;
  }
  
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }
  
  public String getAddress() {
    return address;
  }
  
  public Instant getCreatedAt() {
    return createdAt;
  }
  
  public boolean isActive() {
    return active;
  }
  
  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
  
  private static String trim(String s) {
    if (s == null) {
      throw new InvalidStudentAttributeException("valor nulo no permitido");
    }
    return s.trim();
  }
  
  private static String trimOrNull(String s) {
    return s == null ? null : s.trim();
  }
}
