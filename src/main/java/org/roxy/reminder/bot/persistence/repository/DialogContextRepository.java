package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialogContextRepository extends JpaRepository<DialogContextEntity, Long> {
}
