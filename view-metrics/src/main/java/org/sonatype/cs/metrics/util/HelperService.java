package org.sonatype.cs.metrics.util;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

@Service
public class HelperService {

    private HelperService() {}

    public static int[] getPointsSumAndAverage(List<DbRow> dataList) {
        IntSummaryStatistics dataListStatistics =
                dataList.stream()
                        .map(DbRow::getPointA)
                        .filter(n -> (n != 0))
                        .mapToInt(n -> n)
                        .summaryStatistics();
        return new int[] {(int) dataListStatistics.getSum(), (int) dataListStatistics.getAverage()};
    }

    public static Float getPointsAverage(List<Float> points) {
        return (float)
                points.stream()
                        .filter(f -> (f != 0))
                        .mapToDouble(d -> d)
                        .summaryStatistics()
                        .getAverage();
    }

    public static Map<String, Object> dataMap(String key, List<DbRowStr> data) {

        Map<String, Object> map = new HashMap<>();

        if (data.isEmpty()) {
            map.put(key + "Number", 0);
            map.put(key, false);
        } else {
            map.put(key + "Data", data);
            map.put(key + "Number", data.size());
            map.put(key, true);
        }

        return map;
    }

    public static String calculateDivision(float a, float b) {
        String result;

        if (a > 0 && b > 0) {
            double increase = (double) a / b;
            result = String.format("%.2f", increase);
        } else {
            result = "0";
        }

        return result;
    }

    public static Long convertDateStr(String str) {
        str += " 00:00";
        LocalDateTime localDate =
                LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return localDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Double roundDouble(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Float roundFloat(Float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    public static LocalDateTime latestDate(LocalDateTime value1, LocalDateTime value2) {
        if (value1 == null) {
            return value2;
        }
        if (value2 == null) {
            return value1;
        }
        return value1.isAfter(value2) ? value1 : value2;
    }
}
