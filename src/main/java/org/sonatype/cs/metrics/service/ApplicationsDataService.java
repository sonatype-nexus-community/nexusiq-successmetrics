package org.sonatype.cs.metrics.service;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.Mttr;
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationsDataService {
    @Autowired private DbService dbService;

    public Map<String, Object> getApplicationData(
            String tableName, Map<String, Object> periodsData) {
        Map<String, Object> model = new HashMap<>();

        List<DbRow> applicationsOnboardedData =
                dbService.runSql(tableName, SqlStatements.APPLICATIONSONBOARDED);
        int rows = applicationsOnboardedData.size();

        String endPeriod = null;

        int startPeriodCount = applicationsOnboardedData.get(0).getPointA();
        int endPeriodCount = applicationsOnboardedData.get(rows - 1).getPointA();

        int applicationsOnboardedInPeriod = applicationsOnboardedData.get(rows - 1).getPointA();

        switch (tableName) {
            case SqlStatements.METRICTABLENAME:
                endPeriod = periodsData.get("endPeriod").toString();
                break;

            case SqlStatements.METRICP1TABLENAME:
                endPeriod = periodsData.get("midPeriod").toString();
                break;

            case SqlStatements.METRICP2TABLENAME:
                endPeriod = periodsData.get("endPeriod").toString();
                break;
            default:
        }

        int applicationsOnboardedInPeriodAvg = applicationsOnboardedInPeriod / rows;
        if (applicationsOnboardedInPeriodAvg == 0) {
            applicationsOnboardedInPeriodAvg = 1;
        }

        model.put("startPeriodCount", startPeriodCount);
        model.put("endPeriodCount", endPeriodCount);
        model.put("numberOfPeriods", rows);
        model.put("applicationsOnboardedChart", applicationsOnboardedData);
        model.put("applicationsOnboarded", applicationsOnboardedInPeriod);
        model.put("applicationsOnboardedAvg", applicationsOnboardedInPeriodAvg);

        model.put("applicationReport", applicationsOnboardedInPeriod == 1);

        List<DbRow> numberOfScansData = dbService.runSql(tableName, SqlStatements.NUMBEROFSCANS);
        int[] numberOfScans = HelperService.getPointsSumAndAverage(numberOfScansData);
        model.put("numberOfScansChart", numberOfScansData);
        model.put("numberOfScans", numberOfScans[0]);
        model.put("numberOfScansAvg", numberOfScans[1]);

        List<DbRow> numberOfScannedApplicationsData =
                dbService.runSql(tableName, SqlStatements.NUMBEROFSCANNEDAPPLICATIONS);
        int[] numberOfScannedApplications =
                HelperService.getPointsSumAndAverage(numberOfScannedApplicationsData);
        model.put("numberOfApplicationsScannedChart", numberOfScannedApplicationsData);
        model.put("numberOfApplicationsScanned", numberOfScannedApplications[0]);
        model.put("numberOfApplicationsScannedAvg", numberOfScannedApplications[1]);

        List<Mttr> mttr = dbService.runSqlMttr(tableName, SqlStatements.MTTR);
        model.put("mttrChart", mttr);

        String applicationOpenViolations =
                SqlStatements.APPLICATIONSOPENVIOLATIONS
                        + " where time_period_start = '"
                        + endPeriod
                        + "' group by application_name"
                        + " order by 2 desc, 3 desc";
        List<DbRow> aov = dbService.runSql(tableName, applicationOpenViolations);

        String organisationOpenViolations =
                SqlStatements.ORGANISATIONSOPENVIOLATIONS
                        + " where time_period_start = '"
                        + endPeriod
                        + "' group by organization_name"
                        + " order by 2 desc, 3 desc";
        List<DbRow> oov = dbService.runSql(tableName, organisationOpenViolations);

        if (!aov.isEmpty()) {
            model.put("mostCriticalApplicationCount", aov.get(0).getPointA());
            model.put("leastCriticalApplicationCount", aov.get(aov.size() - 1).getPointA());
            model.put("openCriticalViolationsAvg", HelperService.getPointsSumAndAverage(aov)[1]);
            model.put("mostCriticalApplicationName", aov.get(0).getLabel());
            model.put("leastCriticalApplicationName", aov.get(aov.size() - 1).getLabel());

            model.put(
                    "applicationsSecurityRemediation",
                    dbService.runSql(tableName, SqlStatements.APPLICATIONSSECURITYREMEDIATION));
            model.put(
                    "applicationsLicenseRemediation",
                    dbService.runSql(tableName, SqlStatements.APPLICATIONSLICENSEREMEDIATION));

            model.put("mostCriticalOrganisationsData", oov);
            model.put("mostCriticalApplicationsData", aov);

            model.put(
                    "mostScannedApplicationsData",
                    dbService.runSql(tableName, SqlStatements.MOSTSCANNEDAPPLICATIONS));
        }

        List<DbRow> riskRatio = dbService.runSql(tableName, SqlStatements.RISKRATIO);
        model.put("riskRatioChart", riskRatio);
        model.put("riskRatioAtEndPeriod", riskRatio.get(riskRatio.size() - 1).getPointA());
        model.put("riskRatioAtStartPeriod", riskRatio.get(0).getPointA());

        return model;
    }

    public boolean applicationExists(String applicationName) {
        List<DbRow> applications =
                dbService.runSql(SqlStatements.METRICTABLENAME, SqlStatements.LISTOFAPPLICATIONS);
        boolean status = false;

        for (DbRow r : applications) {
            if (r.getLabel().equalsIgnoreCase(applicationName)) {
                status = true;
                break;
            }
        }

        return status;
    }
}
