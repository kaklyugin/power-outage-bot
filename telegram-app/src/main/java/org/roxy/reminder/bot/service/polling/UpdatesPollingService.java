package org.roxy.reminder.bot.service.polling;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.mapper.UpdateResponseMapper;
import org.roxy.reminder.bot.service.broker.UpdateMessageProducer;
import org.roxy.reminder.bot.service.webclient.dto.updates.UpdateResponseDto;
import org.roxy.reminder.bot.service.webclient.dto.updates.UpdatesResponseDto;
import org.roxy.reminder.bot.service.webclient.HttpBotClientService;
import org.roxy.reminder.bot.cache.ChatStore;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class UpdatesPollingService {

    private final HttpBotClientService httpBotClientService;
    private final ChatStore chatStore;
    private final UpdateMessageProducer updateMessageProducer;
    private final ObjectMapper objectMapper;
    private final ExecutorService executor;
    private final UpdateResponseMapper mapper;
    private final int MAX_THREAD_COUNT = 1;
    private final int SLEEP_BEFORE_NEXT_UPDATE_REQUEST_MS = 1_000;

    public UpdatesPollingService(HttpBotClientService httpBotClientService,
                                 ChatStore chatStore,
                                 UpdateMessageProducer updateMessageProducer,
                                 ObjectMapper objectMapper,
                                 UpdateResponseMapper mapper) {
        this.httpBotClientService = httpBotClientService;
        this.chatStore = chatStore;
        this.updateMessageProducer = updateMessageProducer;
        this.objectMapper = objectMapper;
        this.mapper = mapper;
        executor = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
    }

    @PostConstruct
    void runPolling() {
        executor.submit(this::poll);
    }

    @SneakyThrows
    public void poll() {
        while (true) {
            log.info("Polling");
            String json = httpBotClientService.getUpdates();
            log.info("Updates received from tg =  {}", json);

            UpdatesResponseDto updates = objectMapper.readValue(json, UpdatesResponseDto.class);
            if (!updates.isOk()) {
                throw new RuntimeException("No updates found." + json);
            }
            updates.getResult().forEach(responseDto ->
                    {
                        if (!chatStore.checkUpdateExists(responseDto.getChatId(), responseDto.getUpdateId())) {
                            chatStore.pushUpdate(responseDto.getChatId(), responseDto.getUpdateId(), responseDto);
                            UpdateDto updateDto = mapper.mapUpdateResponseToUpdateDto(responseDto);
                            updateMessageProducer.sendUpdateToUpdateProcessor(updateDto);
                        }
                    }
            );
            // Чистим апдейты на сервере TG
            Optional<Long> maxUpdatedId = updates.getResult().stream()
                    .map(UpdateResponseDto::getUpdateId)
                    .max(Long::compareTo);
            if (maxUpdatedId.isPresent()) {
                log.info("Max updated id is {}", maxUpdatedId.get());
                httpBotClientService.clearUpdates(maxUpdatedId.get());
            } else {
                log.info("No updates found");
            }
            Thread.sleep(SLEEP_BEFORE_NEXT_UPDATE_REQUEST_MS);
        }
    }
}