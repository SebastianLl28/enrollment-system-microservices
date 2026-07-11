package com.app.enrollment.system.enrollment.server.testsupport;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base para tests de integración (*IT) que necesitan PostgreSQL real. El
 * contenedor es estático: se levanta una sola vez y se comparte entre las
 * clases que extienden de aquí.
 */
@Testcontainers
public abstract class PostgresContainerSupport {

  @Container
  @ServiceConnection
  protected static final PostgreSQLContainer<?> POSTGRES =
    new PostgreSQLContainer<>("postgres:16-alpine");

}
