package org.sonatype.cs.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RiskRatio {
    private String label;
    private Double riskRatioValue;
}
