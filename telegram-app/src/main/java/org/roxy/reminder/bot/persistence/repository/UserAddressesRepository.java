package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.UserLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressesRepository extends JpaRepository<UserLocationEntity, Long> {
}