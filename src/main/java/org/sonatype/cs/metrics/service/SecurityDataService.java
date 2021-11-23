package org.sonatype.cs.metrics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityDataService {
    private static final Logger log = LoggerFactory.getLogger(SecurityDataService.class);

	@Autowired
	private DbService dbService;
	
	public Map<String, Object> getSecurityViolations(String tableName) {
		Map<String, Object> model = new HashMap<>();
		
		List<DbRow> securityViolations = dbService.runSql(tableName, SqlStatements.SecurityViolations);
		List<DbRow> discoveredSecurityViolations = dbService.runSql(tableName, SqlStatements.DiscoveredSecurityViolations);
		List<DbRow> openSecurityViolations = dbService.runSql(tableName, SqlStatements.OpenSecurityViolations);
		List<DbRow> fixedSecurityViolations = dbService.runSql(tableName, SqlStatements.FixedSecurityViolations);
		List<DbRow> waivedSecurityViolations = dbService.runSql(tableName, SqlStatements.WaivedSecurityViolations);

		model.put("securityViolationsChart", securityViolations);
		model.put("discoveredSecurityViolationsChart", discoveredSecurityViolations);
		model.put("openSecurityViolationsChart", openSecurityViolations);
		model.put("fixedSecurityViolationsChart", fixedSecurityViolations);
		model.put("waivedSecurityViolationsChart", waivedSecurityViolations);
		
		DbRow discoveredSecurityViolationsTotal = dbService.runSql(tableName, SqlStatements.DiscoveredSecurityViolationsTotal).get(0);
		DbRow openSecurityViolationsTotal = dbService.runSql(tableName, SqlStatements.OpenSecurityViolationsTotal).get(0);
		DbRow fixedSecurityViolationsTotal = dbService.runSql(tableName, SqlStatements.FixedSecurityViolationsTotal).get(0);
		DbRow waivedSecurityViolationsTotal = dbService.runSql(tableName, SqlStatements.WaivedSecurityViolationsTotal).get(0);
		
		model.put("discoveredSecurityViolationsTotal", discoveredSecurityViolationsTotal);
		model.put("openSecurityViolationsTotal", openSecurityViolationsTotal);
		model.put("fixedSecurityViolationsTotal", fixedSecurityViolationsTotal);
		model.put("waivedSecurityViolationsTotal", waivedSecurityViolationsTotal);
		
		int discoveredSecurityTotal = discoveredSecurityViolationsTotal.getPointA() + discoveredSecurityViolationsTotal.getPointB() + discoveredSecurityViolationsTotal.getPointC();
		int fixedSecurityTotal = fixedSecurityViolationsTotal.getPointA() + fixedSecurityViolationsTotal.getPointB() + fixedSecurityViolationsTotal.getPointC();
		int waivedSecurityTotal = waivedSecurityViolationsTotal.getPointA() + waivedSecurityViolationsTotal.getPointB() + waivedSecurityViolationsTotal.getPointC();
		
		model.put("discoveredSecurityTotal", discoveredSecurityTotal);
		model.put("fixedSecurityTotal", fixedSecurityTotal);
		model.put("waivedSecurityTotal", waivedSecurityTotal);
		
		int discoveredSecurityCriticalTotal = discoveredSecurityViolationsTotal.getPointA();
		int fixedSecurityCriticalTotal = fixedSecurityViolationsTotal.getPointA();
		int waivededSecurityCriticalTotal = waivedSecurityViolationsTotal.getPointA();
		
		model.put("discoveredSecurityCriticalTotal", discoveredSecurityCriticalTotal);
		model.put("fixedSecurityCriticalTotal", fixedSecurityCriticalTotal);
		model.put("waivededSecurityCriticalTotal", waivededSecurityCriticalTotal);

		return model;
	}
	
}
