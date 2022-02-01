package org.sonatype.cs.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class DbRow {
    private String label;
    private int pointA;
    private int pointB;
    private int pointC;
    private int pointD;

    @Override
    public String toString() {
        return "DbRow [label="
                + label
                + ", pointA="
                + pointA
                + ", pointB="
                + pointB
                + ", pointC="
                + pointC
                + ", pointD="
                + pointD
                + "]";
    }
}
