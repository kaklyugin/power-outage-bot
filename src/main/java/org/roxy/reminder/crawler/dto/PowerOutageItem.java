package org.roxy.reminder.crawler.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class PowerOutageItem {
    private final int id;
    private final String city;
    private final String address;
    private final ZonedDateTime dateTimeOff;
    private final ZonedDateTime dateTimeOn;
    private final String powerOutageReason;
    private final Integer hashCode;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH['=']['-']mm");

    public PowerOutageItem(String id,
                           String city,
                           String address,
                           String startDate,
                           String endDate,
                           String startTime,
                           String endTime,
                           String powerOutageReason,
                           ZoneId zoneId
    ) {
        this.id = Integer.parseInt(id);
        this.city = city;
        this.address = address;
        this.dateTimeOff = convertDateTime(startDate, startTime, zoneId);
        this.dateTimeOn = convertDateTime(endDate, endTime, zoneId);
        this.hashCode = calculateHashForPowerOutageRecord(city,address,dateTimeOff,dateTimeOn);
        this.powerOutageReason = powerOutageReason;
    }

    private ZonedDateTime convertDateTime(String date, String time, ZoneId zoneId) {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter));
        return ldt.atZone(zoneId);
    }

    private int calculateHashForPowerOutageRecord(String city, String address, ZonedDateTime powerOffDateTime,ZonedDateTime powerOnDateTime )
    {
        return (city + address + powerOnDateTime + powerOffDateTime).toLowerCase().hashCode();
    }
}