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
    public void makeCsvFile(FileIoService f, JsonReader reader) {
        log.info("Making QuarantinedComponentsSummary report");

        List<String[]> data = getQuarantinedComponentsSummaryFromData(reader);

        f.writeCsvFile(FilenameInfo.quarantinedComponentsSummaryCsvFile, data);
    }

    static List<String[]> getQuarantinedComponentsSummaryFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(FilenameInfo.quarantinedComponentsSummaryFileHeader);

        JsonObject dataObj = reader.readObject();

        int repositoryCount = dataObj.getInt("repositoryCount");
        int quarantineEnabledRepositoryCount = dataObj.getInt("quarantineEnabledRepositoryCount");
        boolean quarantineEnabled = dataObj.getBoolean("quarantineEnabled");
        int totalComponentCount = dataObj.getInt("totalComponentCount");
        int quarantinedComponentCount = dataObj.getInt("quarantinedComponentCount");

        String[] line = {
            String.valueOf(repositoryCount),
            String.valueOf(quarantineEnabledRepositoryCount),
            String.valueOf(quarantineEnabled),
            String.valueOf(totalComponentCount),
            String.valueOf(quarantinedComponentCount)
        };

        data.add(line);
        return data;
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonObject reader) {
        throw new NotImplementedException();
    }
}
