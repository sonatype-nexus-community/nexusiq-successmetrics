package org.sonatype.cs.metrics;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(
        properties = {
            "data.dir=src/test/resources",
            "spring.profiles.active=insights",
            "data.successmetrics=successmetrics-2021-01-01-to-2021-06-01.csv"
        },
        webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuccessMetricsIsightsApplicationTestTo202106 {

    @Autowired private SuccessMetricsApplication controller;

    @Test
    public void checkCSVFileIsGenerated() throws Exception {
        assertNotNull(controller.gettimestamp());
        File outputFile =
                new File(
                        "src/test/resources/output/insights-" + controller.gettimestamp() + ".csv");
        assertTrue(outputFile.exists());
        String fileContents =
                new String(Files.readAllBytes(Paths.get(outputFile.getAbsolutePath())));
        Approvals.verify(fileContents);
    }
}
