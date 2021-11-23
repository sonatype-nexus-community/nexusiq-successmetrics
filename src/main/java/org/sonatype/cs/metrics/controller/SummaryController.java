package org.sonatype.cs.metrics.controller;

import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.SuccessMetricsApplication;
import org.sonatype.cs.metrics.service.MetricsService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SummaryController {
	private static final Logger log = LoggerFactory.getLogger(SummaryController.class);
    
	
	@Autowired
	private PeriodsDataService periodsDataService;
	
    @Autowired
    private MetricsService metricsService;

    @GetMapping({ "/summary" })
    public String applications(Model model) throws ParseException {

        log.info("In ReportSummaryController");
        
        if (SuccessMetricsApplication.successMetricsFileLoaded) {
			Map<String, Object> periodsData = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
	        Map<String, Object> metrics = metricsService.getMetrics(SqlStatements.METRICTABLENAME, periodsData);
	        
	        model.mergeAttributes(periodsData);
	        model.mergeAttributes(metrics);
	        model.addAttribute("globalsummary", true);
	        model.addAttribute("smloaded", SuccessMetricsApplication.successMetricsFileLoaded);
        }

		return "summary";
    }
}
