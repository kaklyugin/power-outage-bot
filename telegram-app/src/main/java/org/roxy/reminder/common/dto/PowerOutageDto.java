package org.roxy.reminder.common.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class PowerOutageDto {

    private String city;
    private String address;
    private ZonedDateTime dateTimeOff;
    private ZonedDateTime dateTimeOn;
    private String powerOutageReason;
    private String url;
    private Integer hashCode;

}
