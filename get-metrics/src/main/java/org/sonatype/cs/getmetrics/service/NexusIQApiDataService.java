package org.sonatype.cs.getmetrics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

@Service
public class NexusIQApiDataService {
    private final NexusIqApiConnectionService nexusIqApiConnectionService;
    private final String iqUrl;
    private final String iqUser;
    private final String iqPasswd;
    private final String iqApi;

    public NexusIQApiDataService(
            NexusIqApiConnectionService nexusIqApiConnectionService,
            @Value("${iq.url}") String iqUrl,
            @Value("${iq.user}") String iqUser,
            @Value("${iq.passwd}") String iqPasswd,
            @Value("${iq.api}") String iqApi) {
        this.nexusIqApiConnectionService = nexusIqApiConnectionService;
        this.iqUrl = iqUrl;
        this.iqUser = iqUser;
        this.iqPasswd = iqPasswd;
        this.iqApi = iqApi;
    }

    public JsonObject getData(String endPoint) throws IOException {
        HttpURLConnection urlConnection =
                nexusIqApiConnectionService.createAuthorisedUrlConnection(
                        iqUser, iqPasswd, iqUrl, iqApi, endPoint);

        return getJsonReaderFromURLConnection(urlConnection);
    }

    static JsonObject getJsonReaderFromURLConnection(HttpURLConnection urlConnection)
            throws IOException {
        try (InputStream is = urlConnection.getInputStream();
                JsonReader reader = Json.createReader(is)) {
            return reader.readObject();
        }
    }
}
