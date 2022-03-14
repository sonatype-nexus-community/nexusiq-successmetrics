package org.sonatype.cs.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @Getter @Setter @AllArgsConstructor @NoArgsConstructor class Mttr {
    private String label;
    private float pointA;
    private float pointB;
    private float pointC;
    private float pointD;

    @Override
    public String toString() {
        return "DbRowFloat [label="
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
