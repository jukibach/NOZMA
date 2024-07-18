package com.ecommerce.userservice.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    
    private DateUtil() {}
    
    private static final String YEAR_MONTH_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static DateTimeFormatter formatYearMonthHourMinuteSecond() {
        return DateTimeFormatter.ofPattern(YEAR_MONTH_HOUR_MINUTE_SECOND);
    }
    public static LocalDateTime toLocalDateTime(String stringValue) {
        return LocalDateTime.parse(stringValue, DateUtil.formatYearMonthHourMinuteSecond());
    }
    
    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return formatYearMonthHourMinuteSecond().format(localDateTime);
    }
}
