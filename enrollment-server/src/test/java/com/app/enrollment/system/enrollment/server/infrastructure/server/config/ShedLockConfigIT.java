package com.app.enrollment.system.enrollment.server.infrastructure.server.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.enrollment.system.enrollment.server.testsupport.PostgresContainerSupport;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ShedLockConfigIT extends PostgresContainerSupport {

  @Autowired
  private LockProvider lockProvider;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static LockConfiguration lockNamed(String name) {
    return new LockConfiguration(Instant.now(), name, Duration.ofMinutes(5), Duration.ZERO);
  }

  @Test
  void schemaSqlCreatesTheShedlockTable() {
    Integer count = jdbcTemplate.queryForObject(
      "SELECT count(*) FROM information_schema.tables WHERE table_name = 'shedlock'",
      Integer.class);

    assertThat(count).isEqualTo(1);
  }
  
  @Test
  void secondReplicaCannotAcquireALockAlreadyHeld() {
    Optional<SimpleLock> firstReplica = lockProvider.lock(lockNamed("outbox-test-held"));
    assertThat(firstReplica).as("la primera réplica debe tomar el lock").isPresent();

    Optional<SimpleLock> secondReplica = lockProvider.lock(lockNamed("outbox-test-held"));
    assertThat(secondReplica).as("la segunda réplica no debe entrar").isEmpty();

    firstReplica.get().unlock();
  }

  @Test
  void lockIsReusableOnceReleased() {
    lockProvider.lock(lockNamed("outbox-test-released")).orElseThrow().unlock();

    Optional<SimpleLock> afterRelease = lockProvider.lock(lockNamed("outbox-test-released"));

    assertThat(afterRelease).isPresent();
    afterRelease.get().unlock();
  }

  @Test
  void lockRowRecordsTheHolder() {
    SimpleLock lock = lockProvider.lock(lockNamed("outbox-test-holder")).orElseThrow();

    String lockedBy = jdbcTemplate.queryForObject(
      "SELECT locked_by FROM shedlock WHERE name = ?", String.class, "outbox-test-holder");

    assertThat(lockedBy).isNotBlank();
    lock.unlock();
  }
}
