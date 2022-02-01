package org.sonatype.cs.metrics.util;

public class SqlStatements {

    public static final String METRICTABLENAME = "metric";
    public static final String METRICP1TABLENAME = "metric_p1";
    public static final String METRICP2TABLENAME = "metric_p2";

    public static String TimePeriods =
            "select distinct time_period_start as label from <?> order by 1";

    public static String ApplicationsOnboarded =
            "select time_period_start as label, count(application_id) as pointA from <?> group by"
                + " time_period_start order by 1 asc";

    public static String MostScannedApplications =
            "select application_name as label, sum (evaluation_count) as pointA from <?> group by"
                + " application_name order by 2 desc";

    public static String NumberOfScans =
            "select time_period_start as label, sum(evaluation_count) as pointA from <?> group by"
                + " time_period_start";

    public static String NumberOfScannedApplications =
            "select time_period_start as label, count(application_id) as pointA from <?> where"
                + " evaluation_count > 0 group by time_period_start";

    public static String ListOfApplications = "select distinct application_name as label from <?>";

    public static String MTTR =
            "select time_period_start as label, ifnull(avg(case when ifnull(mttr_critical_threat,0)"
                + " <>0 then ifnull(mttr_critical_threat,0) else null end)/86400000,0) as pointA, "
                + " ifnull(avg(case when ifnull(mttr_severe_threat,0) <> 0 then"
                + " ifnull(mttr_severe_threat,0) else null end)/86400000,0) as pointB,"
                + " ifnull(avg(case when ifnull(mttr_moderate_threat,0) <> 0 then"
                + " ifnull(mttr_moderate_threat,0) else null end)/86400000,0)  as pointC from <?>"
                + " group by time_period_start";

    public static String MTTR2 =
            "select time_period_start as label, "
                    + "ifnull(mttr_critical_threat,0) as pointA,  "
                    + "ifnull(mttr_severe_threat,0) as pointB,  "
                    + "ifnull(mttr_moderate_threat,0)  as pointC "
                    + "from <?>";

    public static String DiscoveredSecurityViolationsTotal =
            "select sum(discovered_count_security_critical) as pointA, "
                    + "sum(discovered_count_security_severe) as pointB, "
                    + "sum(discovered_count_security_moderate) as pointC "
                    + "from <?>";

    public static String OpenSecurityViolationsTotal =
            "select time_period_start  as label,"
                + " sum(open_count_at_time_period_end_security_critical) as pointA,"
                + " sum(open_count_at_time_period_end_security_severe) as pointB,"
                + " sum(open_count_at_time_period_end_security_moderate) as pointC from <?> group"
                + " by time_period_start order by 1 desc";

    public static String FixedSecurityViolationsTotal =
            "select sum(fixed_count_security_critical) as pointA, "
                    + "sum(fixed_count_security_severe) as pointB, "
                    + "sum(fixed_count_security_moderate) as pointC "
                    + "from <?>";

    public static String WaivedSecurityViolationsTotal =
            "select sum(waived_count_security_critical) as pointA, "
                    + "sum(waived_count_security_severe) as pointB, "
                    + "sum(waived_count_security_moderate) as pointC "
                    + "from <?>";

    public static String DiscoveredLicenseViolationsTotal =
            "select sum(discovered_count_license_critical) as pointA, "
                    + "sum(discovered_count_license_severe) as pointB, "
                    + "sum(discovered_count_license_moderate) as pointC "
                    + "from <?>";

    public static String OpenLicenseViolationsTotal =
            "select time_period_start as label, sum(open_count_at_time_period_end_license_critical)"
                + " as pointA, sum(open_count_at_time_period_end_license_severe) as pointB,"
                + " sum(open_count_at_time_period_end_license_moderate) as pointC from <?> group by"
                + " time_period_start order by 1 desc";

    public static String FixedLicenseViolationsTotal =
            "select sum(fixed_count_license_critical) as pointA, "
                    + "sum(fixed_count_license_severe) as pointB, "
                    + "sum(fixed_count_license_moderate) as pointC "
                    + "from <?>";

    public static String WaivedLicenseViolationsTotal =
            "select sum(waived_count_license_critical) as pointA, "
                    + "sum(waived_count_license_severe) as pointB, "
                    + "sum(waived_count_license_moderate) as pointC "
                    + "from <?>";

    public static String SecurityViolations =
            "select time_period_start as label,"
                + " (sum(discovered_count_security_critical)+sum(discovered_count_security_severe)+sum(discovered_count_security_moderate))"
                + " as pointA,"
                + " (sum(open_count_at_time_period_end_security_critical)+sum(open_count_at_time_period_end_security_severe)+sum(open_count_at_time_period_end_security_moderate))"
                + " as pointB,"
                + " (sum(fixed_count_security_critical)+sum(fixed_count_security_severe)+sum(fixed_count_security_moderate))"
                + " as pointC,"
                + " (sum(waived_count_security_critical)+sum(waived_count_security_severe)+sum(waived_count_security_moderate))"
                + " as pointD from <?> group by time_period_start";

    public static String DiscoveredSecurityViolations =
            "select time_period_start as label, "
                    + "sum(discovered_count_security_critical) as pointA, "
                    + "sum(discovered_count_security_severe) as pointB, "
                    + "sum(discovered_count_security_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String OpenSecurityViolations =
            "select time_period_start as label, "
                    + "sum(open_count_at_time_period_end_security_critical) as pointA, "
                    + "sum(open_count_at_time_period_end_security_severe) as pointB, "
                    + "sum(open_count_at_time_period_end_security_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String FixedSecurityViolations =
            "select time_period_start as label, "
                    + "sum(fixed_count_security_critical) as pointA, "
                    + "sum(fixed_count_security_severe) as pointB, "
                    + "sum(fixed_count_security_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String WaivedSecurityViolations =
            "select time_period_start as label, "
                    + "sum(waived_count_security_critical) as pointA, "
                    + "sum(waived_count_security_severe) as pointB, "
                    + "sum(waived_count_security_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String LicenseViolations =
            "select time_period_start as label,"
                + " (sum(discovered_count_license_critical)+sum(discovered_count_license_severe)+sum(discovered_count_license_moderate))"
                + " as pointA,"
                + " (sum(open_count_at_time_period_end_license_critical)+sum(open_count_at_time_period_end_license_severe)+sum(open_count_at_time_period_end_license_moderate))"
                + " as pointB,"
                + " (sum(fixed_count_license_critical)+sum(fixed_count_license_severe)+sum(fixed_count_license_moderate))"
                + " as pointC,"
                + " (sum(waived_count_license_critical)+sum(waived_count_license_severe)+sum(waived_count_license_moderate))"
                + " as pointD from <?> group by time_period_start";

    public static String DiscoveredLicenseViolations =
            "select time_period_start as label, "
                    + "sum(discovered_count_license_critical) as pointA, "
                    + "sum(discovered_count_license_severe) as pointB, "
                    + "sum(discovered_count_license_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String OpenLicenseViolations =
            "select time_period_start as label, "
                    + "sum(open_count_at_time_period_end_license_critical) as pointA, "
                    + "sum(open_count_at_time_period_end_license_severe) as pointB, "
                    + "sum(open_count_at_time_period_end_license_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String FixedLicenseViolations =
            "select time_period_start as label, "
                    + "sum(fixed_count_license_critical) as pointA, "
                    + "sum(fixed_count_license_severe) as pointB, "
                    + "sum(fixed_count_license_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String WaivedLicenseViolations =
            "select time_period_start as label, "
                    + "sum(waived_count_license_critical) as pointA, "
                    + "sum(waived_count_license_severe) as pointB, "
                    + "sum(waived_count_license_moderate) as pointC "
                    + "from <?> "
                    + "group by time_period_start";

    public static String ApplicationsOpenViolations =
            "select  distinct application_name as label,"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_CRITICAL) +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_CRITICAL) as pointA,"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_SEVERE)   +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_SEVERE)as pointB,"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_MODERATE) +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_MODERATE) as pointC, from <?>";

    public static String OrganisationsOpenViolations =
            "select  distinct organization_name as label,"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_CRITICAL) +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_CRITICAL) as pointA,"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_SEVERE)   +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_SEVERE) as pointB,"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_MODERATE) +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_MODERATE) as pointC from <?>";

    public static String ApplicationsSecurityRemediation =
            "select APPLICATION_NAME as label, sum(DISCOVERED_COUNT_SECURITY_CRITICAL) +"
                + " sum(DISCOVERED_COUNT_SECURITY_SEVERE) +"
                + " sum(DISCOVERED_COUNT_SECURITY_MODERATE)as pointA,"
                + " sum(FIXED_COUNT_SECURITY_CRITICAL) + sum(FIXED_COUNT_SECURITY_SEVERE) +"
                + " sum(FIXED_COUNT_SECURITY_MODERATE) as pointB,"
                + " sum(WAIVED_COUNT_SECURITY_CRITICAL) + sum(WAIVED_COUNT_SECURITY_SEVERE) +"
                + " sum(WAIVED_COUNT_SECURITY_MODERATE) as pointC from <?> group by"
                + " APPLICATION_NAME order by 2 desc";

    public static String ApplicationsLicenseRemediation =
            "select APPLICATION_NAME as label, sum(DISCOVERED_COUNT_LICENSE_CRITICAL) +"
                + " sum(DISCOVERED_COUNT_LICENSE_SEVERE) + sum(DISCOVERED_COUNT_LICENSE_MODERATE)as"
                + " pointA, sum(FIXED_COUNT_LICENSE_CRITICAL) + sum(FIXED_COUNT_LICENSE_SEVERE) +"
                + " sum(FIXED_COUNT_LICENSE_MODERATE) as pointB, sum(WAIVED_COUNT_LICENSE_CRITICAL)"
                + " + sum(WAIVED_COUNT_LICENSE_SEVERE) + sum(WAIVED_COUNT_LICENSE_MODERATE) as"
                + " pointC from <?> group by APPLICATION_NAME order by 2 desc";

    public static String ComponentsInQuarantine =
            "select repository as pointA, format as pointB, packageUrl as pointC, "
                    + "quarantineTime as pointD, policyName as pointE, threatLevel as pointF "
                    + "from COMPONENT_QUARANTINE order by 1 asc";

    public static String ComponentWaivers =
            "select application_name as pointA, stage as pointB, packageUrl as pointC, policyName"
                + " as pointD, threatLevel as pointE, comment as pointF, createDate as pointG,"
                + " expiryTime as pointH from COMPONENT_WAIVER order by 1 asc";

    public static String QuarantinedComponents =
            "select repository as pointA, quarantine_date as pointB, date_cleared as pointC,"
                + " path_name as pointD, format as pointE, quarantined as pointF, policy_name as"
                + " pointG, threat_level as pointH, cve as pointI from QUARANTINED_COMPONENTS order"
                + " by 6 asc";

    public static String AutoReleasedFromQuarantinedComponents =
            "select repository as pointA, quarantine_date as pointB, date_cleared as pointC,"
                + " path_name as pointD, format as pointE, quarantined as pointF, policy_name as"
                + " pointG, threat_level as pointH, cve as pointI from AUTORELEASED_FROM_QUARANTINE"
                + " order by 6 asc";

    public static String MetricsTable =
            "DROP TABLE IF EXISTS METRIC; "
                    + "CREATE TABLE METRIC ("
                    + "id INT default null, "
                    + " application_Id VARCHAR(250) DEFAULT NULL,"
                    + " application_Name VARCHAR(250) DEFAULT NULL,"
                    + " application_Public_Id VARCHAR(250) DEFAULT NULL,"
                    + " discovered_Count_License_Critical INT DEFAULT NULL,"
                    + " discovered_Count_License_Moderate INT DEFAULT NULL,"
                    + " discovered_Count_License_Severe INT DEFAULT NULL,"
                    + " discovered_Count_Security_Critical INT DEFAULT NULL,"
                    + " discovered_Count_Security_Moderate INT DEFAULT NULL,"
                    + " discovered_Count_Security_Severe INT DEFAULT NULL,"
                    + " evaluation_Count INT DEFAULT NULL,"
                    + " fixed_Count_License_Critical INT DEFAULT NULL,"
                    + " fixed_Count_License_Moderate INT DEFAULT NULL,"
                    + " fixed_Count_License_Severe INT DEFAULT NULL,"
                    + " fixed_Count_Security_Critical INT DEFAULT NULL,"
                    + " fixed_Count_Security_Moderate INT DEFAULT NULL,"
                    + " fixed_Count_Security_Severe INT DEFAULT NULL,"
                    + " mttr_Critical_Threat DOUBLE DEFAULT NULL,"
                    + " mttr_Moderate_Threat DOUBLE DEFAULT NULL,"
                    + " mttr_Severe_Threat DOUBLE DEFAULT NULL,"
                    + " open_Count_At_Time_Period_End_License_Critical INT DEFAULT NULL,"
                    + " open_Count_At_Time_Period_End_License_Moderate INT DEFAULT NULL,"
                    + " open_Count_At_Time_Period_End_License_Severe INT DEFAULT NULL,"
                    + " open_count_at_time_period_end_security_Critical INT DEFAULT NULL,"
                    + " open_count_at_time_period_end_security_Moderate INT DEFAULT NULL,"
                    + " open_count_at_time_period_end_security_Severe INT DEFAULT NULL,"
                    + " organization_Name VARCHAR(250) DEFAULT NULL,"
                    + " time_Period_Start VARCHAR(250) DEFAULT NULL,"
                    + " waived_Count_License_Critical INT DEFAULT NULL,"
                    + " waived_Count_License_Moderate INT DEFAULT NULL,"
                    + " waived_Count_License_Severe INT DEFAULT NULL,"
                    + " waived_Count_Security_Critical INT DEFAULT NULL,"
                    + " waived_Count_Security_Moderate INT DEFAULT NULL,"
                    + " waived_Count_Security_Severe INT DEFAULT NULL)"
                    + "AS SELECT "
                    + 0
                    + ", "
                    + "applicationId,"
                    + "applicationName,"
                    + "applicationPublicId,"
                    + "discoveredCountLicenseCritical,"
                    + "discoveredCountLicenseModerate,"
                    + "discoveredCountLicenseSevere,"
                    + "discoveredCountSecurityCritical,"
                    + "discoveredCountSecurityModerate,"
                    + "discoveredCountSecuritySevere,"
                    + "evaluationCount,"
                    + "fixedCountLicenseCritical,"
                    + "fixedCountLicenseModerate,"
                    + "fixedCountLicenseSevere,"
                    + "fixedCountSecurityCritical,"
                    + "fixedCountSecurityModerate,"
                    + "fixedCountSecuritySevere,"
                    + "mttrCriticalThreat,"
                    + "mttrModerateThreat,"
                    + "mttrSevereThreat,"
                    + "openCountAtTimePeriodEndLicenseCritical,"
                    + "openCountAtTimePeriodEndLicenseModerate,"
                    + "openCountAtTimePeriodEndLicenseSevere,"
                    + "openCountAtTimePeriodEndSecurityCritical,"
                    + "openCountAtTimePeriodEndSecurityModerate,"
                    + "openCountAtTimePeriodEndSecuritySevere,"
                    + "organizationName,"
                    + "timePeriodStart,"
                    + "waivedCountLicenseCritical,"
                    + "waivedCountLicenseModerate,"
                    + "waivedCountLicenseSevere,"
                    + "waivedCountSecurityCritical,"
                    + "waivedCountSecurityModerate,"
                    + "waivedCountSecuritySevere "
                    + " from csvread ";

    public static String PolicyViolationsTables =
            "DROP TABLE IF EXISTS POLICY_VIOLATION;CREATE TABLE POLICY_VIOLATION (  policy_name"
                + " VARCHAR(250) NOT NULL,  reason VARCHAR(250) NOT NULL,  application_name"
                + " VARCHAR(250) NOT NULL,  open_time VARCHAR(250) DEFAULT NULL,  component"
                + " VARCHAR(250) DEFAULT NULL,  stage VARCHAR(250) DEFAULT NULL)  AS SELECT"
                + " policyname, reason, applicationname, parsedatetime(opentime, 'yyyy-MM-dd',"
                + " 'en'), component, stage FROM CSVREAD ";

    public static String ApplicationEvaluationsTable =
            "DROP TABLE IF EXISTS APPLICATION_EVALUATION;CREATE TABLE APPLICATION_EVALUATION ( "
                + " application_name VARCHAR(250) NOT NULL,  evaluation_date VARCHAR(250) DEFAULT"
                + " NULL,  stage VARCHAR(250) DEFAULT NULL)  AS SELECT applicationname,"
                + " parsedatetime(evaluationdate, 'yyyy-MM-dd', 'en'), stage FROM CSVREAD ";

    public static String ComponentsInQuarantineTable =
            "DROP TABLE IF EXISTS COMPONENT_QUARANTINE;CREATE TABLE COMPONENT_QUARANTINE ( "
                + " repository VARCHAR(250) NOT NULL,  format VARCHAR(250) NOT NULL,  packageUrl"
                + " VARCHAR(250) NOT NULL,  quarantineTime VARCHAR(250) DEFAULT NULL,  policyName"
                + " VARCHAR(250) DEFAULT NULL,  threatLevel VARCHAR(250) DEFAULT NULL)  AS SELECT"
                + " repository, format, packageUrl, parsedatetime(quarantineTime, 'yyyy-MM-dd',"
                + " 'en'), policyName, threatLevel FROM CSVREAD ";

    public static String ComponentWaiversTable =
            "DROP TABLE IF EXISTS COMPONENT_WAIVER;CREATE TABLE COMPONENT_WAIVER ( "
                + " application_name VARCHAR(250) NOT NULL,  stage VARCHAR(250) NOT NULL, "
                + " packageUrl VARCHAR(250) NOT NULL,  policyName VARCHAR(250) DEFAULT NULL, "
                + " threatLevel VARCHAR(250) DEFAULT NULL,  comment VARCHAR(1024) DEFAULT NULL, "
                + " createDate VARCHAR(250) DEFAULT NULL,  expiryTime VARCHAR(250) DEFAULT NULL) "
                + " AS SELECT applicationName, stage, packageUrl, policyName, threatLevel, comment,"
                + " createDate, expiryTime FROM CSVREAD ";

    public static String RiskRatio =
            "select time_period_start as label,"
                + " (sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_CRITICAL) +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_CRITICAL))/count(time_period_start)"
                + " as pointA from <?> group by time_period_start order by 1";

    public static String RiskRatioAnalysis =
            "select application_name as label,"
                + " (sum(OPEN_COUNT_AT_TIME_PERIOD_END_SECURITY_CRITICAL) +"
                + " sum(OPEN_COUNT_AT_TIME_PERIOD_END_LICENSE_CRITICAL)) as pointA from <?> group"
                + " by application_name order by 1";

    public static String QuarantinedComponentsTable =
            "DROP TABLE IF EXISTS QUARANTINED_COMPONENTS;CREATE TABLE QUARANTINED_COMPONENTS ( "
                + " repository VARCHAR(250) NOT NULL,  quarantine_date VARCHAR(250) NOT NULL, "
                + " date_cleared VARCHAR(250) DEFAULT NULL,  path_name VARCHAR(250) DEFAULT NULL, "
                + " format VARCHAR(250) DEFAULT NULL,  quarantined VARCHAR(1024) DEFAULT NULL, "
                + " policy_name VARCHAR(250) DEFAULT NULL,  threat_level VARCHAR(250) DEFAULT NULL,"
                + "   cve VARCHAR(250) DEFAULT NULL)  AS SELECT repository, quarantine_date,"
                + " date_cleared, path_name, format, quarantined, policy_name, threat_level, cve"
                + " FROM CSVREAD ";

    public static String AutoreleasedFromQuarantinedComponentsTable =
            "DROP TABLE IF EXISTS AUTORELEASED_FROM_QUARANTINE;CREATE TABLE"
                + " AUTORELEASED_FROM_QUARANTINE (  repository VARCHAR(250) NOT NULL, "
                + " quarantine_date VARCHAR(250) NOT NULL,  date_cleared VARCHAR(250) DEFAULT NULL,"
                + "  path_name VARCHAR(250) DEFAULT NULL,  format VARCHAR(250) DEFAULT NULL, "
                + " quarantined VARCHAR(1024) DEFAULT NULL,  policy_name VARCHAR(250) DEFAULT NULL,"
                + "  threat_level VARCHAR(250) DEFAULT NULL,   cve VARCHAR(250) DEFAULT NULL)  AS"
                + " SELECT repository, quarantine_date, date_cleared, path_name, format,"
                + " quarantined, policy_name, threat_level, cve FROM CSVREAD ";
}
