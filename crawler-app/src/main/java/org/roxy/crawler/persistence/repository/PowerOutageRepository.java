package org.roxy.crawler.persistence.repository;

import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerOutageRepository extends JpaRepository<PowerOutageEntity, Long> {
}
