package org.roxy.reminder.bot.service.polling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.mapper.UpdateResponseMapper;
import org.roxy.reminder.bot.service.UpdateMessageProducer;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateResponseDto;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdatesResponseDto;
import org.roxy.reminder.bot.service.http.HttpBotClient;
import org.roxy.reminder.bot.tgclient.storage.ChatStore;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class UpdatesPollingService {

    private final HttpBotClient httpBotClient;
    private final ChatStore chatStore;
    private final UpdateMessageProducer updateMessageProducer;
    private final ObjectMapper objectMapper;
    private final UpdateResponseMapper mapper;
    private final ExecutorService executor;
    private final int MAX_THREAD_COUNT = 1;

    public UpdatesPollingService(HttpBotClient httpBotClient,
                                 ChatStore chatStore,
                                 UpdateMessageProducer updateMessageProducer,
                                 ObjectMapper objectMapper,
                                 UpdateResponseMapper mapper) {
        this.httpBotClient = httpBotClient;
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
            String json = httpBotClient.getUpdates();
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
                            publishUpdateForProcessing(updateDto);
                        }
                    }
            );
            // Чистим апдейты на сервере TG
            Optional<Long> maxUpdatedId = updates.getResult().stream()
                    .map(UpdateResponseDto::getUpdateId)
                    .max(Long::compareTo);

            if (maxUpdatedId.isPresent()) {
                log.info("Max updated id is {}", maxUpdatedId.get());
                httpBotClient.clearUpdates(maxUpdatedId.get());
            } else {
                log.info("No updates found");
            }
            Thread.sleep(1000);
        }
    }

    public void publishUpdateForProcessing(UpdateDto update) {
        try {
            updateMessageProducer.sendMessage(objectMapper.writeValueAsString(update));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}