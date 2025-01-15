
package com.tranphucvinh.config.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * check exists for optional argument
     *
     * @param prm
     * @return
     */
    public static boolean isOptExists(Object[] prm) {
        return Objects.nonNull(prm) && prm.length > 0 && Objects.nonNull(prm[0]);
    }

    /**
     * list all times between minute
     *
     * @param eachMinute
     * @return
     */
    public static List<String> times(int eachMinute) {
        List<String> quarterHours = quarterHours(eachMinute);
        List<String> times = new ArrayList<>(); // <-- List instead of array
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < quarterHours.size(); j++) {
                String time = i + ":" + quarterHours.get(j);
                if (i < 10) {
                    time = "0" + time;
                }
                times.add(time); // <-- no need to care about indexes
            }
        }
        return times;
    }

    /**
     * list quarter hours for each minute
     *
     * @param eachMinute
     * @return
     */
    public static List<String> quarterHours(int eachMinute) {
        DecimalFormat formatter = new DecimalFormat("00");
        List<String> quarterHours = new ArrayList<>();
        for (int m = 0; m < 60; m++) {
            if ((m % eachMinute) == 0) {
                quarterHours.add(formatter.format(m));
            }
        }
        return quarterHours;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (Objects.isNull(queryString)) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public static String getPath(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return path;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
    }

    public static <T, C extends Collection<T>> C typesafeAdd(Iterable<?> from, C to, Class<T> listClass) {
        for (Object item : from) {
            to.add(listClass.cast(item));
        }
        return to;
    }

    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes reqAttr = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servlReqAttr = (ServletRequestAttributes)reqAttr;
        HttpServletRequest req = servlReqAttr.getRequest();
        return req;
    }

    public static String getRequestUri() {
        HttpServletRequest req = getHttpServletRequest();
        return req.getRequestURI().toString();
    }

    public static InputStream readResourceFile(String filePath) throws IOException {
        return Utils.class.getClassLoader().getResourceAsStream(filePath);
    }

    public static String toJsonString(Map<String, ? extends Object> data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    public static String formatNumber(String number, char groupSeparator, String pattern) {
        try {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(groupSeparator);

            DecimalFormat formatter = new DecimalFormat(pattern, symbols);

            BigDecimal bd = new BigDecimal(number);
            return formatter.format(bd.longValue());
        } catch (Exception e) {
            return number;
        }
    }

    public static boolean isDateBetween(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
        return (dateToCheck.isEqual(startDate) || dateToCheck.isAfter(startDate)) &&
               (dateToCheck.isEqual(endDate) || dateToCheck.isBefore(endDate));
    }

    public static List<LocalDate> getAllDatesDisplayInCalendar(LocalDate date) {
        List<LocalDate> dates = new ArrayList<>();
        // Find the first and last day of the month
        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

        // Calculate the first Sunday before or equal to the first day of the month
        LocalDate start = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // Calculate the last Saturday after or equal to the last day of the month
        LocalDate end = lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        // Loop through and print all dates
        LocalDate current = start;
        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);
        }

        return dates;
    }

    public static String toDateString(LocalDate date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String toDateString(LocalDate date, String pattern) {
        if (Objects.isNull(date)) {
            return "";
        }

        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String toTimeString(LocalTime time) {
        if (Objects.isNull(time)) {
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static String toTimeString(LocalTime time, String pattern) {
        if (Objects.isNull(time)) {
            return "";
        }

        if (StringUtils.isEmpty(pattern)) {
            pattern = "HH:mm:ss";
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String toDateTimeString(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String toDateTimeString(LocalDateTime dateTime, String pattern) {
        if (Objects.isNull(dateTime)) {
            return "";
        }

        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
