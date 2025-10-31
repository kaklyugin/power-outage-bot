package org.roxy.reminder.bot.service.enricher;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.service.suggestion.SuggestionService;
import org.roxy.reminder.bot.service.suggestion.dto.LocationDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OutageMessageEnricher {
    private final PowerOutageSourceMessageRepository repository;
    private final SuggestionService suggestionService;

    public OutageMessageEnricher(PowerOutageSourceMessageRepository repository,
                                 SuggestionService suggestionService) {
        this.repository = repository;
        this.suggestionService = suggestionService;
    }

    @Transactional
    @Scheduled(cron = "0/10 * * * * *")
    @SchedulerLock(name = "enrichWithFiasId", lockAtMostFor = "9s")
    public void enrichWithFiasId() {
        List<PowerOutageSourceMessageEntity> recordsForEnriching = repository.findByIsStreetFiasRequestedFalseAndIsArchivedFalse();
        try {
            for (PowerOutageSourceMessageEntity record : recordsForEnriching) {
                try {
                    LocationDto location = findLocationFiasId(record.getCity(), record.getStreetType(), record.getStreetName());
                    record.setLocationFiasId(location.getLocationFiasId());
                    record.setLocationType(location.getLocationType());
                    record.setStreetFiasRequested(true);
                } catch (Exception e) {
                    log.error("Failed to add locationFiasId via suggestion service: {}", e.getMessage());
                } finally {
                    record.setStreetFiasRequested(true);
                }
            }
        } catch (Exception e) {
            log.error("Procedure enrichWithFiasId faile: {}", e.getMessage());
        } finally {
            repository.saveAll(recordsForEnriching);
        }
    }

    private LocationDto findLocationFiasId(String city, String streetType, String streetName) {
        String text = "Ростовская область, " + city + " " + streetType + " " + streetName;
        List<LocationDto> suggestions = suggestionService.getStreetSuggestions(text);
        if (suggestions.isEmpty()) {
            /*Пробуем без типа улицы потому что Донэнерго иногда переулки называет улицами*/
            text = "Ростовская область, " + city + " " + streetName;
            suggestions = suggestionService.getStreetSuggestions(text);
        }
        if (suggestions.isEmpty()) {
            log.warn("Could not find suggestion for {}", text);
        }
        return !suggestions.isEmpty() ? suggestions.getFirst() : new LocationDto();
    }
}