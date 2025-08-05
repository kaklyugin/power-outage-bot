package org.roxy.reminder.crawler.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class PowerOutageItem {
    private final int id;
    private final String location;
    private final String address;
    private final ZonedDateTime startDateTime;
    private final ZonedDateTime endDateTime;
    private final String powerOutageReason;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH=mm");

    public PowerOutageItem(String id,
                           String location,
                           String address,
                           String startDate,
                           String endDate,
                           String startTime,
                           String endTime,
                           String powerOutageReason,
                           ZoneId zoneId) {
        this.id = Integer.parseInt(id);
        this.location = location;
        this.address = address;
        this.startDateTime = convertDateTime(startDate, startTime, zoneId);
        this.endDateTime = convertDateTime(endDate, endTime, zoneId);
        this.powerOutageReason = powerOutageReason;
    }

    private ZonedDateTime convertDateTime(String date, String time, ZoneId zoneId) {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter));
        return ldt.atZone(zoneId);
    }
}
