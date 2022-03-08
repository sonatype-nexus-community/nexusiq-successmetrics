package org.sonatype.cs.getmetrics.util;

import org.springframework.stereotype.Component;

@Component
public class FilenameInfo {

    public static final String successMetricsCsvFile = "successmetrics.csv";

    public static final String applicationEvaluationsCsvFile = "application_evaluations.csv";
    public static final String[] applicationEvaluationsFileHeader = {
        "applicationName", "evaluationDate", "stage"
    };

    public static final String policyViolationsCsvFile = "policy_violations.csv";
    public static final String[] policyViolationsFileHeader = {
        "policyName", "reason", "applicationName", "openTime", "component", "stage", "threatLevel"
    };

    public static final String waiversCsvFile = "waivers.csv";
    public static final String[] waiversFileHeader = {
        "applicationName",
        "stage",
        "packageUrl",
        "policyName",
        "threatLevel",
        "comment",
        "createDate",
        "expiryTime"
    };

    public static final String quarantinedComponentsCsvFile = "quarantined_components.csv";
    public static final String[] quarantinedComponentsFileHeader = {
        "repository",
        "quarantineDate",
        "dateCleared",
        "displayName",
        "format",
        "quarantined",
        "policyName",
        "threatLevel",
        "reason"
    };

    public static final String quarantinedComponentsSummaryCsvFile =
            "quarantined_components_summary.csv";
    public static final String[] quarantinedComponentsSummaryFileHeader = {
        "repositoryCount",
        "quarantineEnabledCount",
        "quarantineEnabled",
        "totalComponentCount",
        "quarantinedComponentCount"
    };

    public static final String autoReleasedFromQuarantineComponentsCsvFile =
            "autoreleased_from_quarantine_components.csv";
    public static final String[] autoReleasedFromQuarantineComponentsFileHeader = {
        "repository",
        "quarantineDate",
        "dateCleared",
        "displayName",
        "format",
        "quarantined",
        "policyName",
        "threatLevel",
        "reason"
    };

    public static final String autoReleasedFromQuarantineSummaryCsvFile =
            "autoreleased_from_quarantine_components_summary.csv";
    public static final String[] autoReleasedFromQuarantineSummaryFileHeader = {"MTD", "YTD"};

    public static final String autoReleasedFromQuarantineConfigCsvFile =
            "autoreleased_from_quarantine_config.csv";
    public static final String[] autoReleasedFromQuarantineConfigFileHeader = {
        "id", "name", "autoReleaseQuarantineEnabled"
    };

    private FilenameInfo() {
        /* empty since this is a utility class */
    }
}
