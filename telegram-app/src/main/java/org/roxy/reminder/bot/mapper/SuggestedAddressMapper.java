package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.roxy.reminder.bot.persistence.entity.StreetEntity;
import org.roxy.reminder.bot.service.suggestion.client.AddressDataDto;

@Mapper(componentModel = "spring")
public interface SuggestedAddressMapper {
   StreetEntity mapStreetDtoToEntity(AddressDataDto source);
}
