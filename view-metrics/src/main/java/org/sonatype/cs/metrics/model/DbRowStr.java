package org.sonatype.cs.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @Getter @Setter @AllArgsConstructor @NoArgsConstructor class DbRowStr {
    private String pointA;
    private String pointB;
    private String pointC;
    private String pointD;
    private String pointE;
    private String pointF;
    private String pointG;
    private String pointH;
    private String pointI;
    private String pointJ;

    @Override
    public String toString() {
        return "DbRowStr [pointA="
                + pointA
                + ", pointB="
                + pointB
                + ", pointC="
                + pointC
                + ", pointD="
                + pointD
                + ", pointE="
                + pointE
                + ", pointF="
                + pointF
                + "]";
    }
}
