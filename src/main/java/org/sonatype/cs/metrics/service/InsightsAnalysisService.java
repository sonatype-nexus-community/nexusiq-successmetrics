package org.sonatype.cs.metrics.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
public class InsightsAnalysisService {
	private static final Logger log = LoggerFactory.getLogger(InsightsAnalysisService.class);
	
	@Autowired
	private MetricsService metricsService;

	@Autowired
	private PeriodsDataService periodsDataService;

	@Autowired
	private FileIoService fileIoService;
	
	

	public void writeInsightsAnalysisData() throws ParseException, IOException{
		log.info("Writing insights data file");

		boolean doAnalysis = false;

	  	Map<String, Object> periodsData = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
		doAnalysis  = (boolean) periodsData.get("doAnalysis");
	
	    if (doAnalysis) {
			Map<String, Object> analysisData = getInsightsAnalysisData(periodsData);
			List<String[]> csvData = createCsvData(analysisData);
			
			String csvFilename = fileIoService.makeFilename("insights", "csv");

			String beforeDateRange = "Before " + (String) periodsData.get("midPeriodDate1RangeStr");
			String afterDateRange = "After " + (String) periodsData.get("midPeriodDate2RangeStr");

			fileIoService.writeInsightsCsvFile(csvFilename, csvData, beforeDateRange, afterDateRange);
			
		    log.info("Created insights data file: " + csvFilename);
	    }
  	  
		return;
	}

	public Map<String, Object> getInsightsAnalysisData(Map<String, Object> periodsData) throws ParseException{
		Map<String, Object> model = new HashMap<>();
	    
	    Map<String, Object> p1metrics = metricsService.getMetrics(SqlStatements.METRICP1TABLENAME, periodsData);
	    Map<String, Object> p2metrics = metricsService.getMetrics(SqlStatements.METRICP2TABLENAME, periodsData);
	    
	    int appsInFirstPeriod = (int) p1metrics.get("startPeriodCount");
	    int p1numberOfPeriods = (int) p1metrics.get("numberOfPeriods");
	    int p2numberOfPeriods = (int) p2metrics.get("numberOfPeriods");

	    int onboardedAfter = (int) p2metrics.get("applicationsOnboarded");
	    int onboardedBefore = (int) p1metrics.get("applicationsOnboarded");
	    
	    float scanningCoverageAfter = this.calculatePct((int)p2metrics.get("numberOfApplicationsScannedAvg"), onboardedAfter);
	    float scanningCoverageBefore = this.calculatePct((int)p1metrics.get("numberOfApplicationsScannedAvg"), onboardedBefore);
	    
	    float totalScansAfter = (int) p2metrics.get("numberOfScans");
	    float totalScansBefore = (int) p1metrics.get("numberOfScans");
	    
	    float scanningRateAfter = (int) p2metrics.get("numberOfScans")/p2numberOfPeriods;
	    float scanningRateBefore = (int) p1metrics.get("numberOfScans")/p1numberOfPeriods;
	    
	    float scanningRateAfterAvg = scanningRateAfter/onboardedAfter;
	    float scanningRateBeforeAvg = scanningRateBefore/onboardedBefore;

	    DbRow discoveredSecurityTotalAfter = (DbRow) p2metrics.get("discoveredSecurityViolationsTotal");
	    DbRow discoveredSecurityTotalBefore = (DbRow) p1metrics.get("discoveredSecurityViolationsTotal");
	    
	    float discoveryRateCriticalsBefore = (float) (discoveredSecurityTotalBefore.getPointA()/p1numberOfPeriods)/onboardedBefore;
	    float discoveryRateCriticalsAfter = (float) (discoveredSecurityTotalAfter.getPointA()/p2numberOfPeriods)/onboardedAfter;
	    
	    DbRow fixedSecurityCriticalsTotalAfter = (DbRow) p2metrics.get("fixedSecurityViolationsTotal");
	    DbRow fixedSecurityCriticalsTotalBefore = (DbRow) p1metrics.get("fixedSecurityViolationsTotal");

	    float fixedRateCriticalsBefore = (float) (fixedSecurityCriticalsTotalBefore.getPointA()/p1numberOfPeriods)/onboardedBefore;
	    float fixedRateCriticalsAfter = (float) (fixedSecurityCriticalsTotalAfter.getPointA()/p2numberOfPeriods)/onboardedAfter;
	    
	    float backlogReductionCriticalsRateBefore = (float) p1metrics.get("backlogReductionRateCritical");
	    float backlogReductionCriticalsRateAfter = (float) p2metrics.get("backlogReductionRateCritical");

	    
	    model.put("totalOnboardedBefore", onboardedBefore);
	    model.put("totalOnboardedAfter", onboardedAfter);
	    model.put("totalOnboardedDiff", this.formatFloat(onboardedAfter - onboardedBefore));
	    model.put("totalOnboarded", this.calculateChangePctg(onboardedBefore, onboardedAfter));
	    model.put("totalOnboardedIncrease", this.calculateChangeMultiple(onboardedBefore, onboardedAfter));

	    float onboardingRateBefore = (float) (onboardedBefore - appsInFirstPeriod)/p1numberOfPeriods;
	    float onboardingRateAfter = (float) (onboardedAfter - onboardedBefore)/p2numberOfPeriods;
	    
	    model.put("onboardingRateBefore", this.formatFloat(onboardingRateBefore));
	    model.put("onboardingRateAfter", this.formatFloat(onboardingRateAfter));
	    model.put("onboardingRateDiff", this.formatFloat(onboardingRateAfter - onboardingRateBefore));
	    model.put("onboardingRate", this.calculateChangePctg(onboardingRateAfter, onboardingRateAfter));
	    model.put("onboardingRateIncrease", this.calculateChangeMultiple(onboardingRateBefore, onboardingRateAfter));

	    model.put("scanningCoverageBefore", this.formatFloat(scanningCoverageBefore));
	    model.put("scanningCoverageAfter", this.formatFloat(scanningCoverageAfter));
	    model.put("scanningCoverageDiff", this.formatFloat(scanningCoverageAfter - scanningCoverageBefore));
	    model.put("scanningCoverage", this.calculateChangePctg(scanningCoverageBefore, scanningCoverageAfter));
	    model.put("scanningCoverageIncrease", this.calculateChangeMultiple(scanningCoverageBefore, scanningCoverageAfter));

	    model.put("scanningRateBefore", this.formatFloat(scanningRateBefore));
	    model.put("scanningRateAfter", this.formatFloat(scanningRateAfter));
	    model.put("scanningRateDiff", this.formatFloat(scanningRateAfter - scanningRateBefore));
	    model.put("scanningRate", this.calculateChangePctg(scanningRateBefore, scanningRateAfter));
	    model.put("scanningRateIncrease", this.calculateChangeMultiple(scanningRateBefore, scanningRateAfter));

	    model.put("avgScansBefore", this.formatFloat(scanningRateBeforeAvg));
	    model.put("avgScansAfter", this.formatFloat(scanningRateAfterAvg));
	    model.put("avgScansDiff", this.formatFloat(scanningRateAfterAvg - scanningRateBeforeAvg));
	    model.put("avgScans", this.calculateChangePctg(scanningRateBeforeAvg, scanningRateAfterAvg));
	    model.put("avgScansIncrease", this.calculateChangeMultiple(scanningRateBeforeAvg, scanningRateAfterAvg));

	    model.put("discoveryRateCriticalsBefore", this.formatFloat(discoveryRateCriticalsBefore));
	    model.put("discoveryRateCriticalsAfter", this.formatFloat(discoveryRateCriticalsAfter));
	    model.put("discoveryRateCriticalsDiff", this.formatFloat(discoveryRateCriticalsAfter - discoveryRateCriticalsBefore));
	    model.put("discoveryRateCriticals", this.calculateChangePctg(discoveryRateCriticalsBefore, discoveryRateCriticalsAfter));
	    model.put("discoveryRateCriticalsIncrease", this.calculateChangeMultiple(discoveryRateCriticalsBefore, discoveryRateCriticalsAfter));

	    model.put("fixingRateCriticalsBefore", this.formatFloat(fixedRateCriticalsBefore));
	    model.put("fixingRateCriticalsAfter", this.formatFloat(fixedRateCriticalsAfter));
	    model.put("fixingRateCriticalsDiff", this.formatFloat(fixedRateCriticalsAfter - fixedRateCriticalsBefore));
	    model.put("fixingRateCriticals", this.calculateChangePctg(fixedRateCriticalsBefore, fixedRateCriticalsAfter));
	    model.put("fixingRateCriticalsIncrease", this.calculateChangeMultiple(fixedRateCriticalsBefore, fixedRateCriticalsAfter));

	    model.put("backlogReductionCriticalsRateBefore", this.formatFloat(backlogReductionCriticalsRateBefore));
	    model.put("backlogReductionCriticalsRateAfter", this.formatFloat(backlogReductionCriticalsRateAfter));
	    model.put("backlogReductionCriticalsRateDiff", this.formatFloat(backlogReductionCriticalsRateAfter - backlogReductionCriticalsRateBefore));
	    model.put("backlogReductionCriticalsRate", this.calculateChangePctg(backlogReductionCriticalsRateBefore, backlogReductionCriticalsRateAfter));
	    model.put("backlogReductionCriticalsRateIncrease", this.calculateChangeMultiple(backlogReductionCriticalsRateBefore, backlogReductionCriticalsRateAfter));

//	    int discoveredCriticalBefore = (int) p1metrics.get("discoveredCritical");
//	    int discoveredCriticalAfter = (int) p2metrics.get("discoveredCritical");
//	    float riskRatioBefore = discoveredCriticalBefore/onboardedBefore;
//	    float riskRatioAfter = discoveredCriticalAfter/onboardedAfter;
	    
	    float riskRatioBefore = (int) p1metrics.get("riskRatioAtEndPeriod");
	    float riskRatioAfter = (int) p2metrics.get("riskRatioAtEndPeriod");
	    
	    model.put("riskRatioBefore", riskRatioBefore);
	    model.put("riskRatioAfter", riskRatioAfter);
	    model.put("riskRatioDiff", this.formatFloat(riskRatioAfter - riskRatioBefore));
	    model.put("riskRatio", this.calculateChangePctg(riskRatioBefore, riskRatioAfter));
	    model.put("riskRatioIncrease", this.calculateChangeMultiple(riskRatioBefore, riskRatioAfter));

	    String[] mttrCriticalsBefore = (String[]) p1metrics.get("mttrAvg");
	    String[] mttrCriticalsAfter = (String[]) p2metrics.get("mttrAvg");
	    
	    model.put("mttrCriticalsBefore", Integer.parseInt(mttrCriticalsBefore[0]));
	    model.put("mttrCriticalsAfter", Integer.parseInt(mttrCriticalsAfter[0]));
	    model.put("mttrCriticalsDiff", this.formatFloat(Integer.parseInt(mttrCriticalsAfter[0]) - Integer.parseInt(mttrCriticalsBefore[0])));
	    model.put("mttrCriticals", this.calculateChangePctg(Integer.parseInt(mttrCriticalsBefore[0]), Integer.parseInt(mttrCriticalsAfter[0])));
	    model.put("mttrCriticalsIncrease", this.calculateChangeMultiple(Integer.parseInt(mttrCriticalsBefore[0]), Integer.parseInt(mttrCriticalsAfter[0])));
	    
	    model.put("totalScansBefore", totalScansBefore);
	    model.put("totalScansAfter", totalScansAfter);
	    model.put("totalScansDiff", this.formatFloat(totalScansAfter - totalScansBefore));
	    model.put("totalScans", this.calculateChangePctg(totalScansBefore, totalScansAfter));
	    model.put("totalScansIncrease", this.calculateChangeMultiple(totalScansBefore, totalScansAfter));
	    
	    return model;
	  }

	private String formatFloat(float f) {
		return String.format("%.2f", f);
	}
  
	private String division (float a, float b) {
	  float result = 0;

	  if (a > 0 && b > 0){
		  result = a / b;
	  }
	    
	  return String.format("%.2f", result);
	}
	
	private String calculateChangePctg(float before, float after){
	    float result = 0;

	    if (after > 0 && before > 0){
	    	result = (((after - before) / before) * 100);
	    }

	    return String.format("%.2f", result);
	}

	private String calculateChangeMultiple(float before, float after){
	    float result = 0;
	
	    if (after > 0 && before > 0){
	    	result = after / before;
	    }
	    
	    return String.format("%.2f", result);
	}
  
	private float calculatePct(float after, float before){
		float result = 0;
	
	    if (after > 0 && before > 0){
	    	result = ((after / before) * 100);
	    }
	    
	    return result;
	}

	private List<String[]> createCsvData(Map<String, Object> analysisData) {
		
		List<String[]> data = new ArrayList<>();
		
		data.add(new String[] { "Total Onboarded Apps (# of apps)", 
				String.valueOf(analysisData.get("totalOnboardedBefore")),
				String.valueOf(analysisData.get("totalOnboardedAfter")),
				String.valueOf(analysisData.get("totalOnboardedDiff")),
				String.valueOf(analysisData.get("totalOnboarded")),
				String.valueOf(analysisData.get("totalOnboardedIncrease")) });
		

		data.add(new String[] { "Onboarding Rate (apps/period)", 
				String.valueOf(analysisData.get("onboardingRateBefore")),
				String.valueOf(analysisData.get("onboardingRateAfter")),
				String.valueOf(analysisData.get("onboardingRateDiff")),
				String.valueOf(analysisData.get("onboardingRate")),
				String.valueOf(analysisData.get("onboardingRateIncrease")) });
		
		data.add(new String[] { "Total Scans", 
				String.valueOf(analysisData.get("totalScansBefore")),
				String.valueOf(analysisData.get("totalScansAfter")),
				String.valueOf(analysisData.get("totalScansDiff")),
				String.valueOf(analysisData.get("totalScans")),
				String.valueOf(analysisData.get("totalScansIncrease")) });
		
		data.add(new String[] { "Scanning Coverage %(apps scanned at least once/period)", 
				String.valueOf(analysisData.get("scanningCoverageBefore")),
				String.valueOf(analysisData.get("scanningCoverageAfter")),
				String.valueOf(analysisData.get("scanningCoverageDiff")),
				String.valueOf(analysisData.get("scanningCoverage")),
				String.valueOf(analysisData.get("scanningCoverageIncrease")) });
		
		data.add(new String[] { "Scanning Rate (total scans per period)", 
				String.valueOf(analysisData.get("scanningRateBefore")),
				String.valueOf(analysisData.get("scanningRateAfter")),
				String.valueOf(analysisData.get("scanningRateDiff")),
				String.valueOf(analysisData.get("scanningRate")),
				String.valueOf(analysisData.get("scanningRateIncrease")) });
		
		data.add(new String[] { "Average Scans per App (scanning rate/apps)", 
				String.valueOf(analysisData.get("avgScansBefore")),
				String.valueOf(analysisData.get("avgScansAfter")),
				String.valueOf(analysisData.get("avgScansDiff")),
				String.valueOf(analysisData.get("avgScans")),
				String.valueOf(analysisData.get("avgScansIncrease")) });
		
		data.add(new String[] { "Discovery Rate Criticals (# of discovered Critical violations/period & app)", 
				String.valueOf(analysisData.get("discoveryRateCriticalsBefore")),
				String.valueOf(analysisData.get("discoveryRateCriticalsAfter")),
				String.valueOf(analysisData.get("discoveryRateCriticalsDiff")),
				String.valueOf(analysisData.get("discoveryRateCriticals")),
				String.valueOf(analysisData.get("discoveryRateCriticalsIncrease")) });
		
		data.add(new String[] { "Fixing Rate Criticals (# of fixed Critical violations/period & app)", 
				String.valueOf(analysisData.get("fixingRateCriticalsBefore")),
				String.valueOf(analysisData.get("fixingRateCriticalsAfter")),
				String.valueOf(analysisData.get("fixingRateCriticalsDiff")),
				String.valueOf(analysisData.get("fixingRateCriticals")),
				String.valueOf(analysisData.get("fixingRateCriticalsIncrease")) });
		
		data.add(new String[] { "Backlog Reduction Rate Criticals %(# of fixed / # of discovered)", 
				String.valueOf(analysisData.get("backlogReductionCriticalsRateBefore")),
				String.valueOf(analysisData.get("backlogReductionCriticalsRateAfter")),
				String.valueOf(analysisData.get("backlogReductionCriticalsRateDiff")),
				String.valueOf(analysisData.get("backlogReductionCriticalsRate")),
				String.valueOf(analysisData.get("backlogReductionCriticalsRateIncrease")) });
		
		data.add(new String[] { "Risk Ratio (# of Critical violations / # of apps)", 
				String.valueOf(analysisData.get("riskRatioBefore")),
				String.valueOf(analysisData.get("riskRatioAfter")),
				String.valueOf(analysisData.get("riskRatioDiff")),
				String.valueOf(analysisData.get("riskRatio")),
				String.valueOf(analysisData.get("riskRatioIncrease")) });
		
		data.add(new String[] { "MTTR Criticals (average # of days to fix Critical violations)", 
				String.valueOf(analysisData.get("mttrCriticalsBefore")),
				String.valueOf(analysisData.get("mttrCriticalsAfter")),
				String.valueOf(analysisData.get("mttrCriticalsDiff")),
				String.valueOf(analysisData.get("mttrCriticals")),
				String.valueOf(analysisData.get("mttrCriticalsIncrease")) });

		return data;
	}
}
