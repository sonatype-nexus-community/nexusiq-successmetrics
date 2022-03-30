package org.sonatype.cs.getmetrics.service;

import org.sonatype.cs.getmetrics.util.NexusIqApiConnection;
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
    private FileIoService fileIoService;
    private String iqUrl;
    private String iqUser;
    private String iqPasswd;
    private String iqApi;

    public NexusIQAPIPagingService(
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

        int page = 1;
        int pageSize = 10;
        int pageCount = 0;

        do {
            URLConnection urlConnection =
                    NexusIqApiConnection.createAuthorizedPagedUrlConnection(
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
