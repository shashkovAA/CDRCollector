package objects;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class MyCalendar {

    public static String getCurrentDayDateTimeNow() {
	LocalDateTime now = LocalDateTime.now();
	return now.format(getDateTimeFormat());
    }

    public static String getCurrentDate() {
	LocalDate today = LocalDate.now();
	return today.format(getDateFormat());
    }

    public static String getCurrentDayDateTimeBegin() {
	return getCurrentDate() + " 00:00";
    }

    public static String getCurrentDayDateTimeEnd() {
	return getCurrentDate() + " 23:59";
    }

    public static String getMondayDateTimeOfCurrentWeek() {
	LocalDate today = LocalDate.now();
	LocalDate monday = today;
	while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
	    monday = monday.minusDays(1);
	}
	return monday.format(getDateFormat()) + " 00:00";
    }

    public static String getFirstDayDateTimeOfCurrentMonth() {
	LocalDate today = LocalDate.now();
	return today.with(TemporalAdjusters.firstDayOfMonth()).format(getDateFormat()) + " 00:00";
    }

    public static String getFirstDayDateTimeOfCurrentYear() {
	LocalDate today = LocalDate.now();
	return today.with(TemporalAdjusters.firstDayOfYear()).format(getDateFormat()) + " 00:00";
    }

    private static DateTimeFormatter getDateFormat() {
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	return dateFormatter;
    }

    private static DateTimeFormatter getDateTimeFormat() {
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	return dateTimeFormatter;
    }
}