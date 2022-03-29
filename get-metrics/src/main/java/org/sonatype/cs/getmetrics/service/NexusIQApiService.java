package org.sonatype.cs.getmetrics.service;

import org.slf4j.Logger;
import org.sonatype.cs.getmetrics.util.NexusIqApiConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

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

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(NexusIQApiService.class);

    public void makeReport(CsvFileService cfs, String endPoint) throws IOException {
        HttpURLConnection urlConnection =
                NexusIqApiConnection.createAuthorisedUrlConnection(
                        iqUser, iqPasswd, iqUrl, iqApi, endPoint);
        InputStream is;
        try {
            is = urlConnection.getInputStream();
        } catch (IOException e) {
            logger.info(
                    "IOException raised when trying to reach iqUrl [{}], iqApi [{}], endpoint [{}]"
                            + " (return code [{} - {}]) - no CSV produced",
                    iqUrl,
                    iqApi,
                    endPoint,
                    urlConnection.getResponseCode(),
                    urlConnection.getResponseMessage());
            return;
        }
        try (JsonReader reader = Json.createReader(is)) {
            cfs.makeCsvFile(fileIoService, reader);
        }
    }
}
