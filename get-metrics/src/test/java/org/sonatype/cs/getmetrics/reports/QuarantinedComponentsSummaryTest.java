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

public class QuarantinedComponentsSummaryTest {
    @Test
    void testGetQuarantinedComponentsSummaryFromData() {
        JsonReader jsonReader;
        try {
            jsonReader =
                    Json.createReader(
                            new FileInputStream(
                                    "src/test/java/org/sonatype/cs/getmetrics/resources/quarantinedComponentsSummary.json"));
            String[] headerRow =
                    new String[] {
                        "repositoryCount",
                        "quarantineEnabledCount",
                        "quarantineEnabled",
                        "totalComponentCount",
                        "quarantinedComponentCount"
                    };
            String[] dataRow1 = new String[] {"2", "2", "true", "25", "0"};
            List<String[]> data =
                    QuarantinedComponentsSummary.getQuarantinedComponentsSummaryFromData(
                            jsonReader);
            Assertions.assertEquals(2, data.size());
            Assertions.assertArrayEquals(headerRow, data.get(0));
            Assertions.assertArrayEquals(dataRow1, data.get(1));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testMakeCsvFile() {
        QuarantinedComponentsSummary quarantinedComponentsSummary =
                new QuarantinedComponentsSummary();
        FileIoService fileIoService = null;
        JsonObject jsonObject = null;
        Assertions.assertThrows(
                NotImplementedException.class,
                () -> quarantinedComponentsSummary.makeCsvFile(fileIoService, jsonObject));
    }
}
