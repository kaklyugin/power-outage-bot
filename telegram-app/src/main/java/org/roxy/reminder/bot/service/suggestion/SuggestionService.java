package org.roxy.reminder.bot.service.suggestion;

import java.util.List;

public interface SuggestionService {
    /*LocationFiasId - ФИАС код города(city) или поселения (settlement)*/
    List<StreetDto> getStreetSuggestions (String locationRestrictionFiasId, String text);
    List<StreetDto> getStreetSuggestions (String text);
}
