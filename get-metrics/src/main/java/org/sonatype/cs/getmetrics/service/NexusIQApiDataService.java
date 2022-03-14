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
    @Value("${iq.url}")
    private String iqUrl;

    @Value("${iq.user}")
    private String iqUser;

    @Value("${iq.passwd}")
    private String iqPasswd;

    @Value("${iq.api}")
    private String iqApi;

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
