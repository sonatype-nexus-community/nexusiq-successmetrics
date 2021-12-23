package org.sonatype.cs.metrics.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MttrTest {
    @Test
    void testToString() {
        Mttr mttr = new Mttr("row1", 1f, 2f, 3f, 4f);
        assertEquals("DbRowFloat [label=row1, pointA=1.0, pointB=2.0, pointC=3.0, pointD=4.0]", mttr.toString());
    }
}