package org.sonatype.cs.getmetrics.service;

import org.apache.http.HttpException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
class NexusIqApiConnectionService {
    NexusIqApiConnectionService() {}

    public HttpURLConnection createAuthorisedUrlConnection(
            String user, String password, String url, String api, String endPoint)
            throws IOException {
        String encodedAuthString = createEncodedAuthString(user, password);
        if ("/".equals(UtilService.lastChar(url))) {
            url = UtilService.removeLastChar(url);
        }
        String urlString = url + api + endPoint;
        return createUrlConnection(urlString, encodedAuthString);
    }

    public HttpURLConnection createAuthorisedUrlConnection(
            String user, String password, String urlString) throws IOException {
        String encodedAuthString = createEncodedAuthString(user, password);
        return createUrlConnection(urlString, encodedAuthString);
    }

    public HttpURLConnection createUrlConnection(String urlString, String encodedAuthString)
            throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", encodedAuthString);
        return urlConnection;
    }

    public String createEncodedAuthString(String user, String password) {
        String authString = user + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(authString.getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + new String(encodedAuth);
    }

    public HttpURLConnection createAuthorizedPagedUrlConnection(
            String user,
            String password,
            String url,
            String api,
            String endPoint,
            int page,
            int pageSize)
            throws IOException {
        if ("/".equals(UtilService.lastChar(url))) {
            url = UtilService.removeLastChar(url);
        }
        String urlString =
                url + api + endPoint + "?" + "page=" + page + "&pageSize=" + pageSize + "&asc=true";
        String encodedAuthString = createEncodedAuthString(user, password);
        return createUrlConnection(urlString, encodedAuthString);
    }

    public String retrieveCsvBasedOnPayload(
            String user,
            String password,
            String url,
            String api,
            String endpoint,
            String apiPayload)
            throws IOException, HttpException {
        if ("/".equals(UtilService.lastChar(url))) {
            url = UtilService.removeLastChar(url);
        }
        HttpURLConnection urlConnection =
                prepareHttpURLPostForCSV(user, password, url, api, endpoint);
        return executeHttpURLPostForCSV(apiPayload, urlConnection);
    }

    public String executeHttpURLPostForCSV(String apiPayload, HttpURLConnection urlConnection)
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

    public String executeHttpURLGetForJson(HttpURLConnection urlConnection)
            throws IOException, HttpException {
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

    public HttpURLConnection prepareHttpURLPostForCSV(
            String user, String password, String url, String api, String endpoint)
            throws IOException {
        String metricsUrl = url + api + endpoint;
        HttpURLConnection urlConnection = createAuthorisedUrlConnection(user, password, metricsUrl);
        urlConnection.setRequestProperty("Accept", "text/csv");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        return urlConnection;
    }

    public HttpURLConnection prepareHttpURLGetForJSON(
            String user, String password, String url, String endpoint) throws IOException {
        String metricsUrl = url + endpoint;
        HttpURLConnection urlConnection = createAuthorisedUrlConnection(user, password, metricsUrl);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        return urlConnection;
    }
}
