package org.sonatype.cs.metrics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PolicyViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String policyName;
	private String applicationName;
	private String openTime;
	private String component;
    
}
