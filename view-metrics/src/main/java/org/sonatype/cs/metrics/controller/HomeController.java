package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.SuccessMetricsApplication;
import org.sonatype.cs.metrics.service.LoaderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private LoaderService loaderService;
    private SuccessMetricsApplication successMetricsApplication;
    private String smdatabase;

    public HomeController(
            LoaderService loaderService,
            SuccessMetricsApplication successMetricsApplication,
            @Value("${sm.database}") String smdatabase) {
        this.loaderService = loaderService;
        this.successMetricsApplication = successMetricsApplication;
        this.smdatabase = smdatabase;
    }

    @GetMapping({"/", "/home", "/home.html"})
    public String home(Model model) {

        log.info("In HomeController");

        model.addAttribute("smdatabase", smdatabase);
        model.addAttribute(
                "successmetricsreport", successMetricsApplication.isSuccessMetricsFileLoaded());
        model.addAttribute("policyViolationsreport", loaderService.isPolicyViolationsDataLoaded());
        model.addAttribute(
                "applicationEvaluationsreport", loaderService.isApplicationEvaluationsFileLoaded());
        model.addAttribute("firewallreport", loaderService.isQuarantinedComponentsLoaded());
        model.addAttribute("componentWaiversReport", loaderService.isComponentWaiversLoaded());

        return "home";
    }
}
