package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.service.CsvFileService;
import org.sonatype.cs.getmetrics.service.FileIoService;
import org.sonatype.cs.getmetrics.service.PolicyIdsService;
import org.sonatype.cs.getmetrics.util.FilenameInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class PolicyViolations implements CsvFileService {
    private static final Logger log = LoggerFactory.getLogger(PolicyViolations.class);

    private final PolicyIdsService policyIdsService;

    public PolicyViolations(PolicyIdsService policyIdsService) {
        this.policyIdsService = policyIdsService;
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonReader reader) {
        log.info("Make Policy Violations Report");

        List<String[]> data = getPolicyViolationsFromData(reader);

        fileIoService.writeCsvFile(FilenameInfo.POLICY_VIOLATIONS_CSV_FILE, data);
    }

    static List<String[]> getPolicyViolationsFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"policyName", "reason", "applicationName", "openTime", "component", "stage", "threatLevel"});

        JsonObject obj = reader.readObject();
        JsonArray results = obj.getJsonArray("applicationViolations");

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            JsonObject application = result.getJsonObject("application");
            JsonArray policyViolations = result.getJsonArray("policyViolations");

            String applicationPublicId = application.getString("publicId", "");

            for (JsonObject policyViolation : policyViolations.getValuesAs(JsonObject.class)) {
                String stage = policyViolation.getString("stageId", "");
                String openTime = policyViolation.getString("openTime", "");
                String policyName = policyViolation.getString("policyName", "");
                int threatLevel = policyViolation.getInt("threatLevel");

                JsonObject component = policyViolation.getJsonObject("component");
                String packageUrl = component.getString("packageUrl", "");

                JsonArray constraintViolations = policyViolation.getJsonArray("constraintViolations");
                for (JsonObject constraintViolation : constraintViolations.getValuesAs(JsonObject.class)) {
                    JsonArray reasons = constraintViolation.getJsonArray("reasons");

                    String reason = getReason(policyName, reasons);

                    String[] line = {policyName, reason, applicationPublicId, openTime, packageUrl, stage, String.valueOf(threatLevel)};
                    data.add(line);
                }
            }
        }
        return data;
    }

    private static String getReason(String policyName, JsonArray reasons) {
        if (policyName.equalsIgnoreCase("Integrity-Rating")) {
            return "Integrity-Rating";
        } else if (PolicyIdsService.isSecurityPolicy(policyName)) {
            return getCVE(reasons);
        } else if (PolicyIdsService.isLicensePolicy(policyName)) {
            return getLicense(reasons);
        }
        return "";
    }

    static String getCVE(JsonArray reasons) {
        List<String> cves = new ArrayList<>();
        for (JsonObject reason : reasons.getValuesAs(JsonObject.class)) {
            if (!reason.isNull("reference")) {
                JsonObject reference = reason.getJsonObject("reference");
                String cve = reference.getString("value", "");
                if (!cves.contains(cve)) {
                    cves.add(cve);
                }
            }
        }
        return String.join(":", cves);
    }

    static String getLicense(JsonArray reasons) {
        List<String> licenses = new ArrayList<>();
        Pattern pattern = Pattern.compile("^.+\'(.+)\'");

        for (JsonObject reason : reasons.getValuesAs(JsonObject.class)) {
            String licenseFound = reason.getString("reason", "");
            Matcher matcher = pattern.matcher(licenseFound);
            if (matcher.find()) {
                licenseFound = matcher.group(1);
            }
            if (!licenses.contains(licenseFound)) {
                licenses.add(licenseFound);
            }
        }
        return String.join(":", licenses);
    }

    @Override
    public void makeCsvFile(FileIoService fileIoService, JsonObject reader) {
        throw new NotImplementedException();
    }
}
