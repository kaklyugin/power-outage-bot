package org.roxy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data
@NoArgsConstructor
public class PowerOutageMessageDto {
    private String parsingStatus;
    private String city;
    private String address;
    private ZonedDateTime dateTimeOff;
    private ZonedDateTime dateTimeOn;
    private String powerOutageReason;
    private String url;
    private Integer messageHashCode;
    private String comment;
}