package org.roxy.mapper;

import org.mapstruct.Mapper;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.roxy.dto.PowerOutageMessageDto;

@Mapper(componentModel = "spring")
public interface PowerOutageMessageMapper {
    PowerOutageMessageDto mapPowerOutageEntityToDto(PowerOutageEntity entity);
}
