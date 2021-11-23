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
public class ComparePeriodsController {
  private static final Logger log = LoggerFactory.getLogger(ComparePeriodsController.class);

  @Autowired
  private PeriodsDataService periodsDataService;
  
  @Autowired
  private MetricsService metricsService;

  @GetMapping({ "/compare" })
  public String applications(Model model) throws ParseException {
    log.info("In ComparePeriodsController");
   
    boolean doAnalysis = false;
    
    if (SuccessMetricsApplication.successMetricsFileLoaded) {

		Map<String, Object> periodsData = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
		doAnalysis  = (boolean) periodsData.get("doAnalysis");
	
	    model.mergeAttributes(periodsData);
	
		if (doAnalysis) {
			Map<String, Object> p1metrics = metricsService.getMetrics(SqlStatements.METRICP1TABLENAME, periodsData);
			Map<String, Object> p2metrics = metricsService.getMetrics(SqlStatements.METRICP2TABLENAME, periodsData);
			model.addAttribute("p1", p1metrics);
			model.addAttribute("p2", p2metrics);
		}
    }
   
    return "comparePeriods";
  }
}

