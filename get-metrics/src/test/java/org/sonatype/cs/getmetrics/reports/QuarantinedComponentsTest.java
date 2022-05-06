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

public class QuarantinedComponentsTest {
    @Test
    void testGetQuarentinedComponentsFromData() {
        JsonObject jsonObject;
        try {
            jsonObject =
                    Json.createReader(
                                    new FileInputStream(
                                            "src/test/java/org/sonatype/cs/getmetrics/resources/quarantinedComponents.json"))
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
                        "2021-03-29T14:43:51.477+0000",
                        "",
                        "add-fedops:0.0.0",
                        "npm",
                        "true",
                        "Integrity-Rating",
                        "9",
                        "Integrity-Rating"
                    };
            List<String[]> data =
                    QuarantinedComponents.getQuarentinedComponentsFromData(jsonObject);
            Assertions.assertEquals(2, data.size());
            Assertions.assertArrayEquals(headerRow, data.get(0));
            Assertions.assertArrayEquals(dataRow1, data.get(1));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testMakeCsvFile() {
        FileIoService fileIoService = null;
        JsonReader jsonReader = null;
        QuarantinedComponents components = new QuarantinedComponents();
        Assertions.assertThrows(
                NotImplementedException.class,
                () -> components.makeCsvFile(fileIoService, jsonReader));
    }
}
