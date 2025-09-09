package org.roxy.reminder.crawler.persistence.repository;

import org.roxy.reminder.crawler.persistence.entity.PowerOutageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerOutageRepository extends JpaRepository<PowerOutageEntity, Long> {
}
