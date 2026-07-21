package com.app.enrollment.system.enrollment.server;

import com.app.enrollment.system.enrollment.server.testsupport.PostgresContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Verify context loads against a real Postgres (Testcontainers),
 * without Eureka, Kafka, or Mercado Pago.
 */
@SpringBootTest
@ActiveProfiles("test")
class EnrollmentServerApplicationIT extends PostgresContainerSupport {

  @Test
  void contextLoads() {
  }

}
