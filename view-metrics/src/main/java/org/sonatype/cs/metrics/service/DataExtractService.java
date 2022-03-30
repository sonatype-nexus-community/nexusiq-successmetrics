package org.sonatype.cs.metrics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DataExtractObject;
import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.Mttr;
import org.sonatype.cs.metrics.model.RiskRatio;
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataExtractService {
    private static final Logger log = LoggerFactory.getLogger(DataExtractService.class);

    private SecurityDataService securityDataService;
    private LicenseDataService licenseDataService;
    private PeriodsDataService periodsDataService;
    private ApplicationsDataService applicationsDataService;
    private FileIoService fileIoService;

    public DataExtractService(
            SecurityDataService securityDataService,
            LicenseDataService licenseDataService,
            PeriodsDataService periodsDataService,
            ApplicationsDataService applicationsDataService,
            FileIoService fileIoService) {
        this.securityDataService = securityDataService;
        this.licenseDataService = licenseDataService;
        this.periodsDataService = periodsDataService;
        this.applicationsDataService = applicationsDataService;
        this.fileIoService = fileIoService;
    }

    public void writeDataExtract(String timestamp) throws IOException {
        log.info("Writing data extracts file");

        Map<String, Object> securityData =
                securityDataService.getSecurityViolations(SqlStatements.METRICTABLENAME);
        Map<String, Object> licenseData =
                licenseDataService.getLicenseViolations(SqlStatements.METRICTABLENAME);
        Map<String, Object> periodsData =
                periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
        Map<String, Object> applicationData =
                applicationsDataService.getApplicationData(
                        SqlStatements.METRICTABLENAME, periodsData);

        List<DbRow> timePeriods = (List<DbRow>) periodsData.get("timePeriodsList");
        List<DbRow> uniqueAppScanList =
                (List<DbRow>) applicationData.get("numberOfApplicationsScannedChart");
        List<DbRow> applicationsOnboardList =
                (List<DbRow>) applicationData.get("applicationsOnboardedChart");
        List<DbRow> totalNumberOfScansList =
                (List<DbRow>) applicationData.get("numberOfScansChart");
        List<RiskRatio> riskRatioList = (List<RiskRatio>) applicationData.get("riskRatioChart");
        List<Mttr> mttrList = (List<Mttr>) applicationData.get("mttrChart");
        List<DbRow> securityViolationTotals =
                (List<DbRow>) securityData.get("securityViolationsChart");
        List<DbRow> discoveredSecurityList =
                (List<DbRow>) securityData.get("discoveredSecurityViolationsChart");
        List<DbRow> openSecurityList =
                (List<DbRow>) securityData.get("openSecurityViolationsChart");
        List<DbRow> fixedSecurityList =
                (List<DbRow>) securityData.get("fixedSecurityViolationsChart");
        List<DbRow> waivedSecurityList =
                (List<DbRow>) securityData.get("waivedSecurityViolationsChart");
        List<DbRow> licenseViolationTotals = (List<DbRow>) licenseData.get("LicenseViolations");
        List<DbRow> discoveredLicenseList =
                (List<DbRow>) licenseData.get("discoveredLicenseViolations");
        List<DbRow> openLicenseList = (List<DbRow>) licenseData.get("openLicenseViolations");
        List<DbRow> fixedLicenseList = (List<DbRow>) licenseData.get("fixedLicenseViolations");
        List<DbRow> waivedLicenseList = (List<DbRow>) licenseData.get("waivedLicenseViolations");

        List<DataExtractObject> deoList = new ArrayList<>();

        for (DbRow i : timePeriods) {

            DataExtractObject deo = new DataExtractObject();

            String timePeriod = i.getLabel();
            LocalDate date = LocalDate.parse(timePeriod);
            deo.setDate(date);

            uniqueAppScanList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(o -> deo.setTotalUniqueApplicationsScaned(o.getPointA()));

            applicationsOnboardList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(o -> deo.setTotalApplicationsOnboard(o.getPointA()));

            totalNumberOfScansList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(o -> deo.setTotalScans(o.getPointA()));

            riskRatioList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(o -> deo.setRiskRatio(o.getRiskRatioValue()));

            mttrList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setMttrCritical(Math.round(o.getPointA()));
                                deo.setMttrSevere(Math.round(o.getPointB()));
                                deo.setMttrModerate(Math.round(o.getPointC()));
                            });

            securityViolationTotals.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setSecurityDiscovered(o.getPointA());
                                deo.setSecurityOpen(o.getPointB());
                                deo.setSecurityFixed(o.getPointC());
                                deo.setSecurityWaived(o.getPointD());
                            });

            discoveredSecurityList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setSecurityCriticalDiscovered(o.getPointA());
                                deo.setSecuritySevereDiscovered(o.getPointB());
                                deo.setSecurityModerateDiscovered(o.getPointC());
                            });

            openSecurityList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setSecurityCriticalOpen(o.getPointA());
                                deo.setSecuritySevereOpen(o.getPointB());
                                deo.setSecurityModerateOpen(o.getPointC());
                            });

            fixedSecurityList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setSecurityCriticalFixed(o.getPointA());
                                deo.setSecuritySevereFixed(o.getPointB());
                                deo.setSecurityModerateFixed(o.getPointC());
                            });

            waivedSecurityList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setSecurityCriticalWaived(o.getPointA());
                                deo.setSecuritySevereWaived(o.getPointB());
                                deo.setSecurityModerateWaived(o.getPointC());
                            });

            licenseViolationTotals.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setLicenseDiscovered(o.getPointA());
                                deo.setLicenseOpen(o.getPointB());
                                deo.setLicenseFixed(o.getPointC());
                                deo.setLicenseWaived(o.getPointD());
                            });

            discoveredLicenseList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setLicenseCriticalDiscovered(o.getPointA());
                                deo.setLicenseSevereDiscovered(o.getPointB());
                                deo.setLicenseModerateDiscovered(o.getPointC());
                            });

            openLicenseList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setLicenseCriticalOpen(o.getPointA());
                                deo.setLicenseSevereOpen(o.getPointB());
                                deo.setLicenseModerateOpen(o.getPointC());
                            });

            fixedLicenseList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setLicenseCriticalFixed(o.getPointA());
                                deo.setLicenseSevereFixed(o.getPointB());
                                deo.setLicenseModerateFixed(o.getPointC());
                            });

            waivedLicenseList.stream()
                    .filter(o -> o.getLabel().equals(timePeriod))
                    .forEach(
                            o -> {
                                deo.setLicenseCriticalWaived(o.getPointA());
                                deo.setLicenseSevereWaived(o.getPointB());
                                deo.setLicenseModerateWaived(o.getPointC());
                            });

            deo.setSecurityRiskRatio(
                    calculateRiskRatio(deo.getTotalApplicationsOnboard(), deo.getSecurityOpen()));
            deo.setSecurityCriticalRiskRatio(
                    calculateRiskRatio(
                            deo.getTotalApplicationsOnboard(), deo.getSecurityCriticalOpen()));
            deo.setSecuritySevereRiskRatio(
                    calculateRiskRatio(
                            deo.getTotalApplicationsOnboard(), deo.getSecuritySevereOpen()));
            deo.setSecurityModerateRiskRatio(
                    calculateRiskRatio(
                            deo.getTotalApplicationsOnboard(), deo.getSecurityModerateOpen()));
            deo.setLicenseRiskRatio(
                    calculateRiskRatio(deo.getTotalApplicationsOnboard(), deo.getLicenseOpen()));
            deo.setLicenseCriticalRiskRatio(
                    calculateRiskRatio(
                            deo.getTotalApplicationsOnboard(), deo.getLicenseCriticalOpen()));
            deo.setLicenseSevereRiskRatio(
                    calculateRiskRatio(
                            deo.getTotalApplicationsOnboard(), deo.getLicenseSevereOpen()));
            deo.setLicenseModerateRiskRatio(
                    calculateRiskRatio(
                            deo.getTotalApplicationsOnboard(), deo.getLicenseModerateOpen()));

            deo.setSecurityBacklogRate(
                    calculateBacklogRate(deo.getSecurityDiscovered(), deo.getSecurityFixed()));
            deo.setSecurityCriticalBacklogRate(
                    calculateBacklogRate(
                            deo.getSecurityCriticalDiscovered(), deo.getSecurityCriticalFixed()));
            deo.setSecuritySevereBacklogRate(
                    calculateBacklogRate(
                            deo.getSecuritySevereDiscovered(), deo.getSecuritySevereFixed()));
            deo.setSecurityModerateBacklogRate(
                    calculateBacklogRate(
                            deo.getSecurityModerateDiscovered(), deo.getSecurityModerateFixed()));
            deo.setLicenseBacklogRate(
                    calculateBacklogRate(deo.getLicenseDiscovered(), deo.getLicenseFixed()));
            deo.setLicenseCriticalBacklogRate(
                    calculateBacklogRate(
                            deo.getLicenseCriticalDiscovered(), deo.getLicenseCriticalFixed()));
            deo.setLicenseSevereBacklogRate(
                    calculateBacklogRate(
                            deo.getLicenseSevereDiscovered(), deo.getLicenseSevereFixed()));
            deo.setLicenseModerateBacklogRate(
                    calculateBacklogRate(
                            deo.getLicenseModerateDiscovered(), deo.getLicenseModerateFixed()));

            deo.setSecurityDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(), deo.getSecurityDiscovered()));
            deo.setSecurityCriticalDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(),
                            deo.getSecurityCriticalDiscovered()));
            deo.setSecuritySevereDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(), deo.getSecuritySevereDiscovered()));
            deo.setSecurityModerateDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(),
                            deo.getSecurityModerateDiscovered()));
            deo.setLicenseDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(), deo.getLicenseDiscovered()));
            deo.setLicenseCriticalDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(), deo.getLicenseCriticalDiscovered()));
            deo.setLicenseSevereDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(), deo.getLicenseSevereDiscovered()));
            deo.setLicenseModerateDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(), deo.getLicenseModerateDiscovered()));

            deo.setAllDiscovered(deo.getSecurityDiscovered() + deo.getLicenseDiscovered());
            deo.setAllOpen(deo.getSecurityOpen() + deo.getLicenseOpen());
            deo.setAllFixed(deo.getSecurityFixed() + deo.getLicenseFixed());
            deo.setAllWaived(deo.getSecurityWaived() + deo.getLicenseWaived());

            deo.setRiskRatio(
                    calculateRiskRatio(deo.getTotalApplicationsOnboard(), deo.getAllOpen()));
            deo.setBacklogRate(calculateBacklogRate(deo.getAllDiscovered(), deo.getAllFixed()));
            deo.setDiscoveryRate(
                    calculateDiscoveryRate(
                            deo.getTotalApplicationsOnboard(), deo.getAllDiscovered()));

            deoList.add(deo);
        }

        String csvFilename = fileIoService.makeFilename("data-extract", "csv", timestamp);

        fileIoService.writeDataExtractCsvFile(csvFilename, deoList);
        log.info("Created data extract file {}", csvFilename);
    }

    private Double calculateRiskRatio(int applications, int openViolations) {
        return HelperService.roundDouble(Double.valueOf(openViolations) / applications, 1);
    }

    private int calculateBacklogRate(int discovered, int fixed) {
        return Math.round(((float) discovered / fixed) * 100);
    }

    private float calculateDiscoveryRate(int applications, int discovered) {
        // discovered violations / number of applications = discovery rate to two decimal
        // places
        // (rounded up)
        BigDecimal riskRatio =
                BigDecimal.valueOf(discovered)
                        .divide(BigDecimal.valueOf(applications), 2, RoundingMode.HALF_UP);
        return riskRatio.floatValue();
    }
}
