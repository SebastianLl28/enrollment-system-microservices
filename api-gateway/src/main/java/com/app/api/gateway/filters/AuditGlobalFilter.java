package com.app.api.gateway.filters;

import com.app.api.gateway.audit.AuditProperties;
import com.app.api.gateway.port.AuditPublisher;
import com.app.common.constant.ApiConstants;
import com.app.common.events.AuditEvent;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

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
    
    // 1. Verificar exclusiones
    if (props.excludedPaths() != null && props.excludedPaths().stream().anyMatch(path::startsWith)) {
      return chain.filter(exchange);
    }
    
    // 2. Decidir si leemos el body (Solo para POST/PUT y si es JSON/Texto)
    if (shouldReadBody(request)) {
      return ServerWebExchangeUtils.cacheRequestBody(
        exchange,
        (serverHttpRequest) -> {
          // Aquí el body ya está cacheado y es seguro leerlo
          // IMPORTANTE: Usamos 'serverHttpRequest' que es el decorado
          return processAudit(exchange.mutate().request(serverHttpRequest).build(), chain);
        }
      );
    } else {
      // Si es GET o un archivo, no tocamos el body
      return processAudit(exchange, chain);
    }
  }
  
  private Mono<Void> processAudit(ServerWebExchange exchange, GatewayFilterChain chain) {
    Instant start = Instant.now();
    ServerHttpRequest request = exchange.getRequest();
    URI uri = request.getURI();
    
    // Capturamos datos iniciales
    String path = uri.getPath();
    String method = request.getMethod().name();
    String userId = request.getHeaders().getFirst(ApiConstants.HEADER_USER_ID);
    String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
    
    // Intentamos obtener el body ya cacheado
    String requestBody = getCachedBody(exchange);
    
    return chain.filter(exchange)
      .doOnSuccess(v -> publish(exchange, start, path, method, userId, uri.getQuery(), userAgent, requestBody))
      .doOnError(err -> publish(exchange, start, path, method, userId, uri.getQuery(), userAgent, requestBody));
  }
  
  private String getCachedBody(ServerWebExchange exchange) {
    // Spring guarda el body en un atributo tras llamar a cacheRequestBody
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
    
    boolean isModifyMethod = HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method);
    boolean isJsonCompatible = contentType != null && (
      contentType.includes(MediaType.APPLICATION_JSON) ||
        contentType.includes(MediaType.TEXT_PLAIN)
    );
    
    return isModifyMethod && isJsonCompatible;
  }
  
  private void publish(ServerWebExchange exchange, Instant start, String path, String method,
    String userId, String query, String userAgent, String body) {
    
    int status = exchange.getResponse().getStatusCode() != null
      ? exchange.getResponse().getStatusCode().value() : 500;
    
    String serviceId = getServiceId(path);
    
    // OJO: Limpieza de seguridad (opcional pero recomendada)
//     body = maskSensitiveData(body);
    
    auditPublisher.publish(new AuditEvent(start, serviceId, path, method, status, userId, query, userAgent, body));
  }
  
  // ... getServiceId y getOrder iguales ...
  private String getServiceId(String path) {
    // Tu lógica original o la mejora que sugerí antes
    if (path.startsWith("/auth")) return "authorization-server";
    else if (path.startsWith("/api")) return "enrollment-server";
    else return "api-gateway";
  }
  
  @Override
  public int getOrder() {
    return props.order();
  }
}
