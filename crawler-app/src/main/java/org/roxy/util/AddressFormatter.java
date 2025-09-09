package org.roxy.util;

public class AddressFormatter {
    public static String normalizeStreetName(String streetName) {
        return streetName.toLowerCase().replaceAll("ё", "е");
    }
}
