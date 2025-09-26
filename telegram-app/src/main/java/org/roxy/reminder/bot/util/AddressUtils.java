package org.roxy.reminder.bot.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class AddressUtils {

    public static AddressComponents extractAddressComponents(String address) {
        String[] tokens = address.split(" ");
        List<AddressToken> addressTokenList = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            AddressToken token = new AddressToken();
            token.tokenNumber = i;
            token.text = tokens[i];
            token.cyrillicLettersCount = countCyrillicLetters(tokens[i]);
            token.digitsAndSymbolsCount = countDigitsAndSymbols(tokens[i]);
            addressTokenList.add(token);
        }
        int longestTokenNumberWithLetters = addressTokenList.stream()
                .max(Comparator.comparingInt(x -> x.cyrillicLettersCount)).get().tokenNumber;

        for (AddressToken token : addressTokenList) {
            if (token.tokenNumber == 0 &&
                    token.cyrillicLettersCount >= token.digitsAndSymbolsCount) {
                token.type = TokenType.STREET_TYPE;
            } else if (token.tokenNumber >= longestTokenNumberWithLetters
                    && token.digitsAndSymbolsCount >= token.cyrillicLettersCount
            ) {
                token.type = TokenType.BUILDING_NUMBER;
            } else {
                token.type = TokenType.STREET_NAME;
            }

        }
        String streetType = addressTokenList.stream().
                filter(token -> token.type.equals(TokenType.STREET_TYPE))
                .findFirst()
                .map(t -> t.text)
                .map(t -> t.replaceAll("\\.", "").toLowerCase())
                .orElse("");

        String streetName = addressTokenList.stream().
                filter(token -> token.type.equals(TokenType.STREET_NAME))
                .map(t -> t.text)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        List<String> buildingsNumbers = addressTokenList.stream().
                filter(token -> token.type.equals(TokenType.BUILDING_NUMBER))
                .map(t -> t.text)
                .flatMap(t-> Arrays.stream(t.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        return new AddressComponents(streetType, streetName, buildingsNumbers);
    }

    private static class AddressToken {
        public int tokenNumber;
        public String text;
        public int cyrillicLettersCount;
        public int digitsAndSymbolsCount;
        public TokenType type;
    }

    private static int countCyrillicLetters(String text) {
        if (text == null) return 0;
        Pattern pattern = Pattern.compile("[А-Яа-я]");
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countDigitsAndSymbols(String text) {
        if (text == null) return 0;
        Pattern pattern = Pattern.compile("[\\d\\p{P}\\p{S}]");
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private enum TokenType {
        STREET_TYPE,
        STREET_NAME,
        BUILDING_NUMBER
    }
}