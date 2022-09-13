package org.sonatype.cs.getmetrics.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.model.PayloadItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class NexusIQSuccessMetrics {
    private static final Logger log = LoggerFactory.getLogger(NexusIQSuccessMetrics.class);

    private NexusIqApiConnectionService nexusIqApiConnectionService;
    private FileIoService fileIoService;

    private String iqUrl;
    private String iqUser;
    private String iqPwd;
    private String iqSmPeriod;
    private String iqApiFirstTimePeriod;
    private String iqApiLastTimePeriod;
    private String iqApiApplicationName;
    private String iqApiOrganisationName;
    private String iqApi;
    private String iqReportsEndpoint;

    public NexusIQSuccessMetrics(
            NexusIqApiConnectionService nexusIqApiConnectionService,
            FileIoService fileIoService,
            @Value("${iq.url}") String iqUrl,
            @Value("${iq.user}") String iqUser,
            @Value("${iq.passwd}") String iqPwd,
            @Value("${iq.api.sm.period}") String iqSmPeriod,
            @Value("${iq.api.sm.payload.timeperiod.first}") String iqApiFirstTimePeriod,
            @Value("${iq.api.sm.payload.timeperiod.last}") String iqApiLastTimePeriod,
            @Value("${iq.api.sm.payload.application.name}") String iqApiApplicationName,
            @Value("${iq.api.sm.payload.organisation.name}") String iqApiOrganisationName,
            @Value("${iq.api}") String iqApi,
            @Value("${iq.api.reports}") String iqReportsEndpoint) {
        this.nexusIqApiConnectionService = nexusIqApiConnectionService;
        this.fileIoService = fileIoService;
        this.iqUrl = iqUrl;
        this.iqUser = iqUser;
        this.iqPwd = iqPwd;
        this.iqSmPeriod = iqSmPeriod;
        this.iqApiFirstTimePeriod = iqApiFirstTimePeriod;
        this.iqApiLastTimePeriod = iqApiLastTimePeriod;
        this.iqApiApplicationName = iqApiApplicationName;
        this.iqApiOrganisationName = iqApiOrganisationName;
        this.iqApi = iqApi;
        this.iqReportsEndpoint = iqReportsEndpoint;
    }

    public void createSuccessMetricsCsvFile() throws IOException, JSONException, HttpException {
        String apiPayload = getPayload();

        String content =
                nexusIqApiConnectionService.retrieveCsvBasedOnPayload(
                        iqUser, iqPwd, iqUrl, iqApi, iqReportsEndpoint, apiPayload);
        fileIoService.writeSuccessMetricsFile(
                IOUtils.toInputStream(content, StandardCharsets.UTF_8));
    }

    private String getPayload() throws IOException, JSONException, HttpException {
        log.info("Making api payload");

        PayloadItem firstTimePeriod =
                new PayloadItem(UtilService.removeQuotesFromString(iqApiFirstTimePeriod));
        PayloadItem lastTimePeriod =
                new PayloadItem(UtilService.removeQuotesFromString(iqApiLastTimePeriod));
        PayloadItem organisationName = new PayloadItem(iqApiOrganisationName);
        PayloadItem applicationName = new PayloadItem(iqApiApplicationName);

        if (!firstTimePeriod.isExists()) {
            throw new IllegalArgumentException(
                    "No start period specified (iq.api.payload.timeperiod.first)");
        }

        JSONObject ajson = new JSONObject();
        ajson.put("timePeriod", UtilService.removeQuotesFromString(iqSmPeriod.toUpperCase()));
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
        if ("/".equals(UtilService.lastChar(iqUrl))) {
            iqUrl = UtilService.removeLastChar(iqUrl);
        }
        String url = iqUrl + endpoint;
        log.info("Fetching data from: {}", url);

        HttpURLConnection urlConnection =
                nexusIqApiConnectionService.prepareHttpURLGetForJSON(
                        iqUser, iqPwd, iqUrl, endpoint);
        return nexusIqApiConnectionService.executeHttpURLGetForJson(urlConnection);
    }
}
