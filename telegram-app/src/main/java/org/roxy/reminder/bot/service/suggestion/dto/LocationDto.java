package org.roxy.reminder.bot.service.suggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LocationDto {
    private  String locationFullName;
    private  String locationFiasId;
    private  String locationType;
}
