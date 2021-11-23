package org.sonatype.cs.metrics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ComponentQuarantine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String repository;
    private String format;
    private String packageUrl;
    private String quarantineTime;
    private String policyName;
    private int threatLevel;
}
