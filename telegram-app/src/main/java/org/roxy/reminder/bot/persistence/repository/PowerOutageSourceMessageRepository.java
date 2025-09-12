package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerOutageSourceMessageRepository extends JpaRepository<PowerOutageSourceMessageEntity, Long> {
}
