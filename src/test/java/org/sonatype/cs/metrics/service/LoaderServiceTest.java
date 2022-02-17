package org.sonatype.cs.metrics.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoaderServiceTest {
    @Test
    void testIsHeaderValid() {
        try {
            Assertions.assertTrue(
                    LoaderService.isHeaderValid(
                            "src/test/resources/successmetrics.csv",
                            "applicationId,applicationName,applicationPublicId,"));
            Assertions.assertFalse(
                    LoaderService.isHeaderValid(
                            "unknown file", "applicationId,applicationName,applicationPublicId,"));
            Assertions.assertFalse(
                    LoaderService.isHeaderValid(
                            "src/test/resources/successmetrics.csv", "Incorrect header"));
            Assertions.assertFalse(
                    LoaderService.isHeaderValid("src/test/resources/reports2/", "Dummy header"));
            Assertions.assertFalse(
                    LoaderService.isHeaderValid(
                            "src/test/resources/empty_file.csv", "Dummy header"));
            Assertions.assertTrue(
                    LoaderService.isHeaderValid(
                            "src/test/resources/file_with_header_but_no_content.csv", ""));
            Assertions.assertFalse(
                    LoaderService.isHeaderValid(
                            "src/test/resources/file_with_header_but_no_content.csv", "Header"));

        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testGetFirstLine() {
        try {
            Assertions.assertEquals(
                    "Header",
                    LoaderService.getFirstLine(
                            "src/test/resources/file_with_header_but_no_content.csv"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void testCountLines() {
        try {
            Assertions.assertEquals(
                    0, LoaderService.countLines("src/test/resources/empty_file.csv"));
            Assertions.assertEquals(
                    1,
                    LoaderService.countLines(
                            "src/test/resources/file_with_header_but_no_content.csv"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}
