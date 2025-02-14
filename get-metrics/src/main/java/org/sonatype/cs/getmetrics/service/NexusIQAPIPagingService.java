package org.sonatype.cs.getmetrics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

@Service
public class NexusIQAPIPagingService {
    private final NexusIqApiConnectionService nexusIqApiConnectionService;
    private final FileIoService fileIoService;
    private final String iqUrl;
    private final String iqUser;
    private final String iqPasswd;
    private final String iqApi;

    public NexusIQAPIPagingService(
            NexusIqApiConnectionService nexusIqApiConnectionService,
            FileIoService fileIoService,
            @Value("${iq.url}") String iqUrl,
            @Value("${iq.user}") String iqUser,
            @Value("${iq.passwd}") String iqPasswd,
            @Value("${iq.api}") String iqApi) {
        this.nexusIqApiConnectionService = nexusIqApiConnectionService;
        this.fileIoService = fileIoService;
        this.iqUrl = iqUrl;
        this.iqUser = iqUser;
        this.iqPasswd = iqPasswd;
        this.iqApi = iqApi;
    }

    public void makeReport(CsvFileService cfs, String endPoint) throws IOException {
        int page = 1;
        int pageCount;

        do {
            JsonObject obj = fetchPageData(endPoint, page);
            page = obj.getInt("page");
            pageCount = obj.getInt("pageCount");
            cfs.makeCsvFile(fileIoService, obj);
            page++;
        } while (page <= pageCount);
    }

    private JsonObject fetchPageData(String endPoint, int page) throws IOException {
        URLConnection urlConnection =
                nexusIqApiConnectionService.createAuthorizedPagedUrlConnection(
                        iqUser, iqPasswd, iqUrl, iqApi, endPoint, page, 10);
        try (InputStream is = urlConnection.getInputStream();
                JsonReader reader = Json.createReader(is)) {
            return reader.readObject();
        }
    }
}
