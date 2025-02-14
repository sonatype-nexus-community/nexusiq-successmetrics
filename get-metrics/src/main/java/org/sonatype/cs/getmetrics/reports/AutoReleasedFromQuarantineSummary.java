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

public class AutoReleasedFromQuarantineSummary implements CsvFileService {
    private static final Logger log =
            LoggerFactory.getLogger(AutoReleasedFromQuarantineSummary.class);

    @Override
    public void makeCsvFile(FileIoService f, JsonReader reader) {
        log.info("Making AutoReleasedFromQuarantineSummary report");
        if (reader == null) {
            log.error("JsonReader is null");
            return;
        }
        List<String[]> data = getQuarantinedSummaryFromData(reader);

        f.writeCsvFile(FilenameInfo.AUTO_RELEASED_FROM_QUARANTINE_SUMMARY_CSV_FILE, data);
    }

    static List<String[]> getQuarantinedSummaryFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] {"MTD", "YTD"});

        JsonObject dataObj = reader.readObject();

        int autoReleaseQuarantineCountMTD = dataObj.getInt("autoReleaseQuarantineCountMTD");
        int autoReleaseQuarantineCountYTD = dataObj.getInt("autoReleaseQuarantineCountYTD");

        String[] line = {
            String.valueOf(autoReleaseQuarantineCountMTD),
            String.valueOf(autoReleaseQuarantineCountYTD)
        };
        data.add(line);
        return data;
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonObject reader) {
        throw new NotImplementedException();
    }
}
