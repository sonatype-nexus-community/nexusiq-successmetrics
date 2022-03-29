package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.SuccessMetricsApplication;
import org.sonatype.cs.metrics.service.InsightsAnalysisService;
import org.sonatype.cs.metrics.service.PeriodsDataService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class InsightsAnalysisController {
    private static final Logger log = LoggerFactory.getLogger(InsightsAnalysisController.class);

    private InsightsAnalysisService analysisService;
    private PeriodsDataService periodsDataService;
    private SuccessMetricsApplication successMetricsApplication;

    public InsightsAnalysisController(
            InsightsAnalysisService analysisService,
            PeriodsDataService periodsDataService,
            SuccessMetricsApplication successMetricsApplication) {
        this.analysisService = analysisService;
        this.periodsDataService = periodsDataService;
        this.successMetricsApplication = successMetricsApplication;
    }

    @GetMapping({"/analysis", "/analysis.html"})
    public String analysis(Model model) {
        log.info("In InsightsAnalysisController");

        boolean doAnalysis = false;

        if (successMetricsApplication.isSuccessMetricsFileLoaded()) {

            Map<String, Object> periodsData =
                    periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
            doAnalysis = (boolean) periodsData.get("doAnalysis");

            model.mergeAttributes(periodsData);

            if (doAnalysis) {
                Map<String, Object> analysisData =
                        analysisService.getInsightsAnalysisData(periodsData);
                model.mergeAttributes(analysisData);
            }
        }

        return "analysis";
    }
}
