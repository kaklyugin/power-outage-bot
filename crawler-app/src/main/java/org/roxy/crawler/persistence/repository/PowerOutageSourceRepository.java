package org.roxy.crawler.persistence.repository;

import org.roxy.crawler.persistence.entity.PowerOutagePageUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PowerOutageSourceRepository extends JpaRepository<PowerOutagePageUrlEntity,Long> {
    List<PowerOutagePageUrlEntity> findAllWhereEnabledIsTrue();
}
