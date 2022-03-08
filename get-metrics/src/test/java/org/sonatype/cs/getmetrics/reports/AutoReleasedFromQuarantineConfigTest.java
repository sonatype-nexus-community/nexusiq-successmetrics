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

public class AutoReleasedFromQuarantineConfigTest {
    @Test
    void testGetQuarantinedConfigFromData() throws FileNotFoundException {
        JsonReader jsonReader =
                Json.createReader(
                        new FileInputStream(
                                "src/test/java/org/sonatype/cs/getmetrics/resources/autoReleasedFromQuarantineConfig.json"));
        String[] headerRow = new String[] {"id", "name", "autoReleaseQuarantineEnabled"};
        String[] dataRow1 = new String[] {"IntegrityRating", "Integrity Rating", "true"};
        String[] dataRow2 = new String[] {"License", "License", "true"};
        List<String[]> data =
                AutoReleasedFromQuarantineConfig.getQuarantinedConfigFromData(jsonReader);
        Assertions.assertEquals(3, data.size());
        Assertions.assertArrayEquals(headerRow, data.get(0));
        Assertions.assertArrayEquals(dataRow1, data.get(1));
        Assertions.assertArrayEquals(dataRow2, data.get(2));
    }

    @Test
    void testMakeCsvFilewithJsonObject() {
        AutoReleasedFromQuarantineConfig components = new AutoReleasedFromQuarantineConfig();
        FileIoService fileIoService = null;
        JsonObject jsonObject = null;
        Assertions.assertThrows(
                NotImplementedException.class,
                () -> components.makeCsvFile(fileIoService, jsonObject));
    }
}
