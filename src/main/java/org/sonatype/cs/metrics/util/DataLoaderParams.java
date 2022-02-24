package org.sonatype.cs.metrics.util;

import org.springframework.stereotype.Component;

@Component
public class DataLoaderParams {
    private DataLoaderParams() {}

    public static final String SMDATAFILE = "successmetrics.csv";
    public static final String SMHEADER = "applicationId,applicationName,applicationPublicId,";

    public static final String AEDATAFILE = "application_evaluations.csv";
    public static final String AEFILEHEADER = "applicationName,evaluationDate,stage";

    public static final String CWDATAFILE = "waivers.csv";
    public static final String CWFILEHEADER = "applicationName,stage,packageUrl,policyName,threatLevel,comment,createDate,expiryTime";

    public static final String PVDATAFILE = "policy_violations.csv";
    public static final String PVFILEHEADER = "policyName,reason,applicationName,openTime,component,stage,threatLevel";

    public static final String QCOMPDATAFILE = "quarantined_components.csv";
    public static final String QCOMPHEADER = "repository,quarantineDate,dateCleared,displayName,format,quarantined,policyName,threatLevel,reason";

    public static final String QSCOMPDATAFILE = "quarantined_components_summary.csv";
    public static final String QSCOMPHEADER = "repositoryCount,quarantineEnabledCount,quarantineenabled,totalComponentCount,quarantinedComponentCount";

    public static final String AFQCOMPONENTDATAFILE = "autoreleased_from_quarantine_components.csv";
    public static final String AFQCOMPONENTHEADER = "repository,quarantineDate,dateCleared,displayName,format,quarantined,policyName,threatLevel,reason";

    public static final String AFQSDATAFILE = "autoreleased_from_quarantine_summary.csv";
    public static final String AFQSHEADER = "MTD,YTD";

    public static final String AFQCONFIGDATAFILE = "autoreleased_from_quarantine_config.csv";
    public static final String AFQCONFIGHEADER = "id,name,autoReleaseQuarantineEnabled";

    public static final String HTMLTEMPLATE = "templates/thymeleaf_template";
}
