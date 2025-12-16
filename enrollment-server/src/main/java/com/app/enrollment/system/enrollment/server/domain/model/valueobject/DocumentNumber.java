package com.app.enrollment.system.enrollment.server.domain.model.valueobject;


import com.app.enrollment.system.enrollment.server.domain.exception.InvalidDocumentNumberException;
import java.util.Objects;
import java.util.regex.Pattern;

public final class DocumentNumber {

  private static final Pattern ALNUM = Pattern.compile("^[A-Z0-9]{8,20}$");
  private final String value;

  public DocumentNumber(String value) {
    if (value == null) throw new InvalidDocumentNumberException("documentNumber es obligatorio");
    String normalized = value.trim().replace(" ", "").toUpperCase();
    if (!ALNUM.matcher(normalized).matches()) {
      throw new InvalidDocumentNumberException("documentNumber inválido (8-20 alfanumérico): " + value);
    }
    this.value = normalized;
  }

  public String getValue() {
    return value;
  }

  public static DocumentNumber of(String raw) {
    return new DocumentNumber(raw);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DocumentNumber that)) return false;
    return value.equals(that.value);
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
