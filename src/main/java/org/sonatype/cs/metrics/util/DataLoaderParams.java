package org.sonatype.cs.metrics.util;

import org.springframework.stereotype.Component;

@Component
public class DataLoaderParams {

    public static final String smDatafile = "successmetrics.csv";
    public static final String smHeader = "applicationId,applicationName,applicationPublicId,";

    public static final String aeDatafile = "reports2/application_evaluations.csv";
    public static final String aeFileHeader = "ApplicationName,EvaluationDate,Stage";

    public static final String cqDatafile = "reports2/components_in_quarantine.csv";
    public static final String cqFileHeader =
            "Repository,Format,PackageUrl,QuarantineTime,PolicyName,ThreatLevel";

    public static final String cwDatafile = "reports2/component_waivers.csv";
    public static final String cwFileHeader =
            "ApplicationName,Stage,PackageUrl,PolicyName,ThreatLevel,Comment";

    public static final String pvDatafile = "reports2/policy_violations.csv";
    public static final String pvFileHeader =
            "PolicyName,Reason,ApplicationName,OpenTime,Component,Stage";

    public static final String qcompDatafile =
            "reports2/quarantined_data/quarantined_components.csv";
    public static final String qcompHeader =
            "repository,quarantine_date,date_cleared,path_name,format,quarantined,policy_name,threat_level,cve";

    public static final String afqcomponentDatafile =
            "reports2/quarantined_data/autoreleased_from_quarantine_components.csv";
    public static final String afqcomponentHeader =
            "repository,quarantined_date,date_cleared,path_name,format,quarantined,policy_name,threat_level,cve";

    public static final String afqconfigDatafile =
            "reports2/quarantined_data/autoreleased_from_quarantine_config.csv";
    public static final String afqconfigHeader = "id,name,autoReleaseQuarantineEnabled";

    public static final String afqsDatafile =
            "reports2/quarantined_data/autoreleased_from_quarantine_summary.csv";
    public static final String qcsDatafile =
            "reports2/quarantined_data/quarantined_components_summary.csv";

    public static final String htmlTemplate = "templates/thymeleaf_template";
}
