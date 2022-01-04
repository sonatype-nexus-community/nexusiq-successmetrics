package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.SuccessMetricsApplication;
import org.sonatype.cs.metrics.service.LoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private LoaderService loaderService;
	
	@Value("${sm.database}")
	private String smdatabase;
	
	@GetMapping({"/", "/home"})
	public String home(Model model) {
        
		log.info("In HomeController");
		
		model.addAttribute("smdatabase", smdatabase);
		model.addAttribute("successmetricsreport", SuccessMetricsApplication.successMetricsFileLoaded);
		model.addAttribute("policyViolationsreport", loaderService.policyViolationsDataLoaded);
		model.addAttribute("applicationEvaluationsreport", loaderService.applicationEvaluationsFileLoaded);
		model.addAttribute("firewallreport", loaderService.quarantinedComponentsLoaded);
		model.addAttribute("componentWaiversReport", loaderService.componentWaiversLoaded);

		return "home";
	}
}

