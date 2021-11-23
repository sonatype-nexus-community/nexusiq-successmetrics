package org.sonatype.cs.metrics.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.Mttr;
import org.sonatype.cs.metrics.model.PayloadItem;
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationsDataService {
	private static final Logger log = LoggerFactory.getLogger(ApplicationsDataService.class);
	
	@Autowired
	private DbService dbService;
	
	@Autowired
	private HelperService helperService;
	
	@Value("${iq.api.payload.application.name}")
	private String iqApiApplicationName;
	
	@Value("${iq.api.payload.organisation.name}")
	private String iqApiOrganisationName;
	
	public Map<String, Object> getApplicationData(String tableName, Map<String, Object> periodsData) throws ParseException {
		Map<String, Object> model = new HashMap<>();
		
		List<DbRow> applicationsOnboardedData = dbService.runSql(tableName, SqlStatements.ApplicationsOnboarded);
		int rows = applicationsOnboardedData.size();

		String startPeriod = null;
		String endPeriod = null;
		
		int startPeriodCount = applicationsOnboardedData.get(0).getPointA();
		int endPeriodCount = applicationsOnboardedData.get(rows-1).getPointA();
		//int applicationsOnboardedInPeriod = endPeriodCount - startPeriodCount;

		int applicationsOnboardedInPeriod = applicationsOnboardedData.get(rows-1).getPointA();

		switch(tableName) {
			case SqlStatements.METRICTABLENAME: 
				startPeriod = periodsData.get("startPeriod").toString();
				endPeriod = periodsData.get("endPeriod").toString();
				break;
				
			case SqlStatements.METRICP1TABLENAME: 
				startPeriod = periodsData.get("startPeriod").toString();
				endPeriod = periodsData.get("midPeriod").toString();
				break;
				
			case SqlStatements.METRICP2TABLENAME: 
				startPeriod = periodsData.get("midPeriod").toString();
				endPeriod = periodsData.get("endPeriod").toString();
				
//				List<DbRow> p1applicationsOnboardedData = dbService.runSql(SqlStatements.METRICP1TABLENAME, SqlStatements.ApplicationsOnboarded);
//				int p1rows = p1applicationsOnboardedData.size();
//				int p1endPeriodCount = p1applicationsOnboardedData.get(p1rows-1).getPointA();
//				int applicationsOnboardedMidPeriod = startPeriodCount - p1endPeriodCount;
//				applicationsOnboardedInPeriod = applicationsOnboardedInPeriod + applicationsOnboardedMidPeriod;
//				rows++;

				break;
			default:
		}
		
		int applicationsOnboardedInPeriodAvg = applicationsOnboardedInPeriod/rows;
		if (applicationsOnboardedInPeriodAvg == 0) {
			applicationsOnboardedInPeriodAvg = 1;
		}

		model.put("startPeriodCount", startPeriodCount);
		model.put("endPeriodCount", endPeriodCount);
		model.put("numberOfPeriods", rows);
		model.put("applicationsOnboardedChart", applicationsOnboardedData);
		model.put("applicationsOnboarded", applicationsOnboardedInPeriod);
		model.put("applicationsOnboardedAvg", applicationsOnboardedInPeriodAvg);

		if (applicationsOnboardedInPeriod == 1){
			model.put("applicationReport", true);
		}
		else {
			model.put("applicationReport", false);
		}

		PayloadItem organisationName = new PayloadItem(iqApiOrganisationName, false);
		PayloadItem applicationName = new PayloadItem(iqApiApplicationName, false);

		if (organisationName.isExists()){
			model.put("orgOrAppName", "Organisation: " + iqApiOrganisationName);
		}
		else if (applicationName.isExists()){
			model.put("orgOrAppName", "Application: " + iqApiApplicationName);
		}
		else {
			model.put("orgOrAppName", "");
		}
	
		List<DbRow> numberOfScansData = dbService.runSql(tableName, SqlStatements.NumberOfScans);
		int[] numberOfScans = helperService.getPointsSumAndAverage(numberOfScansData);
		model.put("numberOfScansChart", numberOfScansData);
		model.put("numberOfScans", numberOfScans[0]);
		model.put("numberOfScansAvg", numberOfScans[1]);

		List<DbRow> numberOfScannedApplicationsData = dbService.runSql(tableName, SqlStatements.NumberOfScannedApplications);
		int[] numberOfScannedApplications = helperService.getPointsSumAndAverage(numberOfScannedApplicationsData);
		model.put("numberOfApplicationsScannedChart", numberOfScannedApplicationsData);
		model.put("numberOfApplicationsScanned", numberOfScannedApplications[0]);
		model.put("numberOfApplicationsScannedAvg", numberOfScannedApplications[1]);

		List<Mttr> mttr = dbService.runSqlMttr(tableName, SqlStatements.MTTR);
		model.put("mttrChart", mttr);
		
		String applicationOpenViolations = SqlStatements.ApplicationsOpenViolations + " where time_period_start = '" + endPeriod + "' group by application_name" + " order by 2 desc, 3 desc";
		List<DbRow> aov = dbService.runSql(tableName, applicationOpenViolations);

        String organisationOpenViolations = SqlStatements.OrganisationsOpenViolations + " where time_period_start = '" + endPeriod + "' group by organization_name" + " order by 2 desc, 3 desc";
        List<DbRow> oov = dbService.runSql(tableName, organisationOpenViolations);

        if (aov.size() > 0) {
			model.put("mostCriticalApplicationCount", aov.get(0).getPointA());
			model.put("leastCriticalApplicationCount", aov.get(aov.size() - 1).getPointA());
			
			model.put("openCriticalViolationsAvg", helperService.getPointsSumAndAverage(aov)[1]);
			
	        model.put("mostCriticalApplicationName", aov.get(0).getLabel());
	        model.put("mostCriticalApplicationCount", aov.get(0).getPointA());
	        
	        model.put("leastCriticalApplicationName", aov.get(aov.size()-1).getLabel());
	        model.put("leastCriticalApplicationCount", aov.get(aov.size()-1).getPointA());
	        
	        model.put("applicationsSecurityRemediation", dbService.runSql(tableName, SqlStatements.ApplicationsSecurityRemediation));
	        model.put("applicationsLicenseRemediation", dbService.runSql(tableName, SqlStatements.ApplicationsLicenseRemediation));	
	        
			model.put("mostCriticalOrganisationsData", oov);
			model.put("mostCriticalApplicationsData", aov);
			
			model.put("mostScannedApplicationsData", dbService.runSql(tableName, SqlStatements.MostScannedApplications));
        }
        
		List<DbRow> riskRatio = dbService.runSql(tableName, SqlStatements.RiskRatio);
		model.put("riskRatioChart", riskRatio);
		model.put("riskRatioAtEndPeriod", riskRatio.get(riskRatio.size() - 1).getPointA());

		return model;
	}
	
	public boolean applicationExists(String applicationName) {
		List<DbRow> applications = dbService.runSql(SqlStatements.METRICTABLENAME, SqlStatements.ListOfApplications);
		boolean status = false;
		
		for (DbRow r: applications) {
			if (r.getLabel().equalsIgnoreCase(applicationName)) {
				status = true;
				break;
			}
		}
		
		return status;
	}
}
