package org.sonatype.cs.metrics.controller;

import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.SuccessMetricsApplication;
import org.sonatype.cs.metrics.service.InsightsAnalysisService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InsightsAnalysisController {
  private static final Logger log = LoggerFactory.getLogger(InsightsAnalysisController.class);

  @Autowired
  private InsightsAnalysisService analysisService;
  
  @Autowired
  private PeriodsDataService periodsDataService;

	     

  @GetMapping({ "/analysis" })
  public String analysis(Model model) throws ParseException {
	  log.info("In InsightsAnalysisController");

	  boolean doAnalysis = false;

  	  if (SuccessMetricsApplication.successMetricsFileLoaded) {

	  	  Map<String, Object> periodsData = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
		  doAnalysis  = (boolean) periodsData.get("doAnalysis");
	
	      model.mergeAttributes(periodsData);
	      
	      if (doAnalysis) {
		      Map<String, Object> analysisData = analysisService.getInsightsAnalysisData(periodsData);
		      model.mergeAttributes(analysisData);
	      }
  	  }
  		
      return "analysis";
  }
}


