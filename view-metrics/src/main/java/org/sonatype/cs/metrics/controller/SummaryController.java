package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.SuccessMetricsApplication;
import org.sonatype.cs.metrics.service.MetricsService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class SummaryController {
    private static final Logger log = LoggerFactory.getLogger(SummaryController.class);

    private PeriodsDataService periodsDataService;
    private MetricsService metricsService;
    private SuccessMetricsApplication successMetricsApplication;

    public SummaryController(
            PeriodsDataService periodsDataService,
            MetricsService metricsService,
            SuccessMetricsApplication successMetricsApplication) {
        this.periodsDataService = periodsDataService;
        this.metricsService = metricsService;
        this.successMetricsApplication = successMetricsApplication;
    }

    @GetMapping({"/summary", "/summary.html"})
    public String applications(Model model) {

        log.info("In ReportSummaryController");

        if (successMetricsApplication.isSuccessMetricsFileLoaded()) {
            Map<String, Object> periodsData =
                    periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
            Map<String, Object> metrics =
                    metricsService.getMetrics(SqlStatements.METRICTABLENAME, periodsData);

            model.mergeAttributes(periodsData);
            model.mergeAttributes(metrics);
            model.addAttribute("globalsummary", true);
            model.addAttribute("smloaded", successMetricsApplication.isSuccessMetricsFileLoaded());
        }

        return "summary";
    }
}
