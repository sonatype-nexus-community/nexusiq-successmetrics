package org.sonatype.cs.metrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DbRowTest {
    DbRow dbRow;

    @BeforeEach
    void init() {
        dbRow = new DbRow("row1", 1, 2, 3, 4);
    }

    @Test
    void testPointA() {
        assertEquals(1, dbRow.getPointA());
        dbRow.setPointA(99);
        assertEquals(99, dbRow.getPointA());
    }

    @Test
    void testLabel() {
        assertEquals("row1", dbRow.getLabel());
        dbRow.setLabel("test");
        assertEquals("test", dbRow.getLabel());
    }

    @Test
    void testPointB() {
        assertEquals(2, dbRow.getPointB());
        dbRow.setPointB(99);
        assertEquals(99, dbRow.getPointB());
    }

    @Test
    void testPointC() {
        assertEquals(3, dbRow.getPointC());
        dbRow.setPointC(99);
        assertEquals(99, dbRow.getPointC());
    }

    @Test
    void testPointD() {
        assertEquals(4, dbRow.getPointD());
        dbRow.setPointD(99);
        assertEquals(99, dbRow.getPointD());
    }

    @Test
    void testToString() {
        assertEquals("DbRow [label=row1, pointA=1, pointB=2, pointC=3, pointD=4]", dbRow.toString());
    }
}