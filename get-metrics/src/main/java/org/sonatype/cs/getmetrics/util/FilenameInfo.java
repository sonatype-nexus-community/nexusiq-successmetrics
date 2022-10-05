package org.sonatype.cs.getmetrics.util;

import org.springframework.stereotype.Component;

@Component
public class FilenameInfo {

    public static final String successMetricsCsvFile = "successmetrics.csv";

    public static final String applicationEvaluationsCsvFile = "application_evaluations.csv";

    public static final String policyViolationsCsvFile = "policy_violations.csv";

    public static final String waiversCsvFile = "waivers.csv";

    public static final String quarantinedComponentsCsvFile = "quarantined_components.csv";

    public static final String quarantinedComponentsSummaryCsvFile =
            "quarantined_components_summary.csv";

    public static final String autoReleasedFromQuarantineComponentsCsvFile =
            "autoreleased_from_quarantine_components.csv";

    public static final String autoReleasedFromQuarantineSummaryCsvFile =
            "autoreleased_from_quarantine_components_summary.csv";

    public static final String autoReleasedFromQuarantineConfigCsvFile =
            "autoreleased_from_quarantine_config.csv";

    private FilenameInfo() {
        /* empty since this is a utility class */
    }
}
