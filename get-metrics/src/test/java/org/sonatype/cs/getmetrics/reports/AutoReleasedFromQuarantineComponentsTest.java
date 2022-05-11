package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sonatype.cs.getmetrics.service.FileIoService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class AutoReleasedFromQuarantineComponentsTest {
    @Test
    void testGetQuarantinedComponentsFromData() throws FileNotFoundException {
        JsonObject jsonObject =
                Json.createReader(
                                new FileInputStream(
                                        "src/test/java/org/sonatype/cs/getmetrics/resources/autoReleasedFromQuarantineCompononts.json"))
                        .readObject();

        String[] headerRow =
                new String[] {
                    "repository",
                    "quarantineDate",
                    "dateCleared",
                    "displayName",
                    "format",
                    "quarantined",
                    "policyName",
                    "threatLevel",
                    "reason"
                };
        String[] dataRow1 =
                new String[] {
                    "npm_proxy",
                    "2021-03-24T17:36:34.612+0000",
                    "2021-03-24T18:53:45.588+0000",
                    "1_test:0.0.0",
                    "npm",
                    "false",
                    "Integrity-Rating",
                    "9",
                    "Integrity-Rating"
                };
        String[] dataRow2 =
                new String[] {
                    "npm_proxy",
                    "2021-03-24T14:45:02.567+0000",
                    "2021-03-24T18:53:46.115+0000",
                    "rc-util:5.9.5",
                    "npm",
                    "false",
                    "Integrity-Rating",
                    "9",
                    "Integrity-Rating"
                };
        String[] dataRow3 =
                new String[] {
                    "npm_proxy",
                    "2021-03-24T14:45:02.567+0000",
                    "2021-03-24T18:53:46.115+0000",
                    "rc-util:5.9.6",
                    "npm",
                    "false",
                    "None",
                    "0",
                    ""
                };
        List<String[]> data =
                AutoReleasedFromQuarantineComponents.getQuarantinedComponentsFromData(jsonObject);
        Assertions.assertEquals(4, data.size());
        Assertions.assertArrayEquals(headerRow, data.get(0));
        Assertions.assertArrayEquals(dataRow1, data.get(1));
        Assertions.assertArrayEquals(dataRow2, data.get(2));
        Assertions.assertArrayEquals(dataRow3, data.get(3));
    }

    @Test
    void testMakeCsvFile() {
        AutoReleasedFromQuarantineComponents components =
                new AutoReleasedFromQuarantineComponents();
        FileIoService fileIOService = null;
        JsonReader jsonReader = null;
        Assertions.assertThrows(
                NotImplementedException.class,
                () -> {
                    components.makeCsvFile(fileIOService, jsonReader);
                });
    }
}
