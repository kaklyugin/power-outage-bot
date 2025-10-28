package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.roxy.reminder.bot.persistence.entity.LocationEntity;
import org.roxy.reminder.bot.service.suggestion.dto.DaDataAddressResponseDto;

@Mapper(componentModel = "spring")
public interface SuggestedAddressMapper {

   @Mapping(target = "locationFiasId", source = ".", qualifiedByName = "mapFiasId")
   LocationEntity mapStreetDtoToEntity(DaDataAddressResponseDto source);

   @Named("mapFiasId")
   default String mapFiasId(DaDataAddressResponseDto source) {
      if (source.getStreetFiasId() != null && !source.getStreetFiasId().isEmpty()) {
         return source.getStreetFiasId();
      } else if (source.getSettlementFiasId() != null && !source.getSettlementFiasId().isEmpty()) {
         return source.getSettlementFiasId();
      }
      return null;
   }
}