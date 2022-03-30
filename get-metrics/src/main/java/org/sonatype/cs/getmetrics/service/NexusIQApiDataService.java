package org.sonatype.cs.getmetrics.service;

import org.sonatype.cs.getmetrics.util.NexusIqApiConnection;
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
    private String iqUrl;
    private String iqUser;
    private String iqPasswd;
    private String iqApi;

    public NexusIQApiDataService(
            @Value("${iq.url}") String iqUrl,
            @Value("${iq.user}") String iqUser,
            @Value("${iq.passwd}") String iqPasswd,
            @Value("${iq.api}") String iqApi) {
        this.iqUrl = iqUrl;
        this.iqUser = iqUser;
        this.iqPasswd = iqPasswd;
        this.iqApi = iqApi;
    }

    public JsonObject getData(String endPoint) throws IOException {
        HttpURLConnection urlConnection =
                NexusIqApiConnection.createAuthorisedUrlConnection(
                        iqUser, iqPasswd, iqUrl, iqApi, endPoint);

        return getJsonReaderFromURLConnection(urlConnection);
    }

    static JsonObject getJsonReaderFromURLConnection(HttpURLConnection urlConnection)
            throws IOException {
        try (InputStream is = urlConnection.getInputStream()) {
            try (JsonReader reader = Json.createReader(is)) {
                return reader.readObject();
            }
        }
    }
}
