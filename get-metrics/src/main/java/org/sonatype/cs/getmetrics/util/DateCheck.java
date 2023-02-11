package org.sonatype.cs.getmetrics.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateCheck {
    public static boolean checkDateFormat(String period, String date) {
        if (!period.toUpperCase().equals("MONTH") && !period.toUpperCase().equals("WEEK")) {
            throw new IllegalArgumentException("The period must be either MONTH or WEEK");
        }
        if (period.toUpperCase().equals("MONTH")) {
            Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(date);
            if (matcher.find()) {
                return true;
            } else {
                throw new IllegalArgumentException(
                        "iq.api.sm.period is set to MONTH but the dates prodived are not in 2022-01"
                                + " format. Perhaps there is a W in there for week format?");
            }
        }
        Pattern pattern = Pattern.compile("^\\d{4}-W\\d{2}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(date);
        if (matcher.find()) {
            return true;
        } else {
            throw new IllegalArgumentException(
                    "iq.api.sm.period is set to WEEK but the dates prodived are not in 2022-W01"
                            + " format. Perhaps you've missed out the W?");
        }
    }
}
