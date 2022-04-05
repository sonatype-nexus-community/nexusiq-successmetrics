package org.sonatype.cs.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("The H2 console should")
@SpringBootTest(
        properties = {"metrics.dir=src/test/resources/iqmetrics", "spring.profiles.active=web"},
        webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class H2WebConsoleTest {
    @Value("${spring.h2.console.enabled}")
    String h2ConsoleEnabled;

    @Test
    @DisplayName("not be enabled")
    public void testH2ConsoleIsNotEnabled() {
        assertNotEquals("true", h2ConsoleEnabled);
    }

    @Test
    @DisplayName("be disabled")
    public void testH2ConsoleIsDisabled() {
        assertEquals("false", h2ConsoleEnabled);
    }
}
