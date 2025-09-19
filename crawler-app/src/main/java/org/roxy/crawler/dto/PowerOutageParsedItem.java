package org.roxy.crawler.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@Slf4j
public class PowerOutageParsedItem {
    private final int id;
    private final String city;
    private final String address;
    private final ZonedDateTime dateTimeOff;
    private final ZonedDateTime dateTimeOn;
    private final String powerOutageReason;
    private final Integer messageHashCode;
    private final String comment;

    private static final List<DateTimeFormatter> dateFormatters = List.of(
            DateTimeFormatter.ofPattern("dd.MM.yy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy")
    );
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH['=']['-']mm");


    public PowerOutageParsedItem(String id,
                                 String city,
                                 String address,
                                 String startDate,
                                 String endDate,
                                 String startTime,
                                 String endTime,
                                 String powerOutageReason,
                                 ZoneId zoneId,
                                 String comment
    ) {
        this.id = Integer.parseInt(id);
        this.city = city;
        this.address = address;
        this.dateTimeOff = convertDateTime(startDate, startTime, zoneId);
        this.dateTimeOn = convertDateTime(endDate, endTime, zoneId);
        this.messageHashCode = calculateHashForPowerOutageRecord(city,address,dateTimeOff,dateTimeOn);
        this.powerOutageReason = powerOutageReason;
        this.comment = comment;
    }
    //TODO Вынести Zone в настройки бота
    private ZonedDateTime convertDateTime(String date, String time, ZoneId zoneId) {
        LocalDateTime ldt;
        try {
            LocalDate localDate = parseDate(date);
            Objects.requireNonNull(localDate);
            ldt = LocalDateTime.of(localDate, LocalTime.parse(time, timeFormatter));
            return ldt.atZone(zoneId);
        }
        catch (Exception e)
        {
            log.error("Failed to parse date/time from string: time = {} date = {}. Error message : {}", time, date, e.getMessage());
        }
        return null;
    }

    public static LocalDate parseDate(String dateString) {
        for (DateTimeFormatter dtf : dateFormatters) {
            try {
                return LocalDate.parse(dateString,dtf);
            } catch (DateTimeParseException e) {
                log.info("Retry parse date: date = {} with other formatter", dateString);
            }
        }
        return null;
    }

    private int calculateHashForPowerOutageRecord(String city, String address, ZonedDateTime powerOffDateTime,ZonedDateTime powerOnDateTime )
    {
        return (city + address + powerOnDateTime + powerOffDateTime).toLowerCase().hashCode();
    }
}