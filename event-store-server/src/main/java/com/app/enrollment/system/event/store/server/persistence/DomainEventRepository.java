package com.app.enrollment.system.event.store.server.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alonso
 */
public interface DomainEventRepository extends JpaRepository<DomainEventEntity, Long> {
}
