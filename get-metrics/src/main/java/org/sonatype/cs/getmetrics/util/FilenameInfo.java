package org.sonatype.cs.getmetrics.util;

import org.springframework.stereotype.Component;

@Component
public final class FilenameInfo {

    public static final String SUCCESS_METRICS_CSV_FILE = "successmetrics.csv";
    public static final String APPLICATION_EVALUATIONS_CSV_FILE = "application_evaluations.csv";
    public static final String POLICY_VIOLATIONS_CSV_FILE = "policy_violations.csv";
    public static final String WAIVERS_CSV_FILE = "waivers.csv";
    public static final String QUARANTINED_COMPONENTS_CSV_FILE = "quarantined_components.csv";
    public static final String QUARANTINED_COMPONENTS_SUMMARY_CSV_FILE =
            "quarantined_components_summary.csv";
    public static final String AUTO_RELEASED_FROM_QUARANTINE_COMPONENTS_CSV_FILE =
            "autoreleased_from_quarantine_components.csv";
    public static final String AUTO_RELEASED_FROM_QUARANTINE_SUMMARY_CSV_FILE =
            "autoreleased_from_quarantine_components_summary.csv";
    public static final String AUTO_RELEASED_FROM_QUARANTINE_CONFIG_CSV_FILE =
            "autoreleased_from_quarantine_config.csv";

    private FilenameInfo() {
        // Prevent instantiation
    }
}
