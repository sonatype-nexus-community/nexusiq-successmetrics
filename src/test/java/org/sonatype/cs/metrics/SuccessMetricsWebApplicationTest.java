package org.sonatype.cs.metrics;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(
        properties = {"data.dir=src/test/resources", "spring.profiles.active=web"},
        webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuccessMetricsWebApplicationTest {

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

    @Test
    public void defaultPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/", String.class)
                        .contains("Success Metrics"));
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/home", String.class)
                        .contains("Success Metrics"));
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/home.html", String.class)
                        .contains("Success Metrics"));
    }

    @Test
    public void summaryPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/summary", String.class)
                        .contains("Summary Report"));
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/summary.html", String.class)
                        .contains("Summary Report"));
    }

    @Test
    public void applicationsPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/applications", String.class)
                        .contains("Applications Report"));
        assertTrue(
                this.restTemplate
                        .getForObject(
                                "http://localhost:" + port + "/applications.html", String.class)
                        .contains("Applications Report"));
    }

    @Test
    public void securityViolationsPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject(
                                "http://localhost:" + port + "/securityviolations", String.class)
                        .contains("Security Violations Report"));
        assertTrue(
                this.restTemplate
                        .getForObject(
                                "http://localhost:" + port + "/securityviolations.html",
                                String.class)
                        .contains("Security Violations Report"));
    }

    @Test
    public void licenseViolationsPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject(
                                "http://localhost:" + port + "/licenseviolations", String.class)
                        .contains("License Violations Report"));
        assertTrue(
                this.restTemplate
                        .getForObject(
                                "http://localhost:" + port + "/licenseviolations.html",
                                String.class)
                        .contains("License Violations Report"));
    }

    @Test
    public void PolicyViolationsReportPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/violationsage", String.class)
                        .contains("Policy Violations Age"));
        assertTrue(
                this.restTemplate
                        .getForObject(
                                "http://localhost:" + port + "/violationsage.html", String.class)
                        .contains("Policy Violations Age"));
    }

    @Test
    public void ApplicationEvaluationsReportPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/evaluations", String.class)
                        .contains("Application Last Date Evaluated"));
        assertTrue(
                this.restTemplate
                        .getForObject(
                                "http://localhost:" + port + "/evaluations.html", String.class)
                        .contains("Application Last Date Evaluated"));
    }

    @Test
    public void omponentWaiversReportPageTest() throws Exception {
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/waivers", String.class)
                        .contains("Components Waivers Report"));
        assertTrue(
                this.restTemplate
                        .getForObject("http://localhost:" + port + "/waivers.html", String.class)
                        .contains("Components Waivers Report"));
    }

    @Test
    public void defaultPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject("http://localhost:" + port + "/", String.class);
        pageContents = removeLine(pageContents, 23);
        Approvals.verify(pageContents);
    }

    @Test
    public void summaryPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/summary.html", String.class);
        pageContents = removeLine(pageContents, 23);
        Approvals.verify(pageContents);
    }

    @Test
    public void applicationsPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/applications.html", String.class);
        pageContents = removeLine(pageContents, 23);
        Approvals.verify(pageContents);
    }

    @Test
    public void securityViolationsPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/securityviolations.html", String.class);
        pageContents = removeLine(pageContents, 23);
        Approvals.verify(pageContents);
    }

    @Test
    public void licenseViolationsPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/licenseviolations.html", String.class);
        pageContents = removeLine(pageContents, 23);
        Approvals.verify(pageContents);
    }

    @Test
    public void PolicyViolationsReportPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/violationsage.html?date=2020-12-15",
                        String.class);
        pageContents = removeLine(pageContents, 24);
        Approvals.verify(pageContents);
    }

    @Test
    public void ApplicationEvaluationsReportPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/evaluations.html?date=2021-12-15",
                        String.class);
        pageContents = removeLine(pageContents, 23);
        Approvals.verify(pageContents);
    }

    @Test
    public void ComponentWaiversReportPageContentTest() throws Exception {
        String pageContents =
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/waivers.html", String.class);
        pageContents = removeLine(pageContents, 24);
        Approvals.verify(pageContents);
    }
}
