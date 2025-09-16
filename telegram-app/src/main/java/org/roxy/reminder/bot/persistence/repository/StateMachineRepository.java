package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StateMachineRepository extends JpaRepository<StateMachineEntity, Long> {
    StateMachineEntity getByChatId(Long chatId);

    @Query("select s from StateMachineEntity s where s.chatId = :chatId and s.state != State.COMPLETED")
    Optional<StateMachineEntity> findActiveByChatId(@Param("chatId") Long chatId);

    Optional<StateMachineEntity> findByChatId(Long chatId);
}
