package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.service.webclient.dto.updates.UpdateResponseDto;

@Mapper(componentModel = "spring")
public interface UpdateResponseMapper {
    UpdateDto mapUpdateResponseToUpdateDto(UpdateResponseDto updateResponseDto);
}
