package org.roxy.reminder.bot.tgclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.roxy.reminder.bot.util.AddressUtils;
import org.roxy.reminder.bot.util.AddressComponents;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AddressFormatTest {
    @Test
    @DisplayName("Address Split Test")
    void testAddressSplitting() {
        // Test case 1

        String address1 = "Ул. 24 Гв. Дивизии 1, 11-13А,17,55Б";
        AddressComponents sa1 = AddressUtils.extractAddressComponents(address1);
        assertEquals("ул", sa1.getStreetType());
        assertEquals("24 Гв. Дивизии", sa1.getStreetName());
        assertEquals(List.of("1", "11-13А", "17", "55Б"), sa1.getBuildingsNumbers());

    }
}
