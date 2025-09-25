package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.roxy.reminder.bot.persistence.entity.StreetEntity;
import org.roxy.reminder.bot.service.suggestion.dto.DaDataAddressResponseDto;

@Mapper(componentModel = "spring")
public interface SuggestedAddressMapper {
   StreetEntity mapStreetDtoToEntity(DaDataAddressResponseDto source);
}
