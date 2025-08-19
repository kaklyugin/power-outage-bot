package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateMachineRepository extends JpaRepository<StateMachineEntity, Long> {
}
