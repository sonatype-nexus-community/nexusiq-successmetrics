package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class AutoReleasedFromQuarantineSummaryTest {
    @Test
    void testGetQuarantinedSummaryFromData() throws FileNotFoundException {
        JsonReader jsonReader =
                Json.createReader(
                        new FileInputStream(
                                "src/test/java/org/sonatype/cs/getmetrics/resources/autoReleasedFromQuarantineSummary.json"));
        String[] headerRow = new String[] {"MTD", "YTD"};
        String[] dataRow1 = new String[] {"3", "120"};
        List<String[]> data =
                AutoReleasedFromQuarantineSummary.getQuarantinedSummaryFromData(jsonReader);
        Assertions.assertEquals(2, data.size());
        Assertions.assertArrayEquals(headerRow, data.get(0));
        Assertions.assertArrayEquals(dataRow1, data.get(1));
    }

    @Test
    void testMakeCsvFile() {
        AutoReleasedFromQuarantineSummary component = new AutoReleasedFromQuarantineSummary();
        JsonObject jsonObject = null;
        Assertions.assertThrows(
                NotImplementedException.class, () -> component.makeCsvFile(null, jsonObject));
    }
}
