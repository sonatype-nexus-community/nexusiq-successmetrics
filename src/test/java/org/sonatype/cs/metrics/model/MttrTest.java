package org.sonatype.cs.metrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MttrTest {
    Mttr mttr;

    @BeforeEach
    void init() {
        mttr = new Mttr("row1", 1f, 2f, 3f, 4f);
    }

    @Test
    void testPointA() {
        assertEquals(1f, mttr.getPointA());
        mttr.setPointA(99f);
        assertEquals(99f, mttr.getPointA());
    }

    @Test
    void testLabel() {
        assertEquals("row1", mttr.getLabel());
        mttr.setLabel("test");
        assertEquals("test", mttr.getLabel());
    }

    @Test
    void testPointB() {
        assertEquals(2f, mttr.getPointB());
        mttr.setPointB(99f);
        assertEquals(99f, mttr.getPointB());
    }

    @Test
    void testPointC() {
        assertEquals(3f, mttr.getPointC());
        mttr.setPointC(99f);
        assertEquals(99f, mttr.getPointC());
    }

    @Test
    void testPointD() {
        assertEquals(4f, mttr.getPointD());
        mttr.setPointD(99f);
        assertEquals(99f, mttr.getPointD());
    }

    @Test
    void testToString() {
        // TODO Should this in fact return Mttr rather than DbRowFloat?
        assertEquals("DbRowFloat [label=row1, pointA=1.0, pointB=2.0, pointC=3.0, pointD=4.0]", mttr.toString());
    }
}