package org.sonatype.cs.getmetrics.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import javax.json.Json;

public class NexusIQApiDataServiceTest {
    @Test
    void testGetJsonReaderFromURLConnection() {
        HttpURLConnection mockedUrlConnection = mock(HttpURLConnection.class);
        try {
            when(mockedUrlConnection.getInputStream())
                    .thenReturn(
                            (InputStream)
                                    new ByteArrayInputStream(
                                            "{\"1\": \"2\"}".getBytes(StandardCharsets.UTF_8)));
            String expectedJsonString =
                    Json.createReader(new StringReader("{\"1\": \"2\"}")).readObject().toString();
            Assertions.assertEquals(
                    expectedJsonString,
                    NexusIQApiDataService.getJsonReaderFromURLConnection(mockedUrlConnection)
                            .toString());
        } catch (IOException e) {
            Assertions.fail();
        }
    }
}
