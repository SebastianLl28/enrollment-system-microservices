package com.app.common.constant;

/**
 * @author Alonso
 */
public class ApiConstants {
  
  public static final String API_PREFIX = "/api/v1";
  
  public static final String[] PUBLIC_ENDPOINTS = {"/auth/login", "/auth/register",
    "/auth/validateToken", "/login/oauth2/code/**", "/oauth2/**", "/auth/2fa/verify",
    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/login/**", "/login/oauth2/**"};
  
  public static final String HEADER_USER_ID = "X-User-Id";
  
  public static final String HEADER_USER_NAME = "X-User-Name";
  
}
