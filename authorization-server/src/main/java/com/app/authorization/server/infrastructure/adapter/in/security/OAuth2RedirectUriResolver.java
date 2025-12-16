package com.app.authorization.server.infrastructure.adapter.in.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class OAuth2RedirectUriResolver implements OAuth2AuthorizationRequestResolver {
  
  private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;
  private final String publicBaseUrl;
  
  
  public OAuth2RedirectUriResolver(
    ClientRegistrationRepository clientRegistrationRepository,
    @Value("${oauth2.public-base-url}") String publicBaseUrl) {
    this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
      clientRegistrationRepository,
      "/oauth2/authorization"
    );
    this.publicBaseUrl = publicBaseUrl;
  }
  
  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
    return customizeAuthorizationRequest(authorizationRequest);
  }
  
  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request,
    String clientRegistrationId) {
    OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
    return customizeAuthorizationRequest(authorizationRequest);
  }
  
  private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
    if (authorizationRequest == null) {
      return null;
    }
    
    String originalRedirectUri = authorizationRequest.getRedirectUri();
    
    String customRedirectUri = originalRedirectUri
      .replaceFirst("http://[^/]+", publicBaseUrl)
      .replaceFirst("https://[^/]+", publicBaseUrl);
    
    return OAuth2AuthorizationRequest
      .from(authorizationRequest)
      .redirectUri(customRedirectUri)
      .build();
  }
}
