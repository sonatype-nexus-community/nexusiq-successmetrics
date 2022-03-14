package org.sonatype.cs.getmetrics.service;

import org.sonatype.cs.getmetrics.util.NexusIqApiConnection;
import org.springframework.beans.factory.annotation.Autowired;
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
