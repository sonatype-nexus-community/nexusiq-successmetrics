package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sonatype.cs.getmetrics.service.NexusIQApiDataService;
import org.sonatype.cs.getmetrics.service.PolicyIdsService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class PolicyViolationsTest {
    @Test
    void testGetCVE() {
        try {
            JsonArray reasons =
                    Json.createReader(
                                    new FileInputStream(
                                            "src/test/java/org/sonatype/cs/getmetrics/resources/policyViolationsSecurityReasons.json"))
                            .readObject()
                            .getJsonArray("reasons");
            Assertions.assertEquals(
                    "CVE-2021-43466:CVE-2016-1000027:", PolicyViolations.getCVE(reasons));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testGetLicense() throws FileNotFoundException {
        try {
            JsonArray reasons =
                    Json.createReader(
                                    new FileInputStream(
                                            "src/test/java/org/sonatype/cs/getmetrics/resources/policyViolationsLicenseReasons.json"))
                            .readObject()
                            .getJsonArray("reasons");
            Assertions.assertEquals("MIT:MIT-O:Apache-2.0", PolicyViolations.getLicense(reasons));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testGetPolicyViolationsFromData() {
        try (JsonReader jsonReader =
                Json.createReader(
                        new FileInputStream(
                                "src/test/java/org/sonatype/cs/getmetrics/resources/policyViolations.json"))) {
            List<String[]> policyViolations =
                    PolicyViolations.getPolicyViolationsFromData(jsonReader);

            String[] header = {
                "policyName",
                "reason",
                "applicationName",
                "openTime",
                "component",
                "stage",
                "threatLevel"
            };
            String[] row1 = {
                "MIT license",
                "",
                "WebGoat",
                "2022-02-15T10:11:04.055+0000",
                "\"pkg:maven/org.projectlombok/lombok@1.18.20?type=jar\"",
                "build",
                "7"
            };
            String[] row2 = {
                "Security-Critical",
                "CVE-2021-21346",
                "WebGoat",
                "2022-02-14T15:00:33.236+0000",
                "\"pkg:maven/com.thoughtworks.xstream/xstream@1.4.5?type=jar\"",
                "build",
                "10"
            };
            String[] row3 = {
                "License-None",
                "Non-license string for testing",
                "WebGoat",
                "2022-02-15T10:11:04.055+0000",
                "\"pkg:maven/org.projectlombok/lombok@1.18.20?type=jar\"",
                "build",
                "7"
            };

            Assertions.assertEquals(4, policyViolations.size());
            Assertions.assertArrayEquals(header, policyViolations.get(0));
            Assertions.assertArrayEquals(row1, policyViolations.get(1));
            Assertions.assertArrayEquals(row2, policyViolations.get(2));
            Assertions.assertArrayEquals(row3, policyViolations.get(3));

        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testMakeCsvFile() {
        JsonObject jsonObject = null;
        NexusIQApiDataService nexusIQApiDataService =
                new NexusIQApiDataService(null, "URL", "USER", "PASSWORD", "API");
        PolicyIdsService policyIdsService = new PolicyIdsService(nexusIQApiDataService);
        PolicyViolations violations = new PolicyViolations(policyIdsService);
        Assertions.assertThrows(
                NotImplementedException.class, () -> violations.makeCsvFile(null, jsonObject));
    }
}
