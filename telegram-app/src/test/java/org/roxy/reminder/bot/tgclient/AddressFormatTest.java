package org.roxy.reminder.bot.tgclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.roxy.reminder.bot.util.AddressFormatter;
import org.roxy.reminder.bot.util.SplittedAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AddressFormatTest {
    @Test
    @DisplayName("Address Split Test")
    void testAddressSplitting() {
        // Test case 1
        String address1 = "Лодочная 1Б, 3А, 5, 11, 15А, 21, 23, 23А, 23Н, 25";
        SplittedAddress sa1 = AddressFormatter.splitAddress(address1);
        assertEquals("Лодочная", sa1.getStreetName());
        assertEquals("1Б, 3А, 5, 11, 15А, 21, 23, 23А, 23Н, 25", sa1.getBuildingsNumbers());

        // Test case 2
        String address2 = "66-70";
        SplittedAddress sa2 = AddressFormatter.splitAddress(address2);
        assertEquals("", sa2.getStreetName());
        assertEquals("66-70", sa2.getBuildingsNumbers());

        // Test case 3
        String address3 = "24 Гв. Дивизии 11-13";
        SplittedAddress sa3 = AddressFormatter.splitAddress(address3);
        assertEquals("24 Гв. Дивизии", sa3.getStreetName());
        assertEquals("11-13", sa3.getBuildingsNumbers());

        // Test case 4
        String address4 = "2-й Аварийный";
        SplittedAddress sa4 = AddressFormatter.splitAddress(address4);
        assertEquals("2-й Аварийный", sa4.getStreetName());
        assertEquals("", sa4.getBuildingsNumbers());

        // Test case 5
        String address5 = "Красноармейская";
        SplittedAddress sa5 = AddressFormatter.splitAddress(address5);
        assertEquals("Красноармейская", sa5.getStreetName());
        assertEquals("", sa5.getBuildingsNumbers());

        // Test case 6
        String address6 = "ул. 40 лет Октября 88-94, 65-75.";
        SplittedAddress sa6 = AddressFormatter.splitAddress(address6);
        assertEquals("ул. 40 лет Октября", sa6.getStreetName());
        assertEquals("88-94, 65-75.", sa6.getBuildingsNumbers());
    }
}
