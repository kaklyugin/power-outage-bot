package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateResponseDto;

@Mapper(componentModel = "spring")
public interface UpdateResponseMapper {
    UpdateDto mapUpdateResponseToUpdateDto(UpdateResponseDto updateResponseDto);
}
