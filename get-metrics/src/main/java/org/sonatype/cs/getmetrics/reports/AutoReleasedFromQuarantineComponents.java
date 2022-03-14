package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.service.CsvFileService;
import org.sonatype.cs.getmetrics.service.FileIoService;
import org.sonatype.cs.getmetrics.util.FilenameInfo;
import org.sonatype.cs.getmetrics.util.ParseReasons;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class AutoReleasedFromQuarantineComponents implements CsvFileService {
    private static final Logger log =
            LoggerFactory.getLogger(AutoReleasedFromQuarantineComponents.class);

    @Override
    public void makeCsvFile(FileIoService f, JsonReader reader) {
        throw new NotImplementedException();
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonObject dataObject) {
        log.info("Making AutoReleasedFromQuarantineComponents report");

        List<String[]> data = getQuarantinedComponentsFromData(dataObject);

        f.writeCsvFile(FilenameInfo.autoReleasedFromQuarantineComponentsCsvFile, data);
    }

    static List<String[]> getQuarantinedComponentsFromData(JsonObject dataObject) {
        List<String[]> data = new ArrayList<>();
        data.add(FilenameInfo.autoReleasedFromQuarantineComponentsFileHeader);

        JsonArray results = dataObject.getJsonArray("results");

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String displayName = result.getString("displayName");
            displayName = displayName.replace(" ", "");
            String repository = result.getString("repository");
            String quarantineDate = result.getString("quarantineDate");
            String dateCleared = result.getString("dateCleared", "none");
            boolean quarantined = result.getBoolean("quarantined");

            JsonObject componentIdentifier = result.getJsonObject("componentIdentifier");
            String format = componentIdentifier.getString("format");

            JsonArray quarantinePolicyViolations =
                    result.getJsonArray("quarantinePolicyViolations");
            for (JsonObject quarantinePolicyViolation :
                    quarantinePolicyViolations.getValuesAs(JsonObject.class)) {
                String policyName = quarantinePolicyViolation.getString("policyName");
                int threatLevel = quarantinePolicyViolation.getInt("threatLevel");

                JsonArray constraintViolations =
                        quarantinePolicyViolation.getJsonArray("constraintViolations");
                for (JsonObject constraintViolation :
                        constraintViolations.getValuesAs(JsonObject.class)) {
                    JsonArray reasons = constraintViolation.getJsonArray("reasons");
                    String reason = ParseReasons.getReason(policyName, reasons);

                    String[] line = {
                        repository,
                        quarantineDate,
                        dateCleared,
                        displayName,
                        format,
                        String.valueOf(quarantined),
                        policyName,
                        String.valueOf(threatLevel),
                        reason
                    };
                    data.add(line);
                }
            }
        }
        return data;
    }
}
