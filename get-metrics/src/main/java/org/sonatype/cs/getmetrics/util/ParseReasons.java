package org.sonatype.cs.getmetrics.util;

import org.sonatype.cs.getmetrics.service.PolicyIdsService;
import org.sonatype.cs.getmetrics.service.UtilService;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class ParseReasons {

    private ParseReasons() {}

    public static String getReason(String policyName, JsonArray reasons) {

        String reason = "";

        if (policyName.equalsIgnoreCase("Integrity-Rating")) {
            reason = "Integrity-Rating";
        } else if (PolicyIdsService.isSecurityPolicy(policyName)) {
            reason = getCVE(reasons);
        } else if (PolicyIdsService.isLicensePolicy(policyName)) {
            reason = getLicense(reasons);
        }

        return reason;
    }

    private static String getCVE(JsonArray reasons) {
        String cveList = "";
        List<String> cves = new ArrayList<>();

        for (JsonObject reason : reasons.getValuesAs(JsonObject.class)) {
            JsonObject reference = reason.getJsonObject("reference");
            String cve = reference.getString("value");

            if (!cves.contains(cve)) {
                cves.add(cve);
            }
        }

        for (String c : cves) {
            cveList = c + ":" + cveList;
        }

        cveList = UtilService.removeLastChar(cveList);

        return cveList;
    }

    private static String getLicense(JsonArray reasons) {
        String licenseList = "";
        List<String> licenses = new ArrayList<>();

        for (JsonObject reason : reasons.getValuesAs(JsonObject.class)) {
            String licenseFound = reason.getString("reason");

            String license =
                    licenseFound.substring(
                            licenseFound.indexOf("(") + 1, licenseFound.indexOf(")"));
            license = license.replace("'", "");

            if (!licenses.contains(license)) {
                licenses.add(license);
            }
        }

        for (String l : licenses) {
            licenseList = l + ":" + licenseList;
        }

        licenseList = UtilService.removeLastChar(licenseList);

        return licenseList;
    }
}
