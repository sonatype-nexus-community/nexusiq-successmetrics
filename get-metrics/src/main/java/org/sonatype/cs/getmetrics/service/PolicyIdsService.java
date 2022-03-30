package org.sonatype.cs.getmetrics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

@Service
public class PolicyIdsService {
    private static final Logger log = LoggerFactory.getLogger(PolicyIdsService.class);

    private NexusIQApiDataService nexusIQDataService;

    public PolicyIdsService(NexusIQApiDataService nexusIQDataService) {
        this.nexusIQDataService = nexusIQDataService;
    }

    private static List<String> securityPolicies =
            Arrays.asList(
                    "Security-Critical",
                    "Security-High",
                    "Security-Medium",
                    "Security-Malicious",
                    "Security-Namespace Conflict",
                    "Integrity-Rating");

    private static List<String> licensePolicies =
            Arrays.asList("License-Banned", "License-None", "License-Copyleft");

    public String getPolicyIdsEndpoint() throws IOException {
        JsonObject dataObj = nexusIQDataService.getData("/policies");
        String policyIds = getPolicyIds(dataObj);
        return "/policyViolations?" + policyIds;
    }

    static String getPolicyIds(JsonObject obj) {
        log.info("Getting policy ids");

        JsonArray results = obj.getJsonArray("policies");

        String policyIds = "";

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            String id = result.getString("id");
            String pname = result.getString("name");

            if (isSecurityPolicy(pname) || isLicensePolicy(pname)) {
                policyIds = policyIds.concat("p=" + id + "&");
            }
        }

        policyIds = UtilService.removeLastChar(policyIds);
        return policyIds;
    }

    public static boolean isSecurityPolicy(String policyName) {
        return securityPolicies.contains(policyName);
    }

    public static boolean isLicensePolicy(String policyName) {
        return licensePolicies.contains(policyName);
    }
}
