package org.sonatype.cs.metrics.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoaderService {

	private static final Logger log = LoggerFactory.getLogger(LoaderService.class);
	
	@Autowired
	private DbService dbService;

	@Autowired
	private HelperService helperService;
	
	@Autowired
	private FileIoService fileIoService;
	
	@Autowired
	private PeriodsDataService periodsDataService;

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


	private String iqSmEndpoint = "api/v2/reports/metrics";
	
	public boolean successMetricsFileLoaded = false;
	public boolean applicationEvaluationsFileLoaded = false;
	public boolean policyViolationsDataLoaded = false;
	public boolean componentsQuarantineLoaded = false;
	public boolean componentWaiversLoaded = false;
	public boolean autoreleasedFromQuarantineComponentsLoaded = false;
	public boolean quarantinedComponentsLoaded = false;

	public boolean loadMetricsFile(String fileName, String header, String stmt) throws IOException {
		boolean status = false;
		
		String filePath = dataDir + "/" + fileName;
		log.info("Loading file: " + filePath);

		if (isHeaderValid(filePath, header)){
			status = loadFile(filePath, stmt);
		}

		return status;
	}

	private boolean loadFile(String fileName, String stmt) throws IOException {
		String sqlStmt = stmt + " ('" + fileName + "')";	
		
		dbService.runSqlLoad(sqlStmt);
		
		log.info("Loaded file: " + fileName);
		
		return true;
	}
	
	private boolean isHeaderValid(String filename, String header) throws IOException {

		boolean isValid = false;

		String metricsFile = filename;

		File f = new File(metricsFile);

		if (f.exists()){
				if (!f.isDirectory() && f.length() > 0) {
					isValid = true;

						if (header.length() > 0){
							String firstLine = this.getFirstLine(metricsFile);

							if (!firstLine.startsWith(header)) {
								log.error("Invalid header");
								log.error("-> " + firstLine);
								isValid = false;
							} 
							else {
								if (this.countLines(metricsFile) < 2){
									//log.warn("No metrics data in file");
									isValid = false;
								}
							}
						}
				}
				else {
					log.info("No data");
					isValid = false;
				}
		}
		else {
			log.warn("File not found: " + metricsFile);
		}
	
		return isValid;
	}

	private String getFirstLine(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName)); 

	    String line = br.readLine(); 
	    br.close();
	    return line;
	}

	private int countLines(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName)); 

		String line = br.readLine(); 
		int lineCount = 0;

		while (line != null){
			lineCount++;
			line = br.readLine();
		}

		br.close();
		return lineCount;
	}

	public void filterOutLatestPeriod(String endPeriod) throws ParseException {
		String sqlStmt = "delete from metric where time_period_start = " + "'" + endPeriod + "'";
		dbService.runSqlLoad(sqlStmt);
		return;
	}

	public boolean loadSuccessMetricsData() throws IOException, ParseException {

		String stmt = SqlStatements.MetricsTable;
		boolean fileLoaded = loadMetricsFile(DataLoaderParams.smDatafile, DataLoaderParams.smHeader, stmt);
		boolean doAnalysis = false;

		if (fileLoaded) {
			Map<String, Object> periods = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);
			doAnalysis  = (boolean) periods.get("doAnalysis");
			
			if (doAnalysis) {
				if (!includelatestperiod) {
					String endPeriod = periods.get("endPeriod").toString();
					filterOutLatestPeriod(endPeriod); // it is likely incomplete and only where we know multiple periods available
					log.info("Removing incomplete data for current month " + endPeriod);
				}

				if (doAnalysis && loadInsightsMetrics) {
					log.info("Loading insights data");
					loadInsightsData();
				}
			}
		}
		
		return fileLoaded;
	}

	public void loadInsightsData() throws ParseException {
		Map<String, Object> periods = periodsDataService.getPeriodData(SqlStatements.METRICTABLENAME);

		String midPeriod = periods.get("midPeriod").toString();
		
		log.info("Mid period: " + midPeriod);
		
		String sqlStmtP1 = "DROP TABLE IF EXISTS METRIC_P1; CREATE TABLE METRIC_P1 AS SELECT * FROM METRIC WHERE TIME_PERIOD_START <= '" + midPeriod + "'";
		dbService.runSqlLoad(sqlStmtP1);
		
		String sqlStmtP2 = "DROP TABLE IF EXISTS METRIC_P2; CREATE TABLE METRIC_P2 AS SELECT * FROM METRIC WHERE TIME_PERIOD_START > '" + midPeriod + "'";
		dbService.runSqlLoad(sqlStmtP2);
			 
		return;		
	}
	
	public void createSmDatafile(String iqSmPeriod) throws ClientProtocolException, IOException, JSONException, org.json.simple.parser.ParseException {
		log.info("Creating successmetrics.csv file");
		
		StringEntity apiPayload = getPayload(iqSmPeriod);
				
		String metricsUrl = iqUrl + "/" + iqSmEndpoint;
    	HttpPost request = new HttpPost(metricsUrl);

		String auth = iqUser + ":" + iqPwd;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = "Basic " + new String(encodedAuth);
		
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		request.addHeader("Accept", "text/csv");
		request.addHeader("Content-Type", "application/json");
        request.setEntity(apiPayload);

		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);

		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + statusCode);
	    }
	        
	    log.info("Created successmetrics.csv file");
	    
	    InputStream content = response.getEntity().getContent();
	    fileIoService.writeSuccessMetricsFile(content);
	    
	    return;
	}

	private StringEntity getPayload(String iqSmPeriod) throws IOException, JSONException, org.json.simple.parser.ParseException {
		log.info("Making api payload");

		PayloadItem firstTimePeriod = new PayloadItem(iqApiFirstTimePeriod, false);
		PayloadItem lastTimePeriod = new PayloadItem(iqApiLastTimePeriod, false);
		PayloadItem organisationName = new PayloadItem(iqApiOrganisationName, false);
		PayloadItem applicationName = new PayloadItem(iqApiApplicationName, false);

		if (!firstTimePeriod.isExists()) {
			throw new RuntimeException("No start period specified (iq.api.payload.timeperiod.first)");
		}

		JSONObject ajson = new JSONObject();
		ajson.put("timePeriod", iqSmPeriod.toUpperCase());
		ajson.put("firstTimePeriod", firstTimePeriod.getItem());
		
		if (lastTimePeriod.isExists()) {
			ajson.put("lastTimePeriod", lastTimePeriod.getItem());
		}
		
		// organisation takes precedence
		if (organisationName.isExists()){
			ajson.put("organizationIds", getId("organizations", organisationName.getItem()));
		}
		else if (applicationName.isExists()) {
			ajson.put("applicationIds", getId("applications", applicationName.getItem()));
		}
		
		log.info("Api Payload: " + ajson.toString());

		StringEntity params = new StringEntity(ajson.toString());

		return params;
	}
	
	private String[] getId(String endpoint, String aoName) throws ClientProtocolException, IOException, org.json.simple.parser.ParseException {
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
	        	StringBuffer ep = new StringBuffer(endpoint);
	        	log.info("Reporting for " + ep.deleteCharAt(ep.length()-1) + ": " + aoName + " [" + oId + "]");
	        	s[0] =  oId;
	        	break;
	        }
	    }

		return s;
	}
	
	private String getIqData(String endpoint) throws ClientProtocolException, IOException {
						
		String url = iqUrl + "/" + endpoint;
    	HttpGet request = new HttpGet(url);

		String auth = iqUser + ":" + iqPwd;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = "Basic " + new String(encodedAuth);
		
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		request.addHeader("Content-Type", "application/json");

		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);

		int statusCode = response.getStatusLine().getStatusCode();
		
		if (statusCode != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + statusCode);
	    }
	        	    
	    String jsonString = EntityUtils.toString(response.getEntity());   
	    return jsonString;
	}
	
	public void loadReports2() throws IOException {
		
		applicationEvaluationsFileLoaded = this.loadMetricsFile(DataLoaderParams.aeDatafile, DataLoaderParams.aeFileHeader, SqlStatements.ApplicationEvaluationsTable);
		policyViolationsDataLoaded = this.loadMetricsFile(DataLoaderParams.pvDatafile, DataLoaderParams.pvFileHeader,  SqlStatements.PolicyViolationsTables);
		//componentsQuarantineLoaded = this.loadMetricsFile(DataLoaderParams.cqDatafile, DataLoaderParams.cqFileHeader, SqlStatements.ComponentsInQuarantineTable);
		componentWaiversLoaded = this.loadMetricsFile(DataLoaderParams.cwDatafile, DataLoaderParams.cwFileHeader, SqlStatements.ComponentWaiversTable);
        quarantinedComponentsLoaded = this.loadMetricsFile(DataLoaderParams.qcompDatafile, DataLoaderParams.qcompHeader, SqlStatements.QuarantinedComponentsTable);
		autoreleasedFromQuarantineComponentsLoaded = this.loadMetricsFile(DataLoaderParams.afqcomponentDatafile, DataLoaderParams.afqcomponentHeader, SqlStatements.AutoreleasedFromQuarantinedComponentsTable);
		
	}
	
}
