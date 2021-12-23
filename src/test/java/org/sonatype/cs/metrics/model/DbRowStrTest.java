package org.sonatype.cs.metrics.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbRowStrTest {
    @Test
    void testToString() {
        DbRowStr dbRowStr = new DbRowStr("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M");
        assertEquals("DbRowStr [pointA=A, pointB=B, pointC=C, pointD=D, pointE=E, pointF=F]", dbRowStr.toString()); ;
    }
}
