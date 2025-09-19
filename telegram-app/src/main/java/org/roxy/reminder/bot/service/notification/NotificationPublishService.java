package org.roxy.reminder.bot.service.notification;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.NotificationEntityToTgMessageMapper;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.persistence.repository.NotificationRepository;
import org.roxy.reminder.bot.service.http.BotClient;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.response.SendMessageResponseDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationPublishService {
    private final NotificationRepository notificationRepository;
    private final BotClient botClient;
    private final NotificationEntityToTgMessageMapper mapper;

    public NotificationPublishService(NotificationRepository notificationRepository, BotClient botClient, NotificationEntityToTgMessageMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.botClient = botClient;
        this.mapper = mapper;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void publish() {
        log.info("Started scheduled notification publishing" );
        List<NotificationEntity> notificationEntityList = notificationRepository.findByNotifiedIsFalse();
        for(NotificationEntity notification: notificationEntityList) {
            MessageDto notificationMessage = mapper.mapNotificationEntityToMessageDto(notification);
            SendMessageResponseDto responseDto = botClient.sendMessage(notificationMessage);
            if (responseDto.isOk())
            {
                notification.setNotified(true);
            }
            notificationRepository.save(notification);
            //TODO Как корректно сохранять признак отправки ?
            // TODO Как ретраить, если возникла ошибка записи в БД ?
     }
        log.info("Sent {} notifications", notificationEntityList.size());
    }
}
