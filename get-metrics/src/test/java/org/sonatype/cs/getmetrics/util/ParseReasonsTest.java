package org.sonatype.cs.getmetrics.util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javax.json.Json;
import javax.json.JsonArray;

public class ParseReasonsTest {
    @Test
    void testEmptyLicenseString() {
        JsonArray reasons = Json.createArrayBuilder()
        .add(Json.createObjectBuilder()
            .add("reason", ""))
        .build();
   
        String actualReason = ParseReasons.getLicense(reasons);
        Assertions.assertEquals("", actualReason);
    }

    @Test
    void testSingleLicenseString() {
        JsonArray reasons = Json.createArrayBuilder()
        .add(Json.createObjectBuilder()
            .add("reason", "(license)"))
        .build();
   
        String actualReason = ParseReasons.getLicense(reasons);
        Assertions.assertEquals("license", actualReason);
    }

    @Test
    void testMultipltLicenseString() {
        JsonArray reasons = Json.createArrayBuilder()
        .add(Json.createObjectBuilder()
            .add("reason", "(license)")
        )
        .add(Json.createObjectBuilder()
            .add("reason", "(license2)")
        )
        .build();
   
        String actualReason = ParseReasons.getLicense(reasons);
        Assertions.assertEquals("license:license2", actualReason);
    }
}
