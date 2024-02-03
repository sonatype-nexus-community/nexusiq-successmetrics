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
    private static final Logger log = LoggerFactory.getLogger(AutoReleasedFromQuarantineComponents.class);

    private static final String[] CSV_HEADERS = {
            "repository", "quarantineDate", "dateCleared", "displayName", "format",
            "quarantined", "policyName", "threatLevel", "reason"
    };

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonReader reader) {
        throw new NotImplementedException();
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonObject dataObject) {
        log.info("Making AutoReleasedFromQuarantineComponents report");

        List<String[]> data = getQuarantinedComponentsFromData(dataObject);

        fileIoService.writeCsvFile(FilenameInfo.AUTO_RELEASED_FROM_QUARANTINE_COMPONENTS_CSV_FILE, data);
    }

    static List<String[]> getQuarantinedComponentsFromData(JsonObject dataObject) {
        List<String[]> data = new ArrayList<>();
        data.add(CSV_HEADERS);

        JsonArray results = dataObject.getJsonArray("results");

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String displayName = result.getString("displayName", "").replace(" ", "");
            String repository = result.getString("repository", "");
            String quarantineDate = result.getString("quarantineDate", "");
            String dateCleared = result.getString("dateCleared", "none");
            boolean quarantined = result.getBoolean("quarantined");
            String format = result.getJsonObject("componentIdentifier").getString("format");

            JsonArray quarantinePolicyViolations = result.getJsonArray("quarantinePolicyViolations");
            if (quarantinePolicyViolations.isEmpty()) {
                data.add(createCsvLine(repository, quarantineDate, dateCleared, displayName, format, quarantined, "None", 0, ""));
            } else {
                addPolicyViolations(data, repository, quarantineDate, dateCleared, displayName, format, quarantined, quarantinePolicyViolations);
            }
        }
        return data;
    }

    private static void addPolicyViolations(List<String[]> data, String repository, String quarantineDate, String dateCleared, String displayName, String format, boolean quarantined, JsonArray quarantinePolicyViolations) {
        for (JsonObject quarantinePolicyViolation : quarantinePolicyViolations.getValuesAs(JsonObject.class)) {
            String policyName = quarantinePolicyViolation.getString("policyName");
            int threatLevel = quarantinePolicyViolation.getInt("threatLevel");

            JsonArray constraintViolations = quarantinePolicyViolation.getJsonArray("constraintViolations");
            for (JsonObject constraintViolation : constraintViolations.getValuesAs(JsonObject.class)) {
                JsonArray reasons = constraintViolation.getJsonArray("reasons");
                String reason = ParseReasons.getReason(policyName, reasons);

                data.add(createCsvLine(repository, quarantineDate, dateCleared, displayName, format, quarantined, policyName, threatLevel, reason));
            }
        }
    }

    private static String[] createCsvLine(String repository, String quarantineDate, String dateCleared, String displayName, String format, boolean quarantined, String policyName, int threatLevel, String reason) {
        return new String[]{
                repository, quarantineDate, dateCleared, displayName, format,
                String.valueOf(quarantined), policyName, String.valueOf(threatLevel), reason
        };
    }
}
