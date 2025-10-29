package org.roxy.reminder.bot.service.notification;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.entity.UserLocationEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.NotificationRepository;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.service.formatter.AddressFormatterService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PowerOutageSourceMessageRepository messageRepository;
    private final UserCartRepository userCartRepository;
    private final PowerOutageMessageMapper mapper;
    private final DateTimeFormatter DATE_TIME_FORMATTER_FIRST_DATE = DateTimeFormatter.ofPattern("dd MMMM (EEEE) HH:mm");
    private final DateTimeFormatter DATE_TIME_FORMATER_SECOND_DATE = DateTimeFormatter.ofPattern("HH:mm");

    public NotificationService(NotificationRepository notificationRepository,
                               PowerOutageSourceMessageRepository messageRepository,
                               UserCartRepository userCartRepository,
                               PowerOutageMessageMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.messageRepository = messageRepository;
        this.userCartRepository = userCartRepository;
        this.mapper = mapper;
    }

    @Async
    @Scheduled(fixedRate = 60_000)
    public void creteNotificationsBySchedule() {
        log.info("Started scheduled  function creteNotificationsBySchedule()");
        createNotificationsForAllUsers();
    }

    //TODO REFACTOR
    @Transactional
    public void createNotificationsForAllUsers() {
        try {
            List<UserCartEntity> userCartEntities = userCartRepository.findAll();
            List<Integer> actualMessagesHashCodes = messageRepository.findActualForDateTime(ZonedDateTime.now());
            List<PowerOutageSourceMessageEntity> providerMessages = messageRepository.findAllByMessageHashCodeInOrderByDateTimeOffAsc(actualMessagesHashCodes);
            for (UserCartEntity userCartEntity : userCartEntities) {
                NotificationEntity notificationEntity = new NotificationEntity();
                for (PowerOutageSourceMessageEntity sourceMessage : providerMessages) {
                    boolean isNotificationForMessageCreated =
                            userCartEntity.getNotifications().stream()
                                    .map(NotificationEntity::getMessageHashCodes)
                                    .flatMap(List::stream)
                                    .anyMatch(messageHashCode ->
                                            Objects.equals(messageHashCode, sourceMessage.getMessageHashCode()));
                    if (!isNotificationForMessageCreated) {
                        for (UserLocationEntity userAddress : userCartEntity.getLocations()) {
                            if (userAddress.getLocationEntity().getLocationFiasId().equals(sourceMessage.getLocationFiasId())) {
                                notificationEntity.setUserCart(userCartEntity);
                                notificationEntity.getMessageHashCodes().add(sourceMessage.getMessageHashCode());
                                String notificationText = appendAddressToNotificationText(notificationEntity.getNotificationText(), sourceMessage);
                                notificationEntity.setNotificationText(notificationText);
                                notificationEntity.setUserCart(userCartEntity);
                            }
                        }
                    }
                }
                if (!notificationEntity.getMessageHashCodes().isEmpty()) {
                    notificationRepository.save(notificationEntity);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private String appendAddressToNotificationText(String existingNotificationText, PowerOutageSourceMessageEntity sourceMessage) {
        if (existingNotificationText == null || existingNotificationText.isEmpty()) {
            existingNotificationText =  "💡Планируется отключение света в " + sourceMessage.getCity();
        }
        StringBuilder notificationTextBuilder = new StringBuilder(existingNotificationText);
        String address =
                "\n\n \uD83D\uDCCD " + sourceMessage.getAddress() +
                        "\n⏱ " + sourceMessage.getDateTimeOff().format(DATE_TIME_FORMATTER_FIRST_DATE) + " - " + sourceMessage.getDateTimeOn().format(DATE_TIME_FORMATER_SECOND_DATE) + ". " +
                        "\nПричина: " + sourceMessage.getPowerOutageReason();
        notificationTextBuilder.append(address);
        return notificationTextBuilder.toString();
    }
}