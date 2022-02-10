package org.sonatype.cs.metrics.util;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.springframework.stereotype.Service;

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
                        .map(n -> n.getPointA())
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

        if (data.size() > 0) {
            map.put(key + "Data", data);
            map.put(key + "Number", data.size());
            map.put(key, true);
        } else {
            map.put(key + "Number", 0);
            map.put(key, false);
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
}
