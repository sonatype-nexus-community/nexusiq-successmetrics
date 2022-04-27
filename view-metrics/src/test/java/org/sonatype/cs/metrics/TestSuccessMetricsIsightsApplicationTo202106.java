package org.sonatype.cs.metrics;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(
        properties = {
            "metrics.dir=src/test/resources",
            "spring.profiles.active=data",
            "data.successmetrics=successmetrics-2021-01-01-to-2021-06-01.csv"
        })
public class TestSuccessMetricsIsightsApplicationTo202106 {

    @Autowired private SuccessMetricsApplication controller;

    @Test
    public void checkCSVFileIsGenerated() throws Exception {
        assertNotNull(controller.gettimestamp());
        File outputFile = new File("../datafiles/insights-" + controller.gettimestamp() + ".csv");
        assertTrue(outputFile.exists());
        String fileContents =
                new String(Files.readAllBytes(Paths.get(outputFile.getAbsolutePath())));
        Approvals.verify(fileContents);
    }
}
