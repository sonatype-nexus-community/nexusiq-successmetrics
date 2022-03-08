package org.sonatype.cs.getmetrics.service;

import org.sonatype.cs.getmetrics.util.NexusIqApiConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonReader;

@Service
public class NexusIQApiService {

    @Autowired private FileIoService fileIoService;

    @Value("${iq.url}")
    private String iqUrl;

    @Value("${iq.user}")
    private String iqUser;

    @Value("${iq.passwd}")
    private String iqPasswd;

    @Value("${iq.api}")
    private String iqApi;

    public void makeReport(CsvFileService cfs, String endPoint) throws IOException {
        URLConnection urlConnection =
                NexusIqApiConnection.createAuthorisedUrlConnection(
                        iqUser, iqPasswd, iqUrl, iqApi, endPoint);
        InputStream is = urlConnection.getInputStream();
        try (JsonReader reader = Json.createReader(is)) {
            cfs.makeCsvFile(fileIoService, reader);
        }
    }
}
