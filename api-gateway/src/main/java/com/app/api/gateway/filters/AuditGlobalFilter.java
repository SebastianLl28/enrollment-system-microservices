package com.app.api.gateway.filters;

import com.app.api.gateway.audit.AuditProperties;
import com.app.api.gateway.port.AuditPublisher;
import com.app.common.constant.ApiConstants;
import com.app.common.events.AuditEvent;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Alonso
 */
@Component
public class AuditGlobalFilter implements GlobalFilter, Ordered {

  private static final Logger log = LoggerFactory.getLogger(AuditGlobalFilter.class);

  private final AuditPublisher auditPublisher;
  private final AuditProperties props;
  
  public AuditGlobalFilter(AuditPublisher auditPublisher, AuditProperties props) {
    this.auditPublisher = auditPublisher;
    this.props = props;
  }
  
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    if (!props.enabled()) {
      return chain.filter(exchange);
    }
    
    ServerHttpRequest request = exchange.getRequest();
    String path = request.getURI().getPath();
    
    // 1. exclude paths e.g. /api/health
    if (props.excludedPaths() != null && props.excludedPaths().stream()
        .anyMatch(path::startsWith)) {
      return chain.filter(exchange);
    }
    
    // 2. Check if we should read the body (POST, PUT, PATCH with JSON or text)
    if (shouldReadBody(request)) {
      return ServerWebExchangeUtils.cacheRequestBody(
          exchange,
          (serverHttpRequest) ->
             processAudit(exchange.mutate().request(serverHttpRequest).build(), chain)
      );
    } else {
      return processAudit(exchange, chain);
    }
  }
  
  private Mono<Void> processAudit(ServerWebExchange exchange, GatewayFilterChain chain) {
    Instant start = Instant.now();
    ServerHttpRequest request = exchange.getRequest();
    URI uri = request.getURI();
    
    String path = uri.getPath();
    String method = request.getMethod().name();
    String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
    
    String requestBody = getCachedBody(exchange);
    
    return chain.filter(exchange)
      .doOnSuccess(v -> publish(exchange, start, path, method, uri.getQuery(), userAgent, requestBody))
      .doOnError(err -> publish(exchange, start, path, method, uri.getQuery(), userAgent, requestBody));
  }
  
  private String getCachedBody(ServerWebExchange exchange) {
    Object cachedBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
    if (cachedBody instanceof DataBuffer) {
      DataBuffer buffer = (DataBuffer) cachedBody;
      return buffer.toString(StandardCharsets.UTF_8);
    }
    return null;
  }
  
  private boolean shouldReadBody(ServerHttpRequest request) {
    HttpMethod method = request.getMethod();
    MediaType contentType = request.getHeaders().getContentType();
    
    boolean isModifyMethod =
        HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(
            method);
    boolean isJsonCompatible = contentType != null && (
        contentType.includes(MediaType.APPLICATION_JSON) ||
            contentType.includes(MediaType.TEXT_PLAIN)
    );
    
    return isModifyMethod && isJsonCompatible;
  }
  
  private void publish(ServerWebExchange exchange, Instant start, String path, String method,
      String query, String userAgent, String body) {

    int status = exchange.getResponse().getStatusCode() != null
        ? exchange.getResponse().getStatusCode().value() : 500;

    String serviceId = getServiceId(path);
    String userId = exchange.getAttribute(ApiConstants.HEADER_USER_ID);

    logAccess(path, method, query, status, start, userAgent, body);

    auditPublisher.publish(
        new AuditEvent(start, serviceId, path, method, status, userId, query, userAgent, body));
  }

  /**
   * Access log estilo morgan: una línea por request (método, ruta, status, duración).
   * Solo los webhooks de pago loguean además query, user-agent y body, porque ahí
   * no hay usuario autenticado que rastrear y el payload es lo único que permite
   * diagnosticar una notificación perdida.
   */
  private void logAccess(String path, String method, String query, int status, Instant start,
      String userAgent, String body) {

    long durationMs = Duration.between(start, Instant.now()).toMillis();

    log.info("{} {} {} {}ms", method, path, status, durationMs);

    if (path.startsWith("/webhooks")) {
      log.info("[webhook] {} {}{} ua=\"{}\" body={}", method, path,
          query != null ? "?" + query : "",
          userAgent != null ? userAgent : "-",
          body != null ? body : "-");
    }
  }
  
  private String getServiceId(String path) {
    if (path.startsWith("/auth")) {
      return "authorization-server";
    } else if (path.startsWith("/api")) {
      return "enrollment-server";
    } else {
      return "api-gateway";
    }
  }
  
  @Override
  public int getOrder() {
    return props.order();
  }
}
