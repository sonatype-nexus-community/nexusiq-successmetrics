package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.service.CsvFileService;
import org.sonatype.cs.getmetrics.service.FileIoService;
import org.sonatype.cs.getmetrics.service.PolicyIdsService;
import org.sonatype.cs.getmetrics.service.UtilService;
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

    PolicyIdsService policyIdsService;

    public PolicyViolations(PolicyIdsService policyIdsService) {
        this.policyIdsService = policyIdsService;
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonReader reader) {
        log.info("Make Policy Violations Report");

        List<String[]> data = getPolicyViolationsFromData(reader);

        f.writeCsvFile(FilenameInfo.policyViolationsCsvFile, data);
    }

    static List<String[]> getPolicyViolationsFromData(JsonReader reader) {
        List<String[]> data = new ArrayList<>();
        data.add(
                new String[] {
                    "policyName",
                    "reason",
                    "applicationName",
                    "openTime",
                    "component",
                    "stage",
                    "threatLevel"
                });

        JsonObject obj = reader.readObject();
        JsonArray results = obj.getJsonArray("applicationViolations");

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {

            JsonObject application = result.getJsonObject("application");
            JsonArray policyViolations = result.getJsonArray("policyViolations");

            String applicationPublicId = application.getString("publicId");

            for (JsonObject policyViolation : policyViolations.getValuesAs(JsonObject.class)) {
                String stage = policyViolation.getString("stageId");
                String openTime = policyViolation.getString("openTime");
                String policyName = policyViolation.getString("policyName");
                int threatLevel = policyViolation.getInt("threatLevel");

                JsonObject component = policyViolation.getJsonObject("component");
                String packageUrl =
                        (component.get("packageUrl") != null)
                                ? String.valueOf(component.get("packageUrl"))
                                : "";

                JsonArray constraintViolations =
                        policyViolation.getJsonArray("constraintViolations");
                for (JsonObject constraintViolation :
                        constraintViolations.getValuesAs(JsonObject.class)) {
                    JsonArray reasons = constraintViolation.getJsonArray("reasons");

                    String reason = "";
                    if (policyName.equalsIgnoreCase("Integrity-Rating")) {
                        reason = "Integrity-Rating";
                    } else if (PolicyIdsService.isSecurityPolicy(policyName)) {
                        reason = getCVE(reasons);
                    } else if (PolicyIdsService.isLicensePolicy(policyName)) {
                        reason = getLicense(reasons);
                    }

                    String[] line = {
                        policyName,
                        reason,
                        applicationPublicId,
                        openTime,
                        packageUrl,
                        stage,
                        String.valueOf(threatLevel)
                    };
                    data.add(line);
                }
            }
        }
        return data;
    }

    static String getCVE(JsonArray reasons) {
        List<String> cves = new ArrayList<>();
        for (JsonObject reason : reasons.getValuesAs(JsonObject.class)) {
            String cve = "";
            if (!reason.isNull("reference")) {
                JsonObject reference = reason.getJsonObject("reference");
                cve = reference.getString("value");
            }
            if (!cves.contains(cve)) {
                cves.add(cve);
            }
        }

        StringBuilder cveList = new StringBuilder();
        for (String cve : cves) {
            cveList.append(cve);
            cveList.append(":");
        }

        return UtilService.removeLastChar(cveList.toString());
    }

    static String getLicense(JsonArray reasons) {
        List<String> licenses = new ArrayList<>();

        for (JsonObject reason : reasons.getValuesAs(JsonObject.class)) {
            String licenseFound = reason.getString("reason");

            Pattern pattern = Pattern.compile("^.+\'(.+)\'");
            Matcher matcher = pattern.matcher(licenseFound);
            if (matcher.find()) {
                licenseFound = matcher.group(1);
            }

            if (!licenses.contains(licenseFound)) {
                licenses.add(licenseFound);
            }
        }

        StringBuilder licenseList = new StringBuilder();
        for (String license : licenses) {
            licenseList.append(license);
            licenseList.append(":");
        }

        return UtilService.removeLastChar(licenseList.toString());
    }

    @Override
    public void makeCsvFile(FileIoService f, JsonObject reader) {
        throw new NotImplementedException();
    }
}
