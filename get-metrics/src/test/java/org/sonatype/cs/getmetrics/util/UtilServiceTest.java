package org.sonatype.cs.getmetrics.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sonatype.cs.getmetrics.service.UtilService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class UtilServiceTest {
    @Test
    void testRemoveLastChar() {
        Assertions.assertEquals("abc", UtilService.removeLastChar("abcd"));
        Assertions.assertEquals(null, UtilService.removeLastChar(""));
        Assertions.assertEquals(null, UtilService.removeLastChar(null));
    }

    void testLastChar() {
        Assertions.assertEquals("c", UtilService.lastChar("abc"));
    }

    @Test
    void testWriteCsvDataToBufferedWriter() {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
        List<String[]> data = new ArrayList<>();
        String[] firstLine = {"first", "second", "third"};
        String[] secondLine = {"fourth", "fifth", "sixth"};

        try {
            UtilService.writeCsvDataToBufferedWriter(data, bufferedWriter);
            bufferedWriter.flush();
        } catch (IOException e) {
            Assertions.fail();
        }
        Assertions.assertEquals("", stringWriter.toString());

        data.add(firstLine);
        data.add(secondLine);
        try {
            UtilService.writeCsvDataToBufferedWriter(data, bufferedWriter);
            bufferedWriter.flush();
        } catch (IOException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(
                "first,second,third\nfourth,fifth,sixth\n", stringWriter.toString());
    }
}
