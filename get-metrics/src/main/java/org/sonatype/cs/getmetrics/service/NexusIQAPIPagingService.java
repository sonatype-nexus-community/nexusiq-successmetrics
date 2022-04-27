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
    private NexusIqApiConnectionService nexusIqApiConnectionService;
    private FileIoService fileIoService;
    private String iqUrl;
    private String iqUser;
    private String iqPasswd;
    private String iqApi;

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
        int pageSize = 10;
        int pageCount = 0;

        do {
            URLConnection urlConnection =
                    nexusIqApiConnectionService.createAuthorizedPagedUrlConnection(
                            iqUser, iqPasswd, iqUrl, iqApi, endPoint, page, pageSize);
            InputStream is = urlConnection.getInputStream();
            JsonReader reader = Json.createReader(is);
            JsonObject obj = reader.readObject();
            page = obj.getInt("page");
            pageSize = obj.getInt("pageSize");
            pageCount = obj.getInt("pageCount");
            cfs.makeCsvFile(fileIoService, obj);
            reader.close();
            page += 1;
        } while (page <= pageCount);
    }
}
