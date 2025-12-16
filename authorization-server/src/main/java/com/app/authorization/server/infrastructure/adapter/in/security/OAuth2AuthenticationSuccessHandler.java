package com.app.authorization.server.infrastructure.adapter.in.security;

import com.app.authorization.server.application.dto.command.OAuthRegisterCommand;
import com.app.authorization.server.application.dto.response.UserResponse;
import com.app.authorization.server.application.port.in.FindOrCreateOauthUserUseCase;
import com.app.authorization.server.domain.exception.UserNotFoundException;
import com.app.authorization.server.domain.model.User;
import com.app.authorization.server.domain.model.UserAccessProfile;
import com.app.authorization.server.domain.repository.AccessTokenPort;
import com.app.authorization.server.domain.repository.UserAccessProfilePort;
import com.app.authorization.server.domain.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Alonso
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  
  @Value("${frontend.base-url}")
  private String frontendBaseUrl;
  
  private final AccessTokenPort accessTokenPort;
  private final FindOrCreateOauthUserUseCase findOrCreateOAuthUserUseCase;
  private final UserRepository userRepository;
  private final UserAccessProfilePort userAccessProfilePort;
  
  public OAuth2AuthenticationSuccessHandler(
    AccessTokenPort accessTokenPort,
    FindOrCreateOauthUserUseCase findOrCreateOAuthUserUseCase,
    UserRepository userRepository, UserAccessProfilePort userAccessProfilePort) {
    this.accessTokenPort = accessTokenPort;
    this.findOrCreateOAuthUserUseCase = findOrCreateOAuthUserUseCase;
    this.userRepository = userRepository;
    this.userAccessProfilePort = userAccessProfilePort;
  }
  
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication) throws IOException, ServletException {
    try {
      OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
      String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // "github" o "google"
      
      OAuth2User oAuth2User = oauthToken.getPrincipal();
      
      String provider = registrationId;
      String providerUserId;
      String username;
      String name;
      String email;
      
      if ("github".equalsIgnoreCase(registrationId)) {
        
        Integer idObj = oAuth2User.getAttribute("id");
        providerUserId = idObj != null ? idObj.toString() : null;
        
        username = oAuth2User.getAttribute("login");
        name = oAuth2User.getAttribute("name");
        email = oAuth2User.getAttribute("email");
        
      } else if ("google".equalsIgnoreCase(registrationId)) {
        
        providerUserId = oAuth2User.getAttribute("sub");
        email = oAuth2User.getAttribute("email");
        name = oAuth2User.getAttribute("name");
        username = email;
        
      } else {
        throw new IllegalStateException("Proveedor OAuth2 no soportado: " + registrationId);
      }
      
      OAuthRegisterCommand command = new OAuthRegisterCommand(
        provider,
        providerUserId,
        username,
        name,
        email
      );
      
      UserResponse userResponse = findOrCreateOAuthUserUseCase.findOrCreateOauthUser(command);
      
      User user = userRepository.findByUsername(userResponse.getUsername())
        .orElseThrow(() -> new UserNotFoundException(userResponse.getUsername()));
      
      UserAccessProfile profile = userAccessProfilePort.loadByUsername(username);
      
      String token = accessTokenPort.generateToken(user.getUsername(), profile.getPermissions());
      
      String targetUrl = UriComponentsBuilder.fromUriString(frontendBaseUrl)
        .path("/oauth2/redirect")
        .queryParam("token", token)
        .queryParam("username", userResponse.getUsername())
        .build()
        .toUriString();
      
      getRedirectStrategy().sendRedirect(request, response, targetUrl);
      
    } catch (Exception e) {
      e.printStackTrace();
      getRedirectStrategy().sendRedirect(
        request,
        response,
        frontendBaseUrl + "/login?oauth2Error"
      );
    }
  }
}
