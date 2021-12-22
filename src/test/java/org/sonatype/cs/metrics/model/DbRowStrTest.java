package org.sonatype.cs.metrics.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbRowStrTest {
    DbRowStr dbRowStr;

    @BeforeEach
    void init() {
        dbRowStr = new DbRowStr("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");
    }

    @Test
    void testGetPointA() {
        assertEquals("A", dbRowStr.getPointA());
    }

    @Test
    void testGetPointB() {
        assertEquals("B", dbRowStr.getPointB());
    }

    @Test
    void testGetPointC() {
        assertEquals("C", dbRowStr.getPointC());
    }

    @Test
    void testGetPointD() {
        assertEquals("D", dbRowStr.getPointD());
    }

    @Test
    void testGetPointE() {
        assertEquals("E", dbRowStr.getPointE());
    }

    @Test
    void testGetPointF() {
        assertEquals("F", dbRowStr.getPointF());
    }

    @Test
    void testGetPointG() {
        assertEquals("G", dbRowStr.getPointG());
    }

    @Test
    void testGetPointH() {
        assertEquals("H", dbRowStr.getPointH());
    }

    @Test
    void testGetPointI() {
        assertEquals("I", dbRowStr.getPointI());
    }

    @Test
    void testGetPointJ() {
        assertEquals("J", dbRowStr.getPointJ());
    }

    @Test
    void testGetPointK() {
        assertEquals("K", dbRowStr.getPointK());
    }

    @Test
    void testGetPointL() {
        assertEquals("L", dbRowStr.getPointL());
    }

    @Test
    void testGetPointM() {
        assertEquals("M", dbRowStr.getPointM());
    }

    @Test
    void testSetPointA() {
        dbRowStr.setPointA("text");
        assertEquals("text", dbRowStr.getPointA());
    }

    @Test
    void testSetPointB() {
        dbRowStr.setPointB("text");
        assertEquals("text", dbRowStr.getPointB());
    }

    @Test
    void testSetPointC() {
        dbRowStr.setPointC("text");
        assertEquals("text", dbRowStr.getPointC());
    }

    @Test
    void testSetPointD() {
        dbRowStr.setPointD("text");
        assertEquals("text", dbRowStr.getPointD());
    }

    @Test
    void testSetPointE() {
        dbRowStr.setPointE("text");
        assertEquals("text", dbRowStr.getPointE());
    }

    @Test
    void testSetPointF() {
        dbRowStr.setPointF("text");
        assertEquals("text", dbRowStr.getPointF());
    }

    @Test
    void testSetPointG() {
        dbRowStr.setPointG("text");
        assertEquals("text", dbRowStr.getPointG());
    }

    @Test
    void testSetPointH() {
        dbRowStr.setPointH("text");
        assertEquals("text", dbRowStr.getPointH());
    }

    @Test
    void testSetPointI() {
        dbRowStr.setPointI("text");
        assertEquals("text", dbRowStr.getPointI());
    }

    @Test
    void testSetPointJ() {
        dbRowStr.setPointJ("text");
        assertEquals("text", dbRowStr.getPointJ());
    }

    @Test
    void testSetPointK() {
        dbRowStr.setPointK("text");
        assertEquals("text", dbRowStr.getPointK());
    }

    @Test
    void testSetPointL() {
        dbRowStr.setPointL("text");
        assertEquals("text", dbRowStr.getPointL());
    }

    @Test
    void testSetPointM() {
        dbRowStr.setPointM("text");
        assertEquals("text", dbRowStr.getPointM());
    }

    @Test
    void testToString() {
        assertEquals(
                "DbRowStr(pointA=A, pointB=B, pointC=C, pointD=D, pointE=E, pointF=F, pointG=G, pointH=H, pointI=I, pointJ=J, pointK=K, pointL=L, pointM=M)",
                dbRowStr.toString());
        ;
    }
}
