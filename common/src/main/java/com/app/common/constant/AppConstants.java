package com.app.common.constant;

import java.util.List;

/**
 * @author Alonso
 */
public class AppConstants {
  
  // constructor to prevent instantiation
  private AppConstants() {
    throw new IllegalStateException("Utility class");
  }
  
  public static final List<String> ALLOWED_ORIGINS = List.of(
      "http://localhost:5173",
      "https://course-hub.dev",
      "https://www.course-hub.dev"
  );

}
