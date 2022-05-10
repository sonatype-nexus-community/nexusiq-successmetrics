package org.sonatype.cs.metrics.model;

import com.opencsv.bean.CsvDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
@Table(name = "QUARANTINED_COMPONENTS")
public class QuarantinedComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repository;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSSX", writeFormat = "yyyy-MM-dd HH:mm")
    private LocalDateTime quarantineDate;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSSX", writeFormat = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateCleared;

    private String displayName;
    private String format;
    private Boolean quarantined;
    private String policyName;
    private Integer threatLevel;
    private String reason;
}
