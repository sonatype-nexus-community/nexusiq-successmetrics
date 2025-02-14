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

public class AutoReleasedFromQuarantineConfig implements CsvFileService {
    private static final Logger log =
            LoggerFactory.getLogger(AutoReleasedFromQuarantineConfig.class);

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonReader jsonReader) {
        log.info("Making AutoReleasedFromQuarantineConfig report");

        List<String[]> data = getQuarantinedConfigFromData(jsonReader);

        fileIoService.writeCsvFile(
                FilenameInfo.AUTO_RELEASED_FROM_QUARANTINE_CONFIG_CSV_FILE, data);
    }

    static List<String[]> getQuarantinedConfigFromData(JsonReader jsonReader) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] {"id", "name", "autoReleaseQuarantineEnabled"});

        JsonArray results = jsonReader.readArray();

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String id = result.getString("id", "");
            String name = result.getString("name", "");
            boolean autoReleaseQuarantineEnabled =
                    result.getBoolean("autoReleaseQuarantineEnabled");

            data.add(new String[] {id, name, String.valueOf(autoReleaseQuarantineEnabled)});
        }
        return data;
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonObject jsonObject) {
        throw new NotImplementedException("Method not implemented");
    }
}
