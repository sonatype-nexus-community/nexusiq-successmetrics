package org.sonatype.cs.getmetrics.util;

import static org.mockito.Mockito.*;

import org.apache.http.HttpException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URLConnection;

public class NexusApiConnectionTest {
    @Test
    void testCreateEncodedAuthString() {
        Assertions.assertEquals(
                "Basic dXNlcjpwYXNzd29yZA==",
                NexusIqApiConnection.createEncodedAuthString("user", "password"));
    }

    @Test
    void testCreateUrlConnection() {
        try {
            URLConnection urlConnection =
                    NexusIqApiConnection.createUrlConnection(
                            "http://test.com/api/v2/test", "Basic dXNlcjpwYXNzd29yZA==");
            Assertions.assertEquals(
                    "http://test.com/api/v2/test", urlConnection.getURL().toString());
        } catch (IOException e) {
            Assertions.fail();
        }

        Assertions.assertThrows(
                IOException.class,
                () -> {
                    NexusIqApiConnection.createUrlConnection(
                            "http123://test.com/api/v2/test", "Basic dXNlcjpwYXNzd29yZA==");
                });
    }

    @Test
    void testCreateAuthorisedUrlConnection() {
        try {
            URLConnection urlConnection =
                    NexusIqApiConnection.createAuthorisedUrlConnection(
                            "user", "password", "http://test.com", "/api/v2", "/test");
            Assertions.assertEquals(
                    "http://test.com/api/v2/test", urlConnection.getURL().toString());
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void testCreateAuthorizedPagedUrlConnection() {
        try {
            URLConnection urlConnection =
                    NexusIqApiConnection.createAuthorizedPagedUrlConnection(
                            "user", "password", "http://test.com", "/api/v2", "/test", 1, 50);
            Assertions.assertEquals(
                    "http://test.com/api/v2/test?page=1&pageSize=50&asc=true",
                    urlConnection.getURL().toString());
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    void testExecuteHttpURLPostForCSVWithNon200ResponseCode() throws IOException {
        String apiPayload = "{}";
        HttpURLConnection mockedUrlConnection = mock(HttpURLConnection.class);
        when(mockedUrlConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockedUrlConnection.getContent())
                .thenReturn(new ByteArrayInputStream("{'1': '2'}".getBytes()));
        when(mockedUrlConnection.getResponseCode()).thenReturn(400);
        Assertions.assertThrows(
                HttpException.class,
                () -> {
                    NexusIqApiConnection.executeHttpURLPostForCSV(apiPayload, mockedUrlConnection);
                });

        when(mockedUrlConnection.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockedUrlConnection.getContent())
                .thenReturn(new ByteArrayInputStream("{'1': '2'}".getBytes()));
        when(mockedUrlConnection.getResponseCode()).thenReturn(200);
        try {
            Assertions.assertEquals(
                    "{'1': '2'}",
                    NexusIqApiConnection.executeHttpURLPostForCSV(apiPayload, mockedUrlConnection));
        } catch (HttpException e) {
            Assertions.fail();
        }
    }

    @Test
    void testPrepareHttpURLPostForCSV() {
        HttpURLConnection urlConnection;
        try {
            urlConnection =
                    NexusIqApiConnection.prepareHttpURLPostForCSV(
                            "user", "password", "http://test.com", "/api/v2", "/test");
            Assertions.assertEquals(
                    "http://test.com/api/v2/test", urlConnection.getURL().toString());
            Assertions.assertEquals("text/csv", urlConnection.getRequestProperty("Accept"));
            Assertions.assertEquals(
                    "application/json", urlConnection.getRequestProperty("Content-Type"));
            Assertions.assertEquals("POST", urlConnection.getRequestMethod());
        } catch (ProtocolException e) {
            Assertions.fail();
        } catch (IOException e) {
            Assertions.fail();
        }
    }
}
