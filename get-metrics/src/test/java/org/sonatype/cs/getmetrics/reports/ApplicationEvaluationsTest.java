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

public class ApplicationEvaluationsTest {
    @Test
    void testGetApplicationDataFromData() throws FileNotFoundException {
        JsonReader jsonReader =
                Json.createReader(
                        new FileInputStream(
                                "src/test/java/org/sonatype/cs/getmetrics/resources/ApplicationReport.json"));
        String[] headerRow = new String[] {"applicationName", "evaluationDate", "stage"};
        String[] dataRow1 = new String[] {"Test123", "2015-01-16T13:14:32.139-05:00", "build"};
        String[] dataRow2 =
                new String[] {"Test123", "2014-07-14T15:08:09.501-04:00", "stage-release"};
        List<String[]> data = ApplicationEvaluations.getApplicationInfoFromData(jsonReader);
        Assertions.assertEquals(3, data.size());
        Assertions.assertArrayEquals(headerRow, data.get(0));
        Assertions.assertArrayEquals(dataRow1, data.get(1));
        Assertions.assertArrayEquals(dataRow2, data.get(2));
    }

    @Test
    void testMakeCsvFile() {
        ApplicationEvaluations applicationEvaluation = new ApplicationEvaluations();
        FileIoService fileIOService = null;
        JsonObject jsonObject = null;
        Assertions.assertThrows(
                NotImplementedException.class,
                () -> {
                    applicationEvaluation.makeCsvFile(fileIOService, jsonObject);
                });
    }
}
