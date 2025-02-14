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
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

public class Waivers implements CsvFileService {
    private static final Logger log = LoggerFactory.getLogger(Waivers.class);

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonReader reader) {
        log.info("Making Waivers report");

        List<String[]> data = getWaiversInformationFromData(reader);

        fileIoService.writeCsvFile(FilenameInfo.WAIVERS_CSV_FILE, data);
    }

    static List<String[]> getWaiversInformationFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(
                new String[] {
                    "applicationName",
                    "stage",
                    "packageUrl",
                    "policyName",
                    "threatLevel",
                    "comment",
                    "createDate",
                    "expiryTime"
                });

        JsonObject obj = reader.readObject();

        data.addAll(doWaivers("application", obj.getJsonArray("applicationWaivers")));
        data.addAll(doWaivers("repository", obj.getJsonArray("repositoryWaivers")));

        return data;
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonObject reader) {
        throw new NotImplementedException();
    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    static List<String[]> doWaivers(String waiverType, JsonArray waivers) {
        List<String[]> data = new ArrayList<>();

        for (JsonObject result : waivers.getValuesAs(JsonObject.class)) {
            JsonObject waiverObj = result.getJsonObject(waiverType);
            String applicationName = waiverObj.getString("publicId", "");

            for (JsonObject stage : result.getJsonArray("stages").getValuesAs(JsonObject.class)) {
                String stageId = stage.getString("stageId", "");

                for (JsonObject componentViolation :
                        stage.getJsonArray("componentPolicyViolations")
                                .getValuesAs(JsonObject.class)) {
                    JsonObject component = componentViolation.getJsonObject("component");
                    String packageUrl = getFieldStringFromJsonObject(component, "packageUrl");

                    for (JsonObject waivedPolicyViolation :
                            componentViolation
                                    .getJsonArray("waivedPolicyViolations")
                                    .getValuesAs(JsonObject.class)) {
                        String policyName = waivedPolicyViolation.getString("policyName", "");
                        int threatLevel = waivedPolicyViolation.getInt("threatLevel");

                        JsonObject policyWaiver =
                                waivedPolicyViolation.getJsonObject("policyWaiver");
                        String comment = getFieldStringFromJsonObject(policyWaiver, "comment");
                        String createTime =
                                getFieldStringFromJsonObject(policyWaiver, "createTime");
                        String expiryTime =
                                getFieldStringFromJsonObject(policyWaiver, "expiryTime");

                        data.add(
                                new String[] {
                                    applicationName,
                                    stageId,
                                    packageUrl,
                                    policyName,
                                    String.valueOf(threatLevel),
                                    comment,
                                    createTime,
                                    expiryTime
                                });
                    }
                }
            }
        }

        return data;
    }

    private static String getFieldStringFromJsonObject(JsonObject jsonObject, String field) {
        if (log.isDebugEnabled()) {
            log.debug("getFieldStringFromJsonObject called for " + field);
        }

        JsonValue value = jsonObject.get(field);
        if (value == null) {
            return "";
        }

        if (value instanceof JsonString) {
            return ((JsonString) value).getString();
        } else if (value instanceof JsonStructure) {
            return "";
        } else {
            String result = value.toString();
            return "{}".equals(result) ? "" : result;
        }
    }
}
