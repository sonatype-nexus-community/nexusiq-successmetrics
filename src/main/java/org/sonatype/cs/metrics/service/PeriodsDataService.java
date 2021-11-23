package org.sonatype.cs.metrics.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.controller.InsightsAnalysisController;
import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PeriodsDataService {
	private static final Logger log = LoggerFactory.getLogger(PeriodsDataService.class);

	@Autowired
	private DbService dbService;
	
	private static int oneDayMs = 86400000;
    private static long oneWeekMs = 604800000;
    private static long oneMonthMs = 2629800000L;

	
	public Map<String, Object> getPeriodData(String tableName) throws ParseException {
		Map<String, Object> model = new HashMap<>();
		model.put("doAnalysis", false);

	    List<DbRow> timePeriods = dbService.runSql(tableName, SqlStatements.TimePeriods);
		
	    int numberOfPeriods = timePeriods.size();
	    
	    String startPeriod = timePeriods.get(0).getLabel();
	    String firstTimePeriod = startPeriod.toString();
	    
		String endPeriod = null;
		
	    String secondTimePeriod = null;
	    
	    if (numberOfPeriods == 1) {
	    	endPeriod = startPeriod;
	    	secondTimePeriod = startPeriod.toString();
	    }
	    else {
	    	endPeriod = timePeriods.get(timePeriods.size()-1).getLabel();
	    	secondTimePeriod = timePeriods.get(1).getLabel().toString();
	    }
	    
		String timePeriodFrequency = this.getTimePeriodFrequency(numberOfPeriods, firstTimePeriod, secondTimePeriod);
		
		model.put("timePeriodFrequency", timePeriodFrequency);
		model.put("startPeriod", startPeriod);
		model.put("endPeriod", endPeriod);
		model.put("periodDateRangeStr", "(" + startPeriod + " - " + endPeriod + ")");
		
		if (numberOfPeriods > 2) {
			int midPeriodIndex = numberOfPeriods/2;
			
			if (midPeriodIndex % 2 == 0) {
				midPeriodIndex--;
			}
			
			String midPeriod = timePeriods.get(midPeriodIndex).getLabel();
			
			String midPeriodEnd = midPeriod;
			String midPeriodStart = timePeriods.get(midPeriodIndex+1).getLabel();
	
			model.put("midPeriod", midPeriod);
			model.put("midPeriodDate1RangeStr", "(" + startPeriod + " - " + midPeriodEnd + ")");
			model.put("midPeriodDate2RangeStr", "(" + midPeriodStart + " - " + endPeriod + ")");
			model.put("doAnalysis", true);
		}
		
		return model;
	}
	
	private String getTimePeriodFrequency(int tmSize, String firstTimePeriod, String secondTimePeriod) throws ParseException {

		long oneWeek = 604800000;
		
		String timePeriodLabel = "Week";
		
		if (tmSize > 1) {
		
			long fp = this.convertDateStr(firstTimePeriod);
			long sp = this.convertDateStr(secondTimePeriod);
			
			long diff = sp - fp;

			if (diff < oneMonthMs) {
				timePeriodLabel = "week";
			}
			else {
				timePeriodLabel = "month";

			}
		}
		else {
			timePeriodLabel = "week";
		}
		
		return timePeriodLabel;
	}
	
	private Long convertDateStr(String str) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(str);
		long millis = date.getTime();
		return millis;
	}
	
}
