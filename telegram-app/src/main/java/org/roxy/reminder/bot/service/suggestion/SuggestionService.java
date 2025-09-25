package org.roxy.reminder.bot.service.suggestion;

import java.util.List;

public interface SuggestionService {
    List<StreetDto> getStreetSuggestions (String locationFiasId, String userInput);
}
