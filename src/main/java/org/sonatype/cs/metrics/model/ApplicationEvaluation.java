package org.sonatype.cs.metrics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ApplicationEvaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String evaluationDate;
	private String applicationName;
    private String stage;
    
    
}
