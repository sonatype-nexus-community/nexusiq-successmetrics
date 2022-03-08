package org.sonatype.cs.getmetrics.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sonatype.cs.getmetrics.service.FileIoService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class WaiversTest {
    @Test
    void testDoWaiversForApplication() {
        try {
            JsonArray jsonArray =
                    Json.createReader(
                                    new FileInputStream(
                                            "src/test/java/org/sonatype/cs/getmetrics/resources/componentWaivers.json"))
                            .readObject()
                            .getJsonArray("applicationWaivers");
            List<String[]> data = Waivers.doWaivers("application", jsonArray);
            Assertions.assertEquals(2, data.size());
            Assertions.assertArrayEquals(
                    new String[] {
                        "public_id_1",
                        "develop",
                        "pkg:pypi/flask@2.0.2?extension=tar.gz",
                        "Security-Medium",
                        "7",
                        "\"This has been mitigated in"
                                + " https://github.com/example/application/pull/5\"",
                        "\"2022-01-25T10:55:55.444+0000\"",
                        "\"2022-01-26T10:55:55.444+0000\""
                    },
                    data.get(0));
            Assertions.assertArrayEquals(
                    new String[] {
                        "public_id_1",
                        "source",
                        "pkg:pypi/flask@2.0.2?extension=tar.gz",
                        "Security-Medium",
                        "7",
                        "\"This has been mitigated in"
                                + " https://github.com/example/application/pull/5\"",
                        "\"2022-01-25T10:55:55.444+0000\"",
                        ""
                    },
                    data.get(1));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testDoWaiversForRepository() {
        try {
            JsonArray jsonArray =
                    Json.createReader(
                                    new FileInputStream(
                                            "src/test/java/org/sonatype/cs/getmetrics/resources/componentWaivers.json"))
                            .readObject()
                            .getJsonArray("repositoryWaivers");
            List<String[]> data = Waivers.doWaivers("repository", jsonArray);
            Assertions.assertEquals(1, data.size());
            Assertions.assertArrayEquals(
                    new String[] {
                        "PyPi_proxy",
                        "proxy",
                        "pkg:pypi/django@1.6?extension=whl&qualifier=py2.py3-none-any",
                        "Security-Medium",
                        "7",
                        "",
                        "",
                        ""
                    },
                    data.get(0));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testGetWaiversInformationFromData() {
        try {
            JsonReader jsonReader =
                    Json.createReader(
                            new FileInputStream(
                                    "src/test/java/org/sonatype/cs/getmetrics/resources/componentWaivers.json"));
            String[] header = {
                "applicationName",
                "stage",
                "packageUrl",
                "policyName",
                "threatLevel",
                "comment",
                "createDate",
                "expiryTime"
            };
            List<String[]> data = Waivers.getWaiversInformationFromData(jsonReader);
            Assertions.assertEquals(4, data.size());
            Assertions.assertArrayEquals(header, data.get(0));
            Assertions.assertArrayEquals(
                    new String[] {
                        "public_id_1",
                        "develop",
                        "pkg:pypi/flask@2.0.2?extension=tar.gz",
                        "Security-Medium",
                        "7",
                        "\"This has been mitigated in"
                                + " https://github.com/example/application/pull/5\"",
                        "\"2022-01-25T10:55:55.444+0000\"",
                        "\"2022-01-26T10:55:55.444+0000\""
                    },
                    data.get(1));
            Assertions.assertArrayEquals(
                    new String[] {
                        "public_id_1",
                        "source",
                        "pkg:pypi/flask@2.0.2?extension=tar.gz",
                        "Security-Medium",
                        "7",
                        "\"This has been mitigated in"
                                + " https://github.com/example/application/pull/5\"",
                        "\"2022-01-25T10:55:55.444+0000\"",
                        ""
                    },
                    data.get(2));
            Assertions.assertArrayEquals(
                    new String[] {
                        "PyPi_proxy",
                        "proxy",
                        "pkg:pypi/django@1.6?extension=whl&qualifier=py2.py3-none-any",
                        "Security-Medium",
                        "7",
                        "",
                        "",
                        ""
                    },
                    data.get(3));
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    void testMakeCsvFile() {
        Waivers waivers = new Waivers();
        FileIoService fileIoService = null;
        JsonObject jsonObject = null;
        Assertions.assertThrows(
                NotImplementedException.class,
                () -> waivers.makeCsvFile(fileIoService, jsonObject));
    }
}
