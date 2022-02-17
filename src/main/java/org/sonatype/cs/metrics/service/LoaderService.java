package org.sonatype.cs.metrics.service;

import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.PayloadItem;
import org.sonatype.cs.metrics.util.DataLoaderParams;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class LoaderService {

    private static final Logger log = LoggerFactory.getLogger(LoaderService.class);

    @Autowired private DbService dbService;

    @Autowired private FileIoService fileIoService;

    @Autowired private PeriodsDataService periodsDataService;

    @Value("${data.includelatestperiod}")
    private boolean includelatestperiod;

    @Value("${data.loadInsightsMetrics}")
    private boolean loadInsightsMetrics;

    @Value("${data.dir}")
    private String dataDir;

    @Value("${iq.url}")
    private String iqUrl;

    @Value("${iq.user}")
    private String iqUser;

    @Value("${iq.pwd}")
    private String iqPwd;

    @Value("${iq.api.payload.timeperiod.first}")
    private String iqApiFirstTimePeriod;

    @Value("${iq.api.payload.timeperiod.last}")
    private String iqApiLastTimePeriod;

    @Value("${iq.api.payload.application.name}")
    private String iqApiApplicationName;

    @Value("${iq.api.payload.organisation.name}")
    private String iqApiOrganisationName;

    @Value("${data.successmetrics}")
    private String successmetricsFile;

    private String iqSmEndpoint = "api/v2/reports/metrics";

    private boolean applicationEvaluationsFileLoaded = false;
    private boolean policyViolationsDataLoaded = false;
    private boolean componentWaiversLoaded = false;
    private boolean autoreleasedFromQuarantineComponentsLoaded = false;
    private boolean quarantinedComponentsLoaded = false;

    public boolean loadMetricsFile(String fileName, String header, String stmt) throws IOException {
        boolean status = false;

        String filePath =
                Paths.get(System.getProperty("user.dir"))
                        .resolve(Paths.get(dataDir).resolve(fileName))
                        .toString();

        log.info("Loading file: {}", filePath);

        if (isHeaderValid(filePath, header)) {
            status = loadFile(filePath, stmt);
        }

        return status;
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

    private boolean loadFile(String fileName, String stmt) {
        String sqlStmt = stmt + " ('" + fileName + "')";

        dbService.runSqlLoad(sqlStmt);

        log.info("Loaded file: {}", fileName);

        return true;
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

    public void filterOutLatestPeriod(String endPeriod) {
        String sqlStmt = "delete from metric where time_period_start = " + "'" + endPeriod + "'";
        dbService.runSqlLoad(sqlStmt);
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

    public void createSmDatafile(String iqSmPeriod)
            throws IOException, JSONException, HttpException {
        log.info("Creating successmetrics.csv file");

        StringEntity apiPayload = getPayload(iqSmPeriod);

        String metricsUrl = iqUrl + "/" + iqSmEndpoint;
        HttpPost request = new HttpPost(metricsUrl);

        String auth = iqUser + ":" + iqPwd;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth, StandardCharsets.ISO_8859_1);

        request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        request.addHeader("Accept", "text/csv");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(apiPayload);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != 200) {
            throw new HttpException("Failed with HTTP error code : " + statusCode);
        }

        log.info("Created successmetrics.csv file");

        InputStream content = response.getEntity().getContent();
        fileIoService.writeSuccessMetricsFile(content);
    }

    private StringEntity getPayload(String iqSmPeriod)
            throws IOException, JSONException, HttpException {
        log.info("Making api payload");

        PayloadItem firstTimePeriod = new PayloadItem(iqApiFirstTimePeriod);
        PayloadItem lastTimePeriod = new PayloadItem(iqApiLastTimePeriod);
        PayloadItem organisationName = new PayloadItem(iqApiOrganisationName);
        PayloadItem applicationName = new PayloadItem(iqApiApplicationName);

        if (!firstTimePeriod.isExists()) {
            throw new IllegalArgumentException(
                    "No start period specified (iq.api.payload.timeperiod.first)");
        }

        JSONObject ajson = new JSONObject();
        ajson.put("timePeriod", iqSmPeriod.toUpperCase());
        ajson.put("firstTimePeriod", firstTimePeriod.getItem());

        if (lastTimePeriod.isExists()) {
            ajson.put("lastTimePeriod", lastTimePeriod.getItem());
        }

        // organisation takes precedence
        if (organisationName.isExists()) {
            ajson.put("organizationIds", getId("organizations", organisationName.getItem()));
        } else if (applicationName.isExists()) {
            ajson.put("applicationIds", getId("applications", applicationName.getItem()));
        }

        log.info("Api Payload: {}", ajson);

        return new StringEntity(ajson.toString());
    }

    private String[] getId(String endpoint, String aoName) throws IOException, HttpException {
        String[] s = new String[1];

        String apiEndpoint = "/api/v2/" + endpoint;

        String content = getIqData(apiEndpoint);

        JSONObject jsonObject = new JSONObject(content);

        JSONArray jsonArray = jsonObject.getJSONArray(endpoint);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jObject = jsonArray.getJSONObject(i);

            String oName = jObject.getString("name");
            String oId = jObject.getString("id");

            if (oName.equals(aoName)) {
                StringBuilder ep = new StringBuilder(endpoint);
                log.info(
                        "Reporting for {}: {} [{}]", ep.deleteCharAt(ep.length() - 1), aoName, oId);
                s[0] = oId;
                break;
            }
        }

        return s;
    }

    private String getIqData(String endpoint) throws IOException, HttpException {

        String url = iqUrl + "/" + endpoint;
        HttpGet request = new HttpGet(url);

        String auth = iqUser + ":" + iqPwd;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth, StandardCharsets.ISO_8859_1);

        request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        request.addHeader("Content-Type", "application/json");

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != 200) {
            throw new HttpException("Failed with HTTP error code : " + statusCode);
        }

        return EntityUtils.toString(response.getEntity());
    }

    public void loadReports2() throws IOException {

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
                        DataLoaderParams.QCOMPDATAFILE,
                        DataLoaderParams.QCOMPHEADER,
                        SqlStatements.QUARANTINEDCOMPONENTSTABLE));
        setAutoreleasedFromQuarantineComponentsLoaded(
                this.loadMetricsFile(
                        DataLoaderParams.AFQCOMPONENTDATAFILE,
                        DataLoaderParams.AFQCOMPONENTHEADER,
                        SqlStatements.AUTORELEASEDFROMQUARANTINEDCOMPONENTSTABLE));
    }
}
