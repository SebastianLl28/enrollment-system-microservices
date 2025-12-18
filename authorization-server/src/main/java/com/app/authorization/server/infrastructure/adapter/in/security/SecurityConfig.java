package com.app.authorization.server.infrastructure.adapter.in.security;

import static com.app.common.constant.ApiConstants.PUBLIC_ENDPOINTS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Alonso
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  private final JwtAuthFilter jwtAuthFilter;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
//  private final OAuth2RedirectUriResolver oAuth2RedirectUriResolver;
  
  public SecurityConfig(
    JwtAuthFilter jwtAuthFilter,
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
    OAuth2RedirectUriResolver oAuth2RedirectUriResolver
  ) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
//    this.oAuth2RedirectUriResolver = oAuth2RedirectUriResolver;
  }
  
  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager();
  }
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .cors(Customizer.withDefaults())
      .formLogin(AbstractHttpConfigurer::disable)
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
      )
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
        .requestMatchers("/oauth2/**", "/login/oauth2/**", "/login/**").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth2 -> oauth2
        .authorizationEndpoint(authorization -> authorization
          .baseUri("/oauth2/authorization")
//          .authorizationRequestResolver(oAuth2RedirectUriResolver)
        )
        .successHandler(oAuth2AuthenticationSuccessHandler)
      )
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}
