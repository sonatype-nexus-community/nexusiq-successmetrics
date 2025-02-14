package org.sonatype.cs.getmetrics.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateCheck {
    private static final Pattern MONTH_PATTERN =
            Pattern.compile("^\\d{4}-\\d{2}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern WEEK_PATTERN =
            Pattern.compile("^\\d{4}-W\\d{2}$", Pattern.CASE_INSENSITIVE);

    public static boolean checkDateFormat(String period, String date) {
        if (!"MONTH".equalsIgnoreCase(period) && !"WEEK".equalsIgnoreCase(period)) {
            throw new IllegalArgumentException("The period must be either MONTH or WEEK");
        }

        Pattern pattern = "MONTH".equalsIgnoreCase(period) ? MONTH_PATTERN : WEEK_PATTERN;
        Matcher matcher = pattern.matcher(date);

        if (matcher.find()) {
            return true;
        } else {
            String errorMessage =
                    "iq.api.sm.period is set to "
                            + period.toUpperCase()
                            + " but the dates provided are not in the correct format. ";
            if ("MONTH".equalsIgnoreCase(period)) {
                errorMessage += "Expected format: 2022-01.";
            } else {
                errorMessage += "Expected format: 2022-W01.";
            }
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
