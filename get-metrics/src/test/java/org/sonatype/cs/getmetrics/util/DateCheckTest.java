package org.sonatype.cs.getmetrics.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateCheckTest {
    @Test
    void testValidDates() {
        Assertions.assertTrue(DateCheck.checkDateFormat("MONTH", "2022-01"));
        Assertions.assertTrue(DateCheck.checkDateFormat("WEEK", "2022-W01"));
        Assertions.assertTrue(DateCheck.checkDateFormat("month", "2022-01"));
        Assertions.assertTrue(DateCheck.checkDateFormat("week", "2022-w01"));
        Assertions.assertTrue(DateCheck.checkDateFormat("week", "2022-W01"));
    }

    @Test
    void testInvalidDates() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> DateCheck.checkDateFormat("MONTH", "2022-W01"),
                "Expected checkDateFormat() to throw, but it didn't");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> DateCheck.checkDateFormat("WEEK", "2022-01"),
                "Expected checkDateFormat() to throw, but it didn't");
    }
}
