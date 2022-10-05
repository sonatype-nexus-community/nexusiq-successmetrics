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
    public void makeCsvFile(FileIoService f, JsonReader reader) {
        log.info("Making AutoReleasedFromQuarantineConfig report");

        List<String[]> data = getQuarantinedConfigFromData(reader);

        f.writeCsvFile(FilenameInfo.autoReleasedFromQuarantineConfigCsvFile, data);
    }

    static List<String[]> getQuarantinedConfigFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] {"id", "name", "autoReleaseQuarantineEnabled"});

        JsonArray results = reader.readArray();

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String id = result.getString("id");
            String name = result.getString("name");
            boolean autoReleaseQuarantineEnabled =
                    result.getBoolean("autoReleaseQuarantineEnabled");

            String[] line = {id, name, String.valueOf(autoReleaseQuarantineEnabled)};
            data.add(line);
        }
        return data;
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonObject reader) {
        throw new NotImplementedException();
    }
}
