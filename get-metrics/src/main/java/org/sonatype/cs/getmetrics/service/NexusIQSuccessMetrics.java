package org.sonatype.cs.getmetrics.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.model.PayloadItem;
import org.sonatype.cs.getmetrics.util.NexusIqApiConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class NexusIQSuccessMetrics {
    private static final Logger log = LoggerFactory.getLogger(NexusIQSuccessMetrics.class);

    @Autowired private FileIoService fileIoService;

    @Value("${iq.url}")
    private String iqUrl;

    @Value("${iq.user}")
    private String iqUser;

    @Value("${iq.passwd}")
    private String iqPwd;

    @Value("${iq.api.sm.period}")
    private String iqSmPeriod;

    @Value("${iq.api.sm.payload.timeperiod.first}")
    private String iqApiFirstTimePeriod;

    @Value("${iq.api.sm.payload.timeperiod.last}")
    private String iqApiLastTimePeriod;

    @Value("${iq.api.sm.payload.application.name}")
    private String iqApiApplicationName;

    @Value("${iq.api.sm.payload.organisation.name}")
    private String iqApiOrganisationName;

    @Value("${iq.api}")
    private String iqApi;

    @Value("${iq.api.reports}")
    private String iqReportsEndpoint;

    public void createSuccessMetricsCsvFile() throws IOException, JSONException, HttpException {
        String apiPayload = getPayload();

        String content =
                NexusIqApiConnection.retrieveCsvBasedOnPayload(
                        iqUser, iqPwd, iqUrl, iqApi, iqReportsEndpoint, apiPayload);
        fileIoService.writeSuccessMetricsFile(
                IOUtils.toInputStream(content, StandardCharsets.UTF_8));
    }

    private String getPayload() throws IOException, JSONException, HttpException {
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

            String[] organizationIds = getIds("organizations", organisationName.getItem());
            ajson.put("organizationIds", organizationIds);

        } else if (applicationName.isExists()) {

            String[] applicationIds = getIds("applications", applicationName.getItem());
            ajson.put("applicationIds", applicationIds);
        }

        log.info("Api Payload: {}", ajson);

        return ajson.toString();
    }

    private String[] getIds(String endPointName, String namesStr)
            throws IOException, HttpException {
        String apiEndpoint = iqApi + "/" + endPointName;
        String content = getIqData(apiEndpoint);

        String[] names = namesStr.split(",");
        String[] ids = new String[names.length];

        for (int i = 0; i < names.length; i++) {
            String id = getId(content, endPointName, names[i]);
            ids[i] = id;
        }

        return ids;
    }

    private String getId(String content, String endpoint, String aoName) {
        String s = null;

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
                s = oId;
                break;
            }
        }

        return s;
    }

    private String getIqData(String endpoint) throws IOException, HttpException {

        String url = iqUrl + endpoint;
        log.info("Fetching data from: {}", url);

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
            throw new HttpException(
                    "Failed with HTTP error code : "
                            + statusCode
                            + " ["
                            + response.getStatusLine().getReasonPhrase()
                            + "]");
        }

        return EntityUtils.toString(response.getEntity());
    }
}
