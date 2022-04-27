package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.ApplicationsDataService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class ApplicationsController {
    private static final Logger log = LoggerFactory.getLogger(ApplicationsController.class);

    private PeriodsDataService periodsDataService;
    private ApplicationsDataService applicationsDataService;

    public ApplicationsController(
            PeriodsDataService periodsDataService,
            ApplicationsDataService applicationsDataService) {
        this.periodsDataService = periodsDataService;
        this.applicationsDataService = applicationsDataService;
    }

    @GetMapping({"/applications", "/applications.html"})
    public String applications(Model model) {

        log.info("In ApplicationsController");

        Map<String, Object> periodsData =
                periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
        Map<String, Object> applicationData =
                applicationsDataService.getApplicationData(
                        SqlStatements.METRICTABLENAME, periodsData);
        model.mergeAttributes(applicationData);
        model.addAttribute("globalsummary", true);

        return "applications";
    }
}
