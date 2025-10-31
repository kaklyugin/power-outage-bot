package org.roxy.reminder.bot.service.suggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private  String locationFullName;
    private  String locationFiasId;
    private  String locationType;
}
