package com.app.api.gateway.audit;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Alonso
 */
@ConfigurationProperties(prefix = "audit")
public record AuditProperties(
  boolean enabled,
  int order,
  List<String> excludedPaths
) {}
