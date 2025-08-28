package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCartRepository extends JpaRepository<UserCartEntity, Long> {

    @Query("SELECT distinct u FROM UserCartEntity u left join fetch u.notifications n " +
            " where (n is null or n.powerOutageHash not in :excludeHashes)")
        //FIXME
        //@Query("SELECT distinct u FROM UserCartEntity u left join fetch u.notifications n WHERE  n.powerOutageHash NOT IN :excludeHashes")
    List<UserCartEntity> findAllWhereNotificationHashNotIn(@Param("excludeHashes") List<Integer> excludeHashes);

    Optional<UserCartEntity> findByChatId(Long chatId);
}
