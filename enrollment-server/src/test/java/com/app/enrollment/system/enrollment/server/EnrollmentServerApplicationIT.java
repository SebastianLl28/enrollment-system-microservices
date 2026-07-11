package com.app.enrollment.system.enrollment.server;

import com.app.enrollment.system.enrollment.server.testsupport.PostgresContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Verifica que el contexto completo del servicio arranca contra un Postgres
 * real (Testcontainers), sin Eureka ni Kafka ni Mercado Pago.
 */
@SpringBootTest
@ActiveProfiles("test")
class EnrollmentServerApplicationIT extends PostgresContainerSupport {

  @Test
  void contextLoads() {
  }

}
