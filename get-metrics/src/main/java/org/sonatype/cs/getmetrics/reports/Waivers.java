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
    public void makeCsvFile(FileIoService f, JsonReader reader) {
        log.info("Making Waivers report");

        List<String[]> data = getWaiversInformationFromData(reader);

        f.writeCsvFile(FilenameInfo.waiversCsvFile, data);
    }

    static List<String[]> getWaiversInformationFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(FilenameInfo.waiversFileHeader);

        JsonObject obj = reader.readObject();

        JsonArray applicationWaivers = obj.getJsonArray("applicationWaivers");
        List<String[]> aw = doWaivers("application", applicationWaivers);

        JsonArray repositoryWaivers = obj.getJsonArray("repositoryWaivers");
        List<String[]> rw = doWaivers("repository", repositoryWaivers);

        data.addAll(aw);
        data.addAll(rw);
        return data;
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonObject reader) {
        throw new NotImplementedException();
    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    static List<String[]> doWaivers(String waiverType, JsonArray waivers) {

        List<String[]> data = new ArrayList<>();

        for (JsonObject result : waivers.getValuesAs(JsonObject.class)) {

            JsonObject waiverObj = result.getJsonObject(waiverType);
            String applicationName = waiverObj.getString("publicId");

            JsonArray stages = result.getJsonArray("stages");

            for (JsonObject stage : stages.getValuesAs(JsonObject.class)) {
                String stageId = stage.getString("stageId");

                JsonArray componentViolations = stage.getJsonArray("componentPolicyViolations");

                for (JsonObject componentViolation :
                        componentViolations.getValuesAs(JsonObject.class)) {
                    JsonObject component = componentViolation.getJsonObject("component");
                    String packageUrl = getFieldStringFromJsonObject(component, "packageUrl");
                    JsonArray waivedPolicyViolations =
                            componentViolation.getJsonArray("waivedPolicyViolations");

                    for (JsonObject waivedPolicyViolation :
                            waivedPolicyViolations.getValuesAs(JsonObject.class)) {
                        String policyName = waivedPolicyViolation.getString("policyName");
                        int threatLevel = waivedPolicyViolation.getInt("threatLevel");

                        JsonObject policyWaiver =
                                waivedPolicyViolation.getJsonObject("policyWaiver");
                        String comment = getFieldStringFromJsonObject(policyWaiver, "comment");
                        String createTime =
                                getFieldStringFromJsonObject(policyWaiver, "createTime");
                        String expiryTime =
                                getFieldStringFromJsonObject(policyWaiver, "expiryTime");

                        String[] line = {
                            applicationName,
                            stageId,
                            packageUrl,
                            policyName,
                            String.valueOf(threatLevel),
                            comment,
                            createTime,
                            expiryTime
                        };
                        data.add(line);
                    }
                }
            }
        }

        return data;
    }

    private static String getFieldStringFromJsonObject(JsonObject policyWaiver, String field) {
        String result = "";
        if (log.isDebugEnabled()) {
            log.debug("getFieldStringFromJsonObject called for " + field);
        }
        if (policyWaiver.get(field) != null) {
            JsonValue value = policyWaiver.get(field);
            if (value instanceof JsonString) {
                result = ((JsonString) value).getString();
            } else if (value instanceof JsonStructure) {
                // this happens when override key is present without a value
                result = "";
            } else {
                result = value.toString();
            }
        }
        if (result.equals("{}")) {
            result = "";
        }
        return result;
    }
}
