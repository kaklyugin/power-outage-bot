package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.roxy.reminder.bot.persistence.entity.LocationEntity;
import org.roxy.reminder.bot.service.suggestion.dto.DaDataAddressResponseDto;
import org.roxy.reminder.bot.service.suggestion.dto.DaDataSuggestionResponseDto;
import org.roxy.reminder.bot.service.suggestion.dto.LocationDto;

@Mapper(componentModel = "spring")
public interface SuggestedAddressMapper {

   @Mapping(target = "locationFiasId", source = ".", qualifiedByName = "mapFiasId")
   LocationEntity mapDaDataResponseItemToEntity(DaDataAddressResponseDto source);

   default LocationDto mapDaDataResponseItemToLocationDto(DaDataAddressResponseDto source)
   {
      if (source == null) {
         return null;
      }

      LocationDto dto = new LocationDto();
      dto.setLocationFiasId(mapFiasId(source));
      if (source.getStreetFiasId() != null && !source.getStreetFiasId().isEmpty()) {
         dto.setLocationType(source.getStreetType());
         dto.setLocationFullName(source.getStreetWithType());
      } else if (source.getSettlementFiasId() != null && !source.getSettlementFiasId().isEmpty()) {
         dto.setLocationType(source.getSettlementType());
         dto.setLocationFullName(source.getSettlementWithType());
      }
      return dto;
   }

   @Named("mapFiasId")
   default String mapFiasId(DaDataAddressResponseDto source) {
      if (source == null) {
         return null;
      }
      if (source.getStreetFiasId() != null && !source.getStreetFiasId().isEmpty()) {
         return source.getStreetFiasId();
      } else if (source.getSettlementFiasId() != null && !source.getSettlementFiasId().isEmpty()) {
         return source.getSettlementFiasId();
      }
      return null;
   }
}