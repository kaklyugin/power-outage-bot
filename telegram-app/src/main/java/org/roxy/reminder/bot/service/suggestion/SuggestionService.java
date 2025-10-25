package org.roxy.reminder.bot.service.suggestion;

import java.util.List;

public interface SuggestionService {
    /*LocationFiasId - ФИАС код города(city) или поселения (settlement)*/
    List<LocationDto> getStreetSuggestions (String locationRestrictionFiasId, String text);
    List<LocationDto> getStreetSuggestions (String text);
}
