package org.roxy.reminder.bot.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class AddressFormatter {

    public static SplittedAddress splitAddress(String address) {
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
            if (token.tokenNumber >= longestTokenNumberWithLetters
                    && token.digitsAndSymbolsCount >= token.cyrillicLettersCount
            ) {
                token.isBuildingNumber = true;
            }
        }

        var streetName = addressTokenList.stream().
                filter(token -> !token.isBuildingNumber)
                .map(t -> t.text)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        var buildingsNumbers = addressTokenList.stream().
                filter(token -> token.isBuildingNumber)
                .map(t -> t.text)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        return new SplittedAddress(streetName, buildingsNumbers);

    }

    private static class AddressToken {
        public int tokenNumber;
        public String text;
        public int cyrillicLettersCount;
        public int digitsAndSymbolsCount;
        public boolean isBuildingNumber;
    }

    private static int countCyrillicLetters(String text) {
        if (text == null) return 0;

        Pattern pattern = Pattern.compile("[А-Яа-я]");
        ;
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
}