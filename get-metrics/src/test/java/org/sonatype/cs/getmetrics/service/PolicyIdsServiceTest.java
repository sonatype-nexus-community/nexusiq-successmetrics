package org.sonatype.cs.getmetrics.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.json.Json;
import javax.json.JsonObject;

public class PolicyIdsServiceTest {
    @Test
    void testGetPolicyIds() {
        try {
            JsonObject jsonObject =
                    Json.createReader(
                                    new FileInputStream(
                                            "src/test/java/org/sonatype/cs/getmetrics/resources/policies.json"))
                            .readObject();
            Assertions.assertEquals("p=id1&p=id2", PolicyIdsService.getPolicyIds(jsonObject));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @ParameterizedTest
    @CsvSource({
        "Security-Critical,security",
        "Security-High,security",
        "Security-Medium,security",
        "Security-Malicious,security",
        "Security-Namespace Conflict,security",
        "Integrity-Rating,security",
        "License-None,license",
        "License-Banned,license",
        "License-Copyleft,license",
        "Unknown,unknown"
    })
    void testIsLicensePolicy(String policy, String type) {
        Assertions.assertEquals(type.equals("license"), PolicyIdsService.isLicensePolicy(policy));
    }

    @ParameterizedTest
    @CsvSource({
        "Security-Critical,security",
        "Security-High,security",
        "Security-Medium,security",
        "Security-Malicious,security",
        "Security-Namespace Conflict,security",
        "Integrity-Rating,security",
        "License-None,license",
        "License-Banned,license",
        "License-Copyleft,license"
    })
    void testIsSecurityPolicy(String policy, String type) {
        Assertions.assertEquals(type.equals("security"), PolicyIdsService.isSecurityPolicy(policy));
    }
}
