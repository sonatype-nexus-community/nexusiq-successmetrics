package org.sonatype.cs.metrics.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MetricsService {

    private SecurityDataService securityDataService;
    private LicenseDataService licenseDataService;
    private ApplicationsDataService applicationsDataService;
    private TotalsDataService totalsDataService;

    public MetricsService(
            SecurityDataService securityDataService,
            LicenseDataService licenseDataService,
            ApplicationsDataService applicationsDataService,
            TotalsDataService totalsDataService) {
        this.securityDataService = securityDataService;
        this.licenseDataService = licenseDataService;
        this.applicationsDataService = applicationsDataService;
        this.totalsDataService = totalsDataService;
    }

    public Map<String, Object> getMetrics(String tableName, Map<String, Object> periodsData) {

        Map<String, Object> model = new HashMap<>();

        Map<String, Object> applicationData =
                applicationsDataService.getApplicationData(tableName, periodsData);
        Map<String, Object> securityViolationsData =
                securityDataService.getSecurityViolations(tableName);
        Map<String, Object> licenseViolationsData =
                licenseDataService.getLicenseViolations(tableName);
        Map<String, Object> summaryTotals = totalsDataService.getSummaryData(tableName);

        model.putAll(applicationData);
        model.putAll(securityViolationsData);
        model.putAll(licenseViolationsData);
        model.putAll(summaryTotals);

        return model;
    }
}
