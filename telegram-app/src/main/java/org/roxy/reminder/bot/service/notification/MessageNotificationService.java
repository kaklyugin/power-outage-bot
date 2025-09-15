package org.roxy.reminder.bot.service.notification;

import jakarta.transaction.Transactional;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.NotificationRepository;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.common.util.AddressFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageNotificationService {

    private final NotificationRepository notificationRepository;
    private final PowerOutageSourceMessageRepository messageRepository;
    private final UserCartRepository userCartRepository;
    private final PowerOutageMessageMapper mapper;

    @Value("${local.timezone}")
    private String timeZone;

    public MessageNotificationService(NotificationRepository notificationRepository,
                                      PowerOutageSourceMessageRepository messageRepository,
                                      UserCartRepository userCartRepository,
                                      PowerOutageMessageMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.messageRepository = messageRepository;
        this.userCartRepository = userCartRepository;
        this.mapper = mapper;

    }

    @Transactional
    public void createNotifications()
    {
        List<PowerOutageSourceMessageEntity> powerOutageSourceMessages = messageRepository.findActualForDateTime(ZonedDateTime.now(ZoneId.of(timeZone)));
        List<Integer> notificationsHashCodes = powerOutageSourceMessages.stream().map(PowerOutageSourceMessageEntity::getMessageHashCode).toList();
        List<UserCartEntity> userCartsWithoutNotifications = userCartRepository.findAllUserCartsWithoutConcreteNotifications(notificationsHashCodes);
        for(PowerOutageSourceMessageEntity powerOutageMessageItem: powerOutageSourceMessages)
        {
            List<UserCartEntity> userCartsMatchingNotificationAddress = findUserCartsWhereAddressMatchesPowerOutageAddress(userCartsWithoutNotifications, powerOutageMessageItem);
            List<NotificationEntity> notifications = createNotificationsForUserCarts(userCartsMatchingNotificationAddress, powerOutageMessageItem);
        }
    }

    private List<UserCartEntity> findUserCartsWhereAddressMatchesPowerOutageAddress (List<UserCartEntity> userCartEntities, PowerOutageSourceMessageEntity powerOutageSourceMessage) {
        List<UserCartEntity> userCarts = new ArrayList<>();
        for (UserCartEntity userCartEntity : userCartEntities) {
            if (AddressFormatter.normalizeStreetName(powerOutageSourceMessage.getAddress()).contains(userCartEntity.getNormalizedStreet())
                    && powerOutageSourceMessage.getCity().contains(userCartEntity.getCity().getName())
            )
            {
                userCarts.add(userCartEntity);
            }
        }
        return userCarts;
    }

    private List<NotificationEntity> createNotificationsForUserCarts(List<UserCartEntity> userCartEntities, PowerOutageSourceMessageEntity powerOutageSourceMessage) {
        List<NotificationEntity> notifications = new ArrayList<>();
        for (UserCartEntity userCartEntity : userCartEntities) {
                NotificationEntity notificationEntity = mapper.mapEntityToNotification(powerOutageSourceMessage);
                notificationEntity.setUserCart(userCartEntity);
                notifications.add(notificationEntity);
        }
        return notificationRepository.saveAll(notifications);
    }
}