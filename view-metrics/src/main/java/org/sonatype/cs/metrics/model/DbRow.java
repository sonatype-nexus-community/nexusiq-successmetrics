package org.sonatype.cs.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @Getter @Setter @AllArgsConstructor @NoArgsConstructor class DbRow {
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
