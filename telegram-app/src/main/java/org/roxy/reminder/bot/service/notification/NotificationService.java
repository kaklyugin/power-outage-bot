package org.roxy.reminder.bot.service.notification;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.NotificationRepository;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.common.util.AddressFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PowerOutageSourceMessageRepository messageRepository;
    private final UserCartRepository userCartRepository;
    private final PowerOutageMessageMapper mapper;

    @Value("${local.timezone}")
    private String timeZone;

    public NotificationService(NotificationRepository notificationRepository,
                               PowerOutageSourceMessageRepository messageRepository,
                               UserCartRepository userCartRepository,
                               PowerOutageMessageMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.messageRepository = messageRepository;
        this.userCartRepository = userCartRepository;
        this.mapper = mapper;

    }
    @Scheduled(fixedRate = 10_000)
    public void creteNotificationsBySchedule()
    {
        log.info("Started scheduled  function creteNotificationsBySchedule()");
        List<PowerOutageSourceMessageEntity> actualMessages = messageRepository.findActualForDateTime(ZonedDateTime.now());
        int notificationsCount = createNotificationsForMessages(actualMessages).size();
        log.info("Count of created notifications = " + notificationsCount);
    }

    @Transactional
    public List<NotificationEntity> createNotificationsForUsers(List<UserCartEntity> userCartEntities, List<PowerOutageSourceMessageEntity> powerOutageSourceMessages)
    {
        List<NotificationEntity> notifications = new ArrayList<>();
        for (PowerOutageSourceMessageEntity powerOutageMessageEntity : powerOutageSourceMessages)
        {
            for (UserCartEntity userCartEntity : userCartEntities)
            {
                boolean isNotificationCreated =
                userCartEntity.getNotifications().stream()
                        .map(NotificationEntity::getMessageHashCode)
                        .anyMatch(messageHashCode ->
                                Objects.equals(messageHashCode, powerOutageMessageEntity.getMessageHashCode()));
                if (!isNotificationCreated) {
                    if (AddressFormatter.normalizeStreetName(powerOutageMessageEntity.getAddress()).contains(userCartEntity.getNormalizedStreet())
                            && powerOutageMessageEntity.getCity().contains(userCartEntity.getCity().getName())) {
                        NotificationEntity notificationEntity = mapper.mapEntityToNotification(powerOutageMessageEntity);
                        notificationEntity.setUserCart(userCartEntity);
                        notifications.add(notificationEntity);
                    }
                }
            }
        }
        return notificationRepository.saveAll(notifications);
    }

    @Transactional
    public List<NotificationEntity> createNotificationsForUser(UserCartEntity userCartEntity)
    {
        List<PowerOutageSourceMessageEntity> powerOutageSourceMessages = messageRepository.findActualForDateTime(ZonedDateTime.now(ZoneId.of(timeZone)));
        return createNotificationsForUsers (List.of(userCartEntity),powerOutageSourceMessages);
    }

    @Transactional
    public List<NotificationEntity> createNotificationsForMessages(List<PowerOutageSourceMessageEntity> powerOutageSourceMessages)
    {
        List<UserCartEntity> userCartEntities = userCartRepository.findAll();
        return createNotificationsForUsers(userCartEntities,powerOutageSourceMessages);
    }
}