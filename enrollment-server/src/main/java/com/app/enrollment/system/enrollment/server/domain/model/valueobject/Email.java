package com.app.enrollment.system.enrollment.server.domain.model.valueobject;


import com.app.enrollment.system.enrollment.server.domain.exception.InvalidEmailException;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {
  private static final Pattern SIMPLE_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

  private final String value;

  public Email(String value) {
    if (value == null) throw new InvalidEmailException("email es obligatorio");
    String normalized = value.trim().toLowerCase();
    if (normalized.isEmpty()) throw new InvalidEmailException("email vacío");
    if (!SIMPLE_EMAIL.matcher(normalized).matches()) {
      throw new InvalidEmailException("email inválido: " + value);
    }
    this.value = normalized;
  }

  public String getValue() {
    return value;
  }

  public static Email of(String raw) {
    return new Email(raw);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Email email)) return false;
    return value.equals(email.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
