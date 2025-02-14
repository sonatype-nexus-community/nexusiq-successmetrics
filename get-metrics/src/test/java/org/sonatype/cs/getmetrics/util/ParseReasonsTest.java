package org.sonatype.cs.getmetrics.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import javax.json.Json;
import javax.json.JsonArray;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseReasonsTest {
    private ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> listAppender;

    @BeforeEach
    public void setup() {
        // Set up Logback configuration
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        listAppender = new ListAppender<>();
        listAppender.start();
        loggerContext.getLogger("root").addAppender(listAppender);
    }

    @AfterEach
    public void teardown() {
        // Clean up the logger context
        listAppender.stop();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").detachAppender(listAppender);
    }

    @Test
    void testEmptyLicenseString() {
        JsonArray reasons =
                Json.createArrayBuilder().add(Json.createObjectBuilder().add("reason", "")).build();

        String actualReason = ParseReasons.getLicense(reasons);
        Assertions.assertEquals("", actualReason);
    }

    @Test
    void testLicenseStringWithoutBrackets() {
        JsonArray reasons =
                Json.createArrayBuilder()
                        .add(Json.createObjectBuilder().add("reason", "some reason"))
                        .build();
        String actualReason = ParseReasons.getLicense(reasons);

        Assertions.assertEquals(1, listAppender.list.size());
        ch.qos.logback.classic.spi.ILoggingEvent loggingEvent = listAppender.list.get(0);
        assertEquals(
                "licenseFound string does not contain brackets: some reason",
                loggingEvent.getMessage());
        Assertions.assertEquals("", actualReason);
    }

    @Test
    void testSingleLicenseString() {
        JsonArray reasons =
                Json.createArrayBuilder()
                        .add(Json.createObjectBuilder().add("reason", "(license)"))
                        .build();

        String actualReason = ParseReasons.getLicense(reasons);
        Assertions.assertEquals("license", actualReason);
    }

    @Test
    void testMultipltLicenseString() {
        JsonArray reasons =
                Json.createArrayBuilder()
                        .add(Json.createObjectBuilder().add("reason", "(license)"))
                        .add(Json.createObjectBuilder().add("reason", "(license2)"))
                        .build();

        String actualReason = ParseReasons.getLicense(reasons);
        Assertions.assertEquals("license:license2", actualReason);
    }
}
