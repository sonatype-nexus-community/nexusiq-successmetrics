package org.sonatype.cs.metrics.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.DbRowStr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelperServiceTest {
    @Test
    void testCalculateDivision() {
        assertEquals(new String("2.50"), HelperService.calculateDivision(5.0f, 2.0f));
        assertEquals(new String("0"), HelperService.calculateDivision(-5.0f, 2.0f));
        assertEquals(new String("0"), HelperService.calculateDivision(5.0f, -2.0f));
        assertEquals(new String("0"), HelperService.calculateDivision(-5.0f, -2.0f));
    }

    @Test
    void testDataMap() {
        List<DbRowStr> emptyDatalist = new ArrayList<DbRowStr>();
        Map<String, Object> emptyMap = new HashMap<>();
        emptyMap.put("testNumber", 0);
        emptyMap.put("test", false);
        assertEquals(emptyMap, HelperService.dataMap("test", emptyDatalist));

        List<DbRowStr> datalist = new ArrayList<DbRowStr>();
        datalist.add(new DbRowStr("a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "i1", "j1"));
        datalist.add(new DbRowStr("a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "i1", "j1"));
        Map<String, Object> fullMap = new HashMap<>();
        fullMap.put("testData", datalist);
        fullMap.put("testNumber", 2);
        fullMap.put("test", true);
        assertEquals(fullMap, HelperService.dataMap("test", datalist));
    }

    @Test
    void testGetPointsAverage() {
        List<Float> emptyFloatList = new ArrayList<Float>();
        assertEquals(0f, HelperService.getPointsAverage(emptyFloatList));

        List<Float> populatedList = new ArrayList<Float>();
        populatedList.add(5f);
        populatedList.add(12f);
        assertEquals(8.5f, HelperService.getPointsAverage(populatedList));

        populatedList = new ArrayList<Float>();
        populatedList.add(5f);
        populatedList.add(0f);
        populatedList.add(12f);
        assertEquals(8.5f, HelperService.getPointsAverage(populatedList));

        List<Float> emptyList = new ArrayList<Float>();
        assertEquals(0f, HelperService.getPointsAverage(emptyList));
    }

    @Test
    void testGetPointsSumAndAverage() {
        List<DbRow> datalist = new ArrayList<DbRow>();
        datalist.add(new DbRow("row1", 1, 2, 3, 4));
        datalist.add(new DbRow("row2", 5, 6, 7, 8));
        assertArrayEquals(new int[] {6, 3}, HelperService.getPointsSumAndAverage(datalist));

        datalist = new ArrayList<DbRow>();
        datalist.add(new DbRow("row1", 1, 2, 3, 4));
        datalist.add(new DbRow("row2", 0, 0, 0, 0));
        datalist.add(new DbRow("row3", 5, 6, 7, 8));
        assertArrayEquals(new int[] {6, 3}, HelperService.getPointsSumAndAverage(datalist));

        List<DbRow> emptyList = new ArrayList<DbRow>();
        assertArrayEquals(new int[] {0, 0}, HelperService.getPointsSumAndAverage(emptyList));
        datalist = new ArrayList<DbRow>();
        assertArrayEquals(new int[] {0, 0}, HelperService.getPointsSumAndAverage(datalist));
    }

    @Test
    void testConvertDateStrSuccess() {
        assertEquals(1644278400000L, HelperService.convertDateStr("2022-02-08"));
    }

    @Test
    void testConvertDateStrException() {
        Assertions.assertThrows(
                java.time.format.DateTimeParseException.class,
                () -> HelperService.convertDateStr("Bad text"));
    }
}
