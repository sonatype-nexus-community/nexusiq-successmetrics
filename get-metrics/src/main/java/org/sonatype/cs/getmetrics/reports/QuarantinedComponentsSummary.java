package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.service.CsvFileService;
import org.sonatype.cs.getmetrics.service.FileIoService;
import org.sonatype.cs.getmetrics.util.FilenameInfo;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class QuarantinedComponentsSummary implements CsvFileService {
    private static final Logger log = LoggerFactory.getLogger(QuarantinedComponentsSummary.class);

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonReader jsonReader) {
        log.info("Making QuarantinedComponentsSummary report");

        List<String[]> data = getQuarantinedComponentsSummaryFromData(jsonReader);

        fileIoService.writeCsvFile(FilenameInfo.QUARANTINED_COMPONENTS_SUMMARY_CSV_FILE, data);
    }

    static List<String[]> getQuarantinedComponentsSummaryFromData(JsonReader jsonReader) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] {
            "repositoryCount",
            "quarantineEnabledCount",
            "quarantineEnabled",
            "totalComponentCount",
            "quarantinedComponentCount"
        });

        JsonObject dataObj = jsonReader.readObject();

        String[] line = {
            String.valueOf(dataObj.getInt("repositoryCount")),
            String.valueOf(dataObj.getInt("quarantineEnabledRepositoryCount")),
            String.valueOf(dataObj.getBoolean("quarantineEnabled")),
            String.valueOf(dataObj.getInt("totalComponentCount")),
            String.valueOf(dataObj.getInt("quarantinedComponentCount"))
        };

        data.add(line);
        return data;
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonObject jsonObject) {
        throw new NotImplementedException("Method not implemented");
    }
}
