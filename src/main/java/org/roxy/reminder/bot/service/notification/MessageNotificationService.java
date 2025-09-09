package org.roxy.reminder.bot.service.notification;

import jakarta.transaction.Transactional;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.NotificationRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.common.dto.PowerOutageDto;
import org.roxy.reminder.common.util.AddressFormatter;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageNotificationService {

    private final NotificationRepository notificationRepository;
    private final UserCartRepository userCartRepository;
    private final PowerOutageMessageMapper mapper;


    public MessageNotificationService(NotificationRepository notificationRepository, UserCartRepository userCartRepository, PowerOutageMessageMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.userCartRepository = userCartRepository;
        this.mapper = mapper;

    }

    @Transactional
    public void createNotifications(List<PowerOutageDto> powerOutageItems)
    {
        List<Integer> powerItemHashes = powerOutageItems.stream().map(PowerOutageDto::getHashCode).toList();
        List<UserCartEntity> userCartEntities = userCartRepository.findAllWhereNotificationHashNotIn(powerItemHashes);
        for(PowerOutageDto item: powerOutageItems)
        {
            List<NotificationEntity> notifications = findMatchingUserCartAndGenerateNotification(userCartEntities, item);
            notificationRepository.saveAll(notifications);
        }
    }

    private List<NotificationEntity> findMatchingUserCartAndGenerateNotification(List<UserCartEntity> userCartEntities, PowerOutageDto powerOutageDto) {
        List<NotificationEntity> notifications = new ArrayList<>();
        for (UserCartEntity userCartEntity : userCartEntities) {
            if (AddressFormatter.normalizeStreetName(powerOutageDto.getAddress()).contains(userCartEntity.getNormalizedStreet())
                    && powerOutageDto.getCity().contains(userCartEntity.getCity())
                        && (powerOutageDto.getDateTimeOff().isBefore(ZonedDateTime.now(ZoneId.of("Europe/Moscow"))))
            )
            {
                NotificationEntity notificationEntity = mapper.mapDtoToEntity(powerOutageDto);
                notificationEntity.setUserCart(userCartEntity);
                notifications.add(notificationEntity);
            }
        }
        return notifications;
    }
}
