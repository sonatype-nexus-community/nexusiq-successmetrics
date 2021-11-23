package org.sonatype.cs.metrics.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
	
	@Autowired
	private SecurityDataService securityDataService;
	     
	@Autowired
	private LicenseDataService licenseDataService;

	@Autowired
	private ApplicationsDataService applicationsDataService;
	     
	@Autowired
	private TotalsDataService totalsDataService;
	
	
	public Map<String, Object> getMetrics(String tableName, Map<String, Object> periodsData) throws ParseException{
		
		Map<String, Object> model = new HashMap<>();

	    Map<String, Object> applicationData = applicationsDataService.getApplicationData(tableName, periodsData);
	    Map<String, Object> securityViolationsData = securityDataService.getSecurityViolations(tableName);
	    Map<String, Object> licenseViolationsData = licenseDataService.getLicenseViolations(tableName);
	    Map<String, Object> summaryTotals = totalsDataService.getSummaryData(tableName);
	
		model.putAll(applicationData);
		model.putAll(securityViolationsData);
		model.putAll(licenseViolationsData);
		model.putAll(summaryTotals);

		return model;
	}
	
}
