package org.sonatype.cs.metrics.service;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PeriodsDataService {

    private DbService dbService;

    private static long oneMonthMs = 2629800000L;

    public PeriodsDataService(DbService dbService) {
        this.dbService = dbService;
    }

    public Map<String, Object> getPeriodData(String tableName) {
        Map<String, Object> model = new HashMap<>();
        model.put("doAnalysis", false);

        List<DbRow> timePeriods = dbService.runSql(tableName, SqlStatements.TIMEPERIODS);
        model.put("timePeriodsList", timePeriods);

        int numberOfPeriods = timePeriods.size();

        String startPeriod = timePeriods.get(0).getLabel();
        String firstTimePeriod = startPeriod;

        String endPeriod = null;

        String secondTimePeriod = null;

        if (numberOfPeriods == 1) {
            endPeriod = startPeriod;
            secondTimePeriod = startPeriod;
        } else {
            endPeriod = timePeriods.get(timePeriods.size() - 1).getLabel();
            secondTimePeriod = timePeriods.get(1).getLabel();
        }

        String timePeriodFrequency =
                this.getTimePeriodFrequency(numberOfPeriods, firstTimePeriod, secondTimePeriod);

        model.put("timePeriodFrequency", timePeriodFrequency);
        model.put("startPeriod", startPeriod);
        model.put("endPeriod", endPeriod);
        model.put("periodDateRangeStr", "(" + startPeriod + " - " + endPeriod + ")");

        if (numberOfPeriods > 2) {
            int midPeriodIndex = numberOfPeriods / 2;

            if (numberOfPeriods % 2 == 0) {
                midPeriodIndex--;
            }

            String midPeriod = timePeriods.get(midPeriodIndex).getLabel();

            String midPeriodEnd = midPeriod;
            String midPeriodStart = timePeriods.get(midPeriodIndex + 1).getLabel();

            model.put("midPeriod", midPeriod);
            model.put("midPeriodDate1RangeStr", "(" + startPeriod + " - " + midPeriodEnd + ")");
            model.put("midPeriodDate2RangeStr", "(" + midPeriodStart + " - " + endPeriod + ")");
            model.put("doAnalysis", true);
        }

        return model;
    }

    private String getTimePeriodFrequency(
            int tmSize, String firstTimePeriod, String secondTimePeriod) {
        if (tmSize > 1) {
            long fp = HelperService.convertDateStr(firstTimePeriod);
            long sp = HelperService.convertDateStr(secondTimePeriod);
            long diff = sp - fp;

            if (diff >= oneMonthMs) {
                return "month";
            }
        }
        return "week";
    }
}
