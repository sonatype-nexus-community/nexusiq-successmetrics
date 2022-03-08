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

    public void makeCsvFile(FileIoService f, JsonReader reader) {
        log.info("Making ApplicationEvaluations report");

        List<String[]> data = getApplicationInfoFromData(reader);

        f.writeCsvFile(FilenameInfo.applicationEvaluationsCsvFile, data);
    }

    static List<String[]> getApplicationInfoFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(FilenameInfo.applicationEvaluationsFileHeader);
        JsonArray results = reader.readArray();

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String stage = result.getString("stage");
            String evaluationDate = result.getString("evaluationDate");
            String reportDataUrl = result.getString("reportDataUrl");
            String applicationName = reportDataUrl.split("/")[3];

            String[] line = {applicationName, evaluationDate, stage};
            data.add(line);
        }
        return data;
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonObject reader) {
        throw new NotImplementedException();
    }
}
