package cdw.springboot.gatekeeper.utils;

import cdw.springboot.gatekeeper.constants.AppConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtilities {
    public static LocalDate stringToLocaleDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.FORMAT_DATE);
        return LocalDate.parse(date, formatter);
    }
}
