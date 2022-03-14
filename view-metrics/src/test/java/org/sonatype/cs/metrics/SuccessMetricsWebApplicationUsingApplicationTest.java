package org.sonatype.cs.metrics;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.approvaltests.Approvals;
import org.approvaltests.namer.NamedEnvironment;
import org.approvaltests.namer.NamerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(
        properties = {
            "metrics.dir=src/test/resources/iqmetrics",
            "spring.profiles.active=web",
            "iq.api.payload.application.name=ang-cycdx"
        },
        webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SuccessMetricsWebApplicationUsingApplicationTest {

    @Autowired private SuccessMetricsApplication controller;

    @LocalServerPort private int port;

    private String removeLine(String inputString, int lineToRemove) {
        List<String> lineArray =
                new LinkedList<String>(
                        Arrays.asList(inputString.split(System.getProperty("line.separator"))));
        lineArray.remove(lineToRemove - 1);
        String outputString = String.join(System.getProperty("line.separator"), lineArray);
        return outputString;
    }

    @Test
    public void contextLoadsTest() throws Exception {
        assertNotNull(controller);
    }

    @Autowired private TestRestTemplate restTemplate;

    @ParameterizedTest
    @CsvSource(value = {"applications.html, 23"})
    public void checkPageContents(String page, String lineToRemove) throws Exception {
        try (NamedEnvironment en = NamerFactory.withParameters(page)) {
            String pageContents =
                    this.restTemplate.getForObject(
                            "http://localhost:" + port + "/" + page, String.class);
            pageContents = removeLine(pageContents, Integer.parseInt(lineToRemove));
            Approvals.verify(pageContents);
        }
    }
}
