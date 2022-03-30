package org.sonatype.cs.metrics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.util.DataLoaderParams;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class LoaderService {
    private static final Logger log = LoggerFactory.getLogger(LoaderService.class);

    private DbService dbService;
    private PeriodsDataService periodsDataService;
    private String metricsDir;
    private boolean includelatestperiod;
    private boolean loadInsightsMetrics;
    private String successmetricsFile;
    private boolean successMetricsFileLoaded;
    private boolean applicationEvaluationsFileLoaded;
    private boolean policyViolationsDataLoaded;
    private boolean componentWaiversLoaded;
    private boolean autoreleasedFromQuarantineComponentsLoaded;
    private boolean quarantinedComponentsLoaded;

    public LoaderService(
            DbService dbService,
            PeriodsDataService periodsDataService,
            @Value("${metrics.dir}") String metricsDir,
            @Value("${data.includelatestperiod}") boolean includelatestperiod,
            @Value("${data.loadInsightsMetrics}") boolean loadInsightsMetrics,
            @Value("${data.successmetrics}") String successmetricsFile) {
        this.dbService = dbService;
        this.periodsDataService = periodsDataService;
        this.metricsDir = metricsDir;
        this.includelatestperiod = includelatestperiod;
        this.loadInsightsMetrics = loadInsightsMetrics;
        this.successmetricsFile = successmetricsFile;
        this.successMetricsFileLoaded = false;
        this.applicationEvaluationsFileLoaded = false;
        this.policyViolationsDataLoaded = false;
        this.componentWaiversLoaded = false;
        this.autoreleasedFromQuarantineComponentsLoaded = false;
        this.quarantinedComponentsLoaded = false;
    }

    public boolean loadAllMetrics(String activeProfile) throws IOException {

        successMetricsFileLoaded = loadSuccessMetricsData();

        if (activeProfile.equalsIgnoreCase("web")) {

            setApplicationEvaluationsFileLoaded(
                    this.loadMetricsFile(
                            DataLoaderParams.AEDATAFILE,
                            DataLoaderParams.AEFILEHEADER,
                            SqlStatements.APPLICATIONEVALUATIONSTABLE));
            setPolicyViolationsDataLoaded(
                    this.loadMetricsFile(
                            DataLoaderParams.PVDATAFILE,
                            DataLoaderParams.PVFILEHEADER,
                            SqlStatements.POLICYVIOLATIONSTABLES));
            setComponentWaiversLoaded(
                    this.loadMetricsFile(
                            DataLoaderParams.CWDATAFILE,
                            DataLoaderParams.CWFILEHEADER,
                            SqlStatements.COMPONENTWAIVERSTABLE));
            setQuarantinedComponentsLoaded(
                    this.loadMetricsFile(
                            DataLoaderParams.QCDATAFILE,
                            DataLoaderParams.QCHEADER,
                            SqlStatements.QUARANTINEDCOMPONENTSTABLE));
            setAutoreleasedFromQuarantineComponentsLoaded(
                    this.loadMetricsFile(
                            DataLoaderParams.AFQCDATAFILE,
                            DataLoaderParams.AFQCHEADER,
                            SqlStatements.AUTORELEASEDFROMQUARANTINEDCOMPONENTSTABLE));
        }

        return successMetricsFileLoaded;
    }

    public boolean loadSuccessMetricsData() throws IOException {

        String stmt = SqlStatements.METRICSTABLE;
        boolean fileLoaded = loadMetricsFile(successmetricsFile, DataLoaderParams.SMHEADER, stmt);
        boolean doAnalysis = false;

        if (fileLoaded) {
            Map<String, Object> periods =
                    periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
            doAnalysis = (boolean) periods.get("doAnalysis");

            if (doAnalysis) {
                if (!includelatestperiod) {
                    String endPeriod = periods.get("endPeriod").toString();
                    filterOutLatestPeriod(endPeriod); // it is likely incomplete and only where we
                    // know multiple
                    // periods available
                    log.info("Removing incomplete data for current month {}", endPeriod);
                }

                if (loadInsightsMetrics) {
                    log.info("Loading insights data");
                    loadInsightsData();
                }
            }
        }

        return fileLoaded;
    }

    public void filterOutLatestPeriod(String endPeriod) {
        String sqlStmt = "delete from metric where time_period_start = " + "'" + endPeriod + "'";
        dbService.runSqlLoad(sqlStmt);
    }

    public void loadInsightsData() {
        Map<String, Object> periods =
                periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);

        String midPeriod = periods.get("midPeriod").toString();

        log.info("Mid period: {}", midPeriod);

        String sqlStmtP1 =
                "DROP TABLE IF EXISTS METRIC_P1; CREATE TABLE METRIC_P1 AS SELECT * FROM METRIC"
                        + " WHERE TIME_PERIOD_START <= '"
                        + midPeriod
                        + "'";

        dbService.runSqlLoad(sqlStmtP1);

        String sqlStmtP2 =
                "DROP TABLE IF EXISTS METRIC_P2; CREATE TABLE METRIC_P2 AS SELECT * FROM METRIC"
                        + " WHERE TIME_PERIOD_START > '"
                        + midPeriod
                        + "'";

        dbService.runSqlLoad(sqlStmtP2);
    }

    public boolean loadMetricsFile(String fileName, String header, String stmt) throws IOException {
        boolean status = false;

        String filePath =
                Paths.get(System.getProperty("user.dir"))
                        .resolve(Paths.get(metricsDir).resolve(fileName))
                        .toString();

        log.info("Loading file: {}", filePath);

        if (isHeaderValid(filePath, header)) {
            status = loadFile(filePath, stmt);
        }

        return status;
    }

    private boolean loadFile(String fileName, String stmt) {
        String sqlStmt = stmt + " ('" + fileName + "')";

        dbService.runSqlLoad(sqlStmt);

        log.info("Loaded file: {}", fileName);

        return true;
    }

    public boolean isQuarantinedComponentsLoaded() {
        return quarantinedComponentsLoaded;
    }

    public void setQuarantinedComponentsLoaded(boolean quarantinedComponentsLoaded) {
        this.quarantinedComponentsLoaded = quarantinedComponentsLoaded;
    }

    public boolean isAutoreleasedFromQuarantineComponentsLoaded() {
        return autoreleasedFromQuarantineComponentsLoaded;
    }

    public void setAutoreleasedFromQuarantineComponentsLoaded(
            boolean autoreleasedFromQuarantineComponentsLoaded) {
        this.autoreleasedFromQuarantineComponentsLoaded =
                autoreleasedFromQuarantineComponentsLoaded;
    }

    public boolean isComponentWaiversLoaded() {
        return componentWaiversLoaded;
    }

    public void setComponentWaiversLoaded(boolean componentWaiversLoaded) {
        this.componentWaiversLoaded = componentWaiversLoaded;
    }

    public boolean isPolicyViolationsDataLoaded() {
        return policyViolationsDataLoaded;
    }

    public void setPolicyViolationsDataLoaded(boolean policyViolationsDataLoaded) {
        this.policyViolationsDataLoaded = policyViolationsDataLoaded;
    }

    public boolean isApplicationEvaluationsFileLoaded() {
        return applicationEvaluationsFileLoaded;
    }

    public void setApplicationEvaluationsFileLoaded(boolean applicationEvaluationsFileLoaded) {
        this.applicationEvaluationsFileLoaded = applicationEvaluationsFileLoaded;
    }

    public static boolean isHeaderValid(String filename, String header) throws IOException {
        String metricsFile = filename;
        File f = new File(metricsFile);

        if (!f.exists()) {
            log.warn("File not found: {}", metricsFile);
            return false;
        }
        if (f.isDirectory() || f.length() <= 0) {
            log.info("No data");
            return false;
        }
        if (header.length() == 0) {
            return true;
        }

        String firstLine = getFirstLine(metricsFile);
        if (!firstLine.startsWith(header)) {
            log.error("Invalid header");
            log.error("-> {}", firstLine);
            return false;
        }

        if (countLines(metricsFile) == 1) {
            log.warn("No metrics found for: {}", metricsFile);
        }

        return countLines(metricsFile) >= 2;
    }

    public static String getFirstLine(String fileName) throws IOException {
        try (BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(fileName), StandardCharsets.ISO_8859_1))) {
            return br.readLine();
        }
    }

    public static int countLines(String fileName) throws IOException {
        try (BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(fileName), StandardCharsets.ISO_8859_1))) {
            String line = br.readLine();
            int lineCount = 0;

            while (line != null) {
                lineCount++;
                line = br.readLine();
            }

            return lineCount;
        }
    }
}
