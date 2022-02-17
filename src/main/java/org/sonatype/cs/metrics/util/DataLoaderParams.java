package org.sonatype.cs.metrics.util;

import org.springframework.stereotype.Component;

@Component
public class DataLoaderParams {
    private DataLoaderParams() {}

    public static final String SMDATAFILE = "successmetrics.csv";
    public static final String SMHEADER = "applicationId,applicationName,applicationPublicId,";

    public static final String AEDATAFILE = "reports2/application_evaluations.csv";
    public static final String AEFILEHEADER = "ApplicationName,EvaluationDate,Stage";

    public static final String CQDATAFILE = "reports2/components_in_quarantine.csv";
    public static final String CQFILEHEADER =
            "Repository,Format,PackageUrl,QuarantineTime,PolicyName,ThreatLevel";

    public static final String CWDATAFILE = "reports2/component_waivers.csv";
    public static final String CWFILEHEADER =
            "ApplicationName,Stage,PackageUrl,PolicyName,ThreatLevel,Comment";

    public static final String PVDATAFILE = "reports2/policy_violations.csv";
    public static final String PVFILEHEADER =
            "PolicyName,Reason,ApplicationName,OpenTime,Component,Stage";

    public static final String QCOMPDATAFILE =
            "reports2/quarantined_data/quarantined_components.csv";
    public static final String QCOMPHEADER =
            "repository,quarantine_date,date_cleared,path_name,format,quarantined,policy_name,threat_level,cve";

    public static final String AFQCOMPONENTDATAFILE =
            "reports2/quarantined_data/autoreleased_from_quarantine_components.csv";
    public static final String AFQCOMPONENTHEADER =
            "repository,quarantined_date,date_cleared,path_name,format,quarantined,policy_name,threat_level,cve";

    public static final String AFQCONFIGDATAFILE =
            "reports2/quarantined_data/autoreleased_from_quarantine_config.csv";
    public static final String AFQCONFIGHEADER = "id,name,autoReleaseQuarantineEnabled";

    public static final String AFQSDATAFILE =
            "reports2/quarantined_data/autoreleased_from_quarantine_summary.csv";
    public static final String QCSDATAFILE =
            "reports2/quarantined_data/quarantined_components_summary.csv";

    public static final String HTMLTEMPLATE = "templates/thymeleaf_template";
}
