package org.sonatype.cs.getmetrics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.JsonArray;
import javax.json.JsonObject;

@Service
public class PolicyIdsService {
    private static final Logger log = LoggerFactory.getLogger(PolicyIdsService.class);

    private final NexusIQApiDataService nexusIQDataService;

    private static final List<String> SECURITY_POLICIES = Arrays.asList(
            "Security-Critical",
            "Security-High",
            "Security-Medium",
            "Security-Malicious",
            "Security-Namespace Conflict",
            "Integrity-Rating");

    private static final List<String> LICENSE_POLICIES = Arrays.asList(
            "License-Banned",
            "License-None",
            "License-Copyleft");

    public PolicyIdsService(NexusIQApiDataService nexusIQDataService) {
        this.nexusIQDataService = nexusIQDataService;
    }

    public String getPolicyIdsEndpoint() throws IOException {
        JsonObject dataObj = nexusIQDataService.getData("/policies");
        String policyIds = getPolicyIds(dataObj);
        return "/policyViolations?" + policyIds;
    }

    static String getPolicyIds(JsonObject obj) {
        log.info("Getting policy ids");

        JsonArray results = obj.getJsonArray("policies");

        String policyIds = results.getValuesAs(JsonObject.class).stream()
                .filter(result -> isSecurityPolicy(result.getString("name")) || isLicensePolicy(result.getString("name")))
                .map(result -> "p=" + result.getString("id"))
                .collect(Collectors.joining("&"));

        return policyIds;
    }

    public static boolean isSecurityPolicy(String policyName) {
        return SECURITY_POLICIES.contains(policyName);
    }

    public static boolean isLicensePolicy(String policyName) {
        return LICENSE_POLICIES.contains(policyName);
    }
}
