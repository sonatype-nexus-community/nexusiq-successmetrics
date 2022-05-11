package org.sonatype.cs.metrics.model;

import com.opencsv.bean.CsvDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "AUTORELEASED_FROM_QUARANTINED_COMPONENTS")
public class AutoReleasedFromQuarantineComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repository;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSS", writeFormat = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp quarantineDate;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSS", writeFormat = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp dateCleared;

    private String displayName;
    private String format;
    private Boolean quarantined;
    private String policyName;
    private Integer threatLevel;
    private String reason;
}
