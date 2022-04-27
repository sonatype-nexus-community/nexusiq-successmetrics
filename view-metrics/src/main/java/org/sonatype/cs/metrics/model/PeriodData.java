package org.sonatype.cs.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PeriodData {
    Date timePeriodStart;
    Double riskRatio;
}
