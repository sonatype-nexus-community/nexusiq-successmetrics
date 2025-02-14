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

public class QuarantinedComponents implements CsvFileService {
    private static final Logger log = LoggerFactory.getLogger(QuarantinedComponents.class);

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonReader reader) {
        throw new NotImplementedException();
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonObject dataObject) {
        log.info("Making QuarantinedComponents report");

        List<String[]> data = getQuarentinedComponentsFromData(dataObject);

        fileIoService.writeCsvFile(FilenameInfo.QUARANTINED_COMPONENTS_CSV_FILE, data);
    }

    static List<String[]> getQuarentinedComponentsFromData(JsonObject dataObject) {
        List<String[]> data = new ArrayList<>();
        data.add(
                new String[] {
                    "repository",
                    "quarantineDate",
                    "dateCleared",
                    "displayName",
                    "format",
                    "quarantined",
                    "policyName",
                    "threatLevel",
                    "reason"
                });

        JsonArray results = dataObject.getJsonArray("results");

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String displayName = result.getString("displayName", "").replace(" ", "");
            String repository = result.getString("repository", "");
            String quarantineDate = result.getString("quarantineDate", "");
            String dateCleared = result.getString("dateCleared", "");
            Boolean quarantined =
                    result.isNull("quarantined") ? null : result.getBoolean("quarantined");
            String format = result.getJsonObject("componentIdentifier").getString("format", "");

            JsonArray quarantinePolicyViolations =
                    result.getJsonArray("quarantinePolicyViolations");
            if (quarantinePolicyViolations.isEmpty()) {
                data.add(
                        createDataLine(
                                repository,
                                quarantineDate,
                                dateCleared,
                                displayName,
                                format,
                                quarantined,
                                "None",
                                "0",
                                ""));
            } else {
                processQuarantinePolicyViolations(
                        data,
                        quarantinePolicyViolations,
                        repository,
                        quarantineDate,
                        dateCleared,
                        displayName,
                        format,
                        quarantined);
            }
        }
        return data;
    }

    private static void processQuarantinePolicyViolations(
            List<String[]> data,
            JsonArray quarantinePolicyViolations,
            String repository,
            String quarantineDate,
            String dateCleared,
            String displayName,
            String format,
            Boolean quarantined) {
        for (JsonObject quarantinePolicyViolation :
                quarantinePolicyViolations.getValuesAs(JsonObject.class)) {
            String policyName = quarantinePolicyViolation.getString("policyName", "");
            int threatLevel = quarantinePolicyViolation.getInt("threatLevel");

            JsonArray constraintViolations =
                    quarantinePolicyViolation.getJsonArray("constraintViolations");
            for (JsonObject constraintViolation :
                    constraintViolations.getValuesAs(JsonObject.class)) {
                JsonArray reasons = constraintViolation.getJsonArray("reasons");
                String reason = ParseReasons.getReason(policyName, reasons);

                data.add(
                        createDataLine(
                                repository,
                                quarantineDate,
                                dateCleared,
                                displayName,
                                format,
                                quarantined,
                                policyName,
                                String.valueOf(threatLevel),
                                reason));
            }
        }
    }

    private static String[] createDataLine(
            String repository,
            String quarantineDate,
            String dateCleared,
            String displayName,
            String format,
            Boolean quarantined,
            String policyName,
            String threatLevel,
            String reason) {
        return new String[] {
            repository,
            quarantineDate,
            dateCleared,
            displayName,
            format,
            String.valueOf(quarantined),
            policyName,
            threatLevel,
            reason
        };
    }
}
