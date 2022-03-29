package org.sonatype.cs.getmetrics.service;

import org.slf4j.Logger;
import org.sonatype.cs.getmetrics.util.NexusIqApiConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.json.Json;
import javax.json.JsonReader;

@Service
public class NexusIQApiService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(NexusIQApiService.class);

    private FileIoService fileIoService;
    private String iqUrl;
    private String iqUser;
    private String iqPasswd;
    private String iqApi;

    public NexusIQApiService(
            FileIoService fileIoService,
            @Value("${iq.url}") String iqUrl,
            @Value("${iq.user}") String iqUser,
            @Value("${iq.passwd}") String iqPasswd,
            @Value("${iq.api}") String iqApi) {
        this.fileIoService = fileIoService;
        this.iqUrl = iqUrl;
        this.iqUser = iqUser;
        this.iqPasswd = iqPasswd;
        this.iqApi = iqApi;
    }

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
