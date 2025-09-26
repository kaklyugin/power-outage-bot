package org.roxy.reminder.bot.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public  class AddressComponents {
    private String streetType;
    private String streetName;
    private List<String> buildingsNumbers;
}