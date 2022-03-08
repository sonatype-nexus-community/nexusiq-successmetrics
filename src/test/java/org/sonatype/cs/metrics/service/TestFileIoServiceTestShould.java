package org.sonatype.cs.metrics.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DisplayName("The fileIO service should")
public class TestFileIoServiceTestShould {
    @DisplayName("return true if a file exists")
    @Test
    void testFileExists() {
        try {
            Assertions.assertTrue(
                    FileIoService.fileExists("src/test/resources/iqmetrics/successmetrics.csv"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @DisplayName("return IOexception if a file does not exist")
    @Test
    void testFileExistsFailure() {
        Assertions.assertFalse(FileIoService.fileExists("unknown file"));
    }

    @DisplayName("return a string containing the contents of a JSON file")
    @Test
    void testReadJsonAsString() {
        try {
            Assertions.assertEquals(
                    "{\"Title\": \"This is a title\"}\n",
                    FileIoService.readJsonAsString("src/test/resources/test.json"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @DisplayName("return a string containing the contents of a JSON file")
    @Test
    void testReadJsonAsStringFails() {
        Assertions.assertThrows(
                IOException.class,
                () -> {
                    FileIoService.readJsonAsString("unknown file");
                });
    }

    @DisplayName("return a list of strings with the contents of a file")
    @Test
    void testfileToStringListfileToStringList() {
        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add("{\"Title\": \"This is a title\"}");
        try {
            Assertions.assertEquals(
                    expectedResult, FileIoService.fileToStringList("src/test/resources/test.json"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @DisplayName("throw an IOException if the requested file to convert to a list is empty")
    @Test
    void testfileToStringListfileToStringListFailure() {
        Assertions.assertThrows(
                IOException.class,
                () -> {
                    FileIoService.fileToStringList("unknown file");
                });
    }
}
