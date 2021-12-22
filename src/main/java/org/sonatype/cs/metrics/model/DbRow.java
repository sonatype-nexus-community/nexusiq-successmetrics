package org.sonatype.cs.metrics.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class  DbRow {
    private String label;
    private int pointA;
    private int pointB;
    private int pointC;
    private int pointD;   
}
