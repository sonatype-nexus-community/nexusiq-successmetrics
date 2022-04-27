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
public class ComparePeriodsController {
    private static final Logger log = LoggerFactory.getLogger(ComparePeriodsController.class);

    private PeriodsDataService periodsDataService;
    private MetricsService metricsService;
    private SuccessMetricsApplication successMetricsApplication;

    public ComparePeriodsController(
            PeriodsDataService periodsDataService,
            MetricsService metricsService,
            SuccessMetricsApplication successMetricsApplication) {
        this.periodsDataService = periodsDataService;
        this.metricsService = metricsService;
        this.successMetricsApplication = successMetricsApplication;
    }

    @GetMapping({"/compare", "/compare.html"})
    public String applications(Model model) {
        log.info("In ComparePeriodsController");

        boolean doAnalysis = false;

        if (successMetricsApplication.isSuccessMetricsFileLoaded()) {

            Map<String, Object> periodsData =
                    periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
            doAnalysis = (boolean) periodsData.get("doAnalysis");

            model.mergeAttributes(periodsData);

            if (doAnalysis) {
                Map<String, Object> p1metrics =
                        metricsService.getMetrics(SqlStatements.METRICP1TABLENAME, periodsData);
                Map<String, Object> p2metrics =
                        metricsService.getMetrics(SqlStatements.METRICP2TABLENAME, periodsData);
                model.addAttribute("p1", p1metrics);
                model.addAttribute("p2", p2metrics);
            }
        }

        return "comparePeriods";
    }
}
