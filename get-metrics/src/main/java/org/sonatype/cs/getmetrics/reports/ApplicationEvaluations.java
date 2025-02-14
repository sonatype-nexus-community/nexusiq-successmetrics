package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.service.CsvFileService;
import org.sonatype.cs.getmetrics.service.FileIoService;
import org.sonatype.cs.getmetrics.util.FilenameInfo;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ApplicationEvaluations implements CsvFileService {
    private static final Logger log = LoggerFactory.getLogger(ApplicationEvaluations.class);

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonReader jsonReader) {
        log.info("Making ApplicationEvaluations report");

        List<String[]> data = getApplicationInfoFromData(jsonReader);

        fileIoService.writeCsvFile(FilenameInfo.APPLICATION_EVALUATIONS_CSV_FILE, data);
    }

    static List<String[]> getApplicationInfoFromData(JsonReader jsonReader) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] {"applicationName", "evaluationDate", "stage"});
        JsonArray results = jsonReader.readArray();

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String stage = result.getString("stage", "");
            String evaluationDate = result.getString("evaluationDate", "");
            String reportDataUrl = result.getString("reportDataUrl", "");
            String applicationName = extractApplicationName(reportDataUrl);
            data.add(new String[] {applicationName, evaluationDate, stage});
        }
        return data;
    }

    private static String extractApplicationName(String reportDataUrl) {
        return reportDataUrl.split("/", -1)[3];
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonObject jsonObject) {
        throw new NotImplementedException();
    }
}
