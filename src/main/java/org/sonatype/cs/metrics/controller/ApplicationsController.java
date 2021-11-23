package org.sonatype.cs.metrics.controller;

import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.ApplicationsDataService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationsController {
    private static final Logger log = LoggerFactory.getLogger(ApplicationsController.class);
    
    @Autowired
	private PeriodsDataService periodsDataService;
    
    @Autowired
    private ApplicationsDataService applicationsDataService;
    
    
    @GetMapping({ "/applications" })
    public String applications(Model model) throws ParseException {

        log.info("In ApplicationsController");
    	
		Map<String, Object> periodsData = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
        Map<String, Object> applicationData = applicationsDataService.getApplicationData(SqlStatements.METRICTABLENAME, periodsData);
		model.mergeAttributes(applicationData);
        model.addAttribute("globalsummary", true);

        return "applications";
    }
}
