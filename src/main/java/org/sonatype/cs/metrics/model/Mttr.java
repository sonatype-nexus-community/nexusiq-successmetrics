package org.sonatype.cs.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class Mttr {
    private String label;
    private float pointA;
    private float pointB;
    private float pointC;
    private float pointD;
}
