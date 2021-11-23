package org.sonatype.cs.metrics.controller;

import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.ApplicationsDataService;
import org.sonatype.cs.metrics.service.DbService;
import org.sonatype.cs.metrics.service.MetricsService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ApplicationsSummaryController {
	private static final Logger log = LoggerFactory.getLogger(SummaryController.class);
    
	@Autowired
	private PeriodsDataService periodsDataService;
	
    @Autowired
    private MetricsService metricsService;
    
    @Autowired
	private DbService dbService;
    
    @Autowired
	private ApplicationsDataService applicationsDataService;

    @GetMapping({ "/appsummary" })
    public String application(Model model, @RequestParam(value = "appname", defaultValue = "none") String applicationName) throws ParseException {
    	boolean showData = false;

        log.info("In ApplicationSummaryController with " + applicationName);
        
        model.addAttribute("applicationname", applicationName.toUpperCase());

        if (applicationsDataService.applicationExists(applicationName)) {
        	String applicationTable = "METRIC_" + applicationName.replace("-", "_").toUpperCase();

            String sqlStmt = "DROP TABLE IF EXISTS " + applicationTable + ";" + " CREATE TABLE " + applicationTable + " AS SELECT * FROM METRIC WHERE APPLICATION_NAME = '" + applicationName + "'";

    		dbService.runSqlLoad(sqlStmt);
            
    		Map<String, Object> periodsData = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
            Map<String, Object> metrics = metricsService.getMetrics(applicationTable, periodsData);
            
            model.mergeAttributes(periodsData);
            model.mergeAttributes(metrics);
            model.addAttribute("globalsummary", false);
            model.addAttribute("showData", true);
        }
        
		return "applicationSummary";
    }
}
