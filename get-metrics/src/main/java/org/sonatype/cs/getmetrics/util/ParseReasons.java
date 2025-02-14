package org.sonatype.cs.getmetrics.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.reports.Waivers;
import org.sonatype.cs.getmetrics.service.PolicyIdsService;
import java.util.Set;
import java.util.stream.Collectors;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class ParseReasons {
    private static final Logger log = LoggerFactory.getLogger(Waivers.class);

    private ParseReasons() {}

    public static String getReason(String policyName, JsonArray reasons) {
        if (policyName.equalsIgnoreCase("Integrity-Rating")) {
            return "Integrity-Rating";
        } else if (PolicyIdsService.isSecurityPolicy(policyName)) {
            return getCVE(reasons);
        } else if (PolicyIdsService.isLicensePolicy(policyName)) {
            return getLicense(reasons);
        }
        return "";
    }

    private static String getCVE(JsonArray reasons) {
        Set<String> cves = reasons.getValuesAs(JsonObject.class).stream()
                .filter(reason -> !reason.isNull("reference"))
                .map(reason -> reason.getJsonObject("reference").getString("value"))
                .collect(Collectors.toSet());

        return String.join(":", cves);
    }

    public static String getLicense(JsonArray reasons) {
        Set<String> licenses = reasons.getValuesAs(JsonObject.class).stream()
                .map(reason -> reason.getString("reason"))
                .filter(licenseFound -> !licenseFound.isEmpty())
                .peek(licenseFound -> {
                    if (!licenseFound.contains("(") || !licenseFound.contains(")")) {
                        log.debug("licenseFound string does not contain brackets: " + licenseFound);
                    }
                })
                .filter(licenseFound-> licenseFound.contains("(") && licenseFound.contains(")"))
                .map(licenseFound -> licenseFound.substring(licenseFound.indexOf("(") + 1, licenseFound.indexOf(")")).replace("'", ""))
                .collect(Collectors.toSet());

        return String.join(":", licenses);
    }
}
