package org.sonatype.cs.metrics;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.String;
import java.lang.System;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.approvaltests.Approvals;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(properties = { "data.dir=src/test/resources" }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuccessMetricsWebApplicationTest {

	@Autowired
	private SuccessMetricsApplication controller;

	@LocalServerPort
	private int port;

	private String removeLine(String inputString, int lineToRemove) {
		List<String> lineArray = new LinkedList<String>(
				Arrays.asList(inputString.split(System.getProperty("line.separator"))));
		lineArray.remove(lineToRemove - 1);
		String outputString = String.join(System.getProperty("line.separator"), lineArray);
		return outputString;
	}

	@Test
	public void contextLoadsTest() throws Exception {
		assertNotNull(controller);
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void defaultPageTest() throws Exception {
		assertTrue(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class)
				.contains("Success Metrics"));
	}

	@Test
	public void summaryPageTest() throws Exception {
		assertTrue(this.restTemplate.getForObject("http://localhost:" + port + "/summary", String.class)
				.contains("Summary Report"));
	}

	@Test
	public void applicationsPageTest() throws Exception {
		assertTrue(this.restTemplate.getForObject("http://localhost:" + port + "/applications", String.class)
				.contains("Applications Report"));
	}

	@Test
	public void securityViolationsPageTest() throws Exception {
		assertTrue(this.restTemplate.getForObject("http://localhost:" + port + "/securityviolations", String.class)
				.contains("Security Violations Report"));
	}

	@Test
	public void licenseViolationsPageTest() throws Exception {
		assertTrue(this.restTemplate.getForObject("http://localhost:" + port + "/licenseviolations", String.class)
				.contains("License Violations Report"));
	}

	@Test
	public void defaultPageContentTest() throws Exception {
		String pageContents = this.restTemplate.getForObject("http://localhost:" + port + "/summary", String.class);
		pageContents = removeLine(pageContents, 23);
		Approvals.verify(pageContents);
	}

	@Test
	public void summaryPageContentTest() throws Exception {
		String pageContents = this.restTemplate.getForObject("http://localhost:" + port + "/summary", String.class);
		pageContents = removeLine(pageContents, 23);
		Approvals.verify(pageContents);
	}

	@Test
	public void applicationsPageContentTest() throws Exception {
		String pageContents = this.restTemplate.getForObject("http://localhost:" + port + "/applications",
				String.class);
		pageContents = removeLine(pageContents, 23);
		Approvals.verify(pageContents);
	}

	@Test
	public void securityViolationsPageContentTest() throws Exception {
		String pageContents = this.restTemplate.getForObject("http://localhost:" + port + "/securityviolations",
				String.class);
		pageContents = removeLine(pageContents, 23);
		Approvals.verify(pageContents);
	}

	@Test
	public void licenseViolationsPageContentTest() throws Exception {
		String pageContents = this.restTemplate.getForObject("http://localhost:" + port + "/licenseviolations",
				String.class);
		pageContents = removeLine(pageContents, 23);
		Approvals.verify(pageContents);
	}
}
