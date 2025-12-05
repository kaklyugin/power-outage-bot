package org.roxy.reminder.bot.service.webclient.dto.updates;


import lombok.Getter;

import java.util.List;

@Getter
public class UpdatesResponseDto {
    private boolean ok;
    private List<UpdateResponseDto> result;
}
