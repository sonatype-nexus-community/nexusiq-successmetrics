package org.sonatype.cs.getmetrics.util;

import org.apache.http.HttpException;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class NexusIqApiConnection {
    private NexusIqApiConnection() {}

    public static HttpURLConnection createAuthorisedUrlConnection(
            String user, String password, String url, String api, String endPoint)
            throws IOException {
        String encodedAuthString = createEncodedAuthString(user, password);
        String urlString = url + api + endPoint;
        return createUrlConnection(urlString, encodedAuthString);
    }

    public static HttpURLConnection createAuthorisedUrlConnection(
            String user, String password, String urlString) throws IOException {
        String encodedAuthString = createEncodedAuthString(user, password);
        return createUrlConnection(urlString, encodedAuthString);
    }

    public static HttpURLConnection createUrlConnection(String urlString, String encodedAuthString)
            throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", encodedAuthString);
        return urlConnection;
    }

    public static String createEncodedAuthString(String user, String password) {
        String authString = user + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(authString.getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + new String(encodedAuth);
    }

    public static HttpURLConnection createAuthorizedPagedUrlConnection(
            String user,
            String password,
            String url,
            String api,
            String endPoint,
            int page,
            int pageSize)
            throws IOException {
        String urlString =
                url + api + endPoint + "?" + "page=" + page + "&pageSize=" + pageSize + "&asc=true";
        String encodedAuthString = createEncodedAuthString(user, password);
        return createUrlConnection(urlString, encodedAuthString);
    }

    public static String retrieveCsvBasedOnPayload(
            String user,
            String password,
            String url,
            String api,
            String endpoint,
            String apiPayload)
            throws IOException, HttpException {
        HttpURLConnection urlConnection =
                prepareHttpURLPostForCSV(user, password, url, api, endpoint);
        return executeHttpURLPostForCSV(apiPayload, urlConnection);
    }

    static String executeHttpURLPostForCSV(String apiPayload, HttpURLConnection urlConnection)
            throws IOException, HttpException {
        try (OutputStream connectionOutputStream = urlConnection.getOutputStream()) {
            byte[] payloadBytes = apiPayload.getBytes();
            connectionOutputStream.write(payloadBytes, 0, payloadBytes.length);
        }
        if (urlConnection.getResponseCode() != 200) {
            throw new HttpException(
                    "Failed with HTTP error code : "
                            + urlConnection.getResponseCode()
                            + " ["
                            + urlConnection.getResponseMessage()
                            + "]");
        }

        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                (InputStream) urlConnection.getContent(), StandardCharsets.UTF_8));
        String response = bufferedReader.lines().collect(Collectors.joining("\n"));
        int statusCode = urlConnection.getResponseCode();
        urlConnection.disconnect();

        if (statusCode != 200) {
            throw new HttpException(
                    "Failed with HTTP error code : "
                            + statusCode
                            + " ["
                            + urlConnection.getResponseMessage()
                            + "]");
        }
        return response;
    }

    static HttpURLConnection prepareHttpURLPostForCSV(
            String user, String password, String url, String api, String endpoint)
            throws IOException, ProtocolException {
        String metricsUrl = url + api + endpoint;
        HttpURLConnection urlConnection = createAuthorisedUrlConnection(user, password, metricsUrl);
        urlConnection.setRequestProperty("Accept", "text/csv");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        return urlConnection;
    }
}
