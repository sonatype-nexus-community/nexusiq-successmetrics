package org.sonatype.cs.metrics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.opencsv.bean.CsvBindByName;

@Entity
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String applicationId;
	private String applicationPublicId;
	private String applicationName;
	private String organizationName;

	private String timePeriodStart;
	private int evaluationCount;
	
	private Float mttrModerateThreat;
	private Float mttrSevereThreat;
	private Float mttrCriticalThreat;

	private int discoveredCountSecurityModerate;
	private int discoveredCountSecuritySevere;
	private int discoveredCountSecurityCritical;
	private int discoveredCountLicenseModerate;
	private int discoveredCountLicenseSevere;
	private int discoveredCountLicenseCritical;

	private int fixedCountSecurityModerate;
	private int fixedCountSecuritySevere;
	private int fixedCountSecurityCritical;
	private int fixedCountLicenseModerate;
	private int fixedCountLicenseSevere;
	private int fixedCountLicenseCritical;

	private int waivedCountSecurityModerate;
	private int waivedCountSecuritySevere;
	private int waivedCountSecurityCritical;
	private int waivedCountLicenseModerate;	
	private int waivedCountLicenseSevere;
	private int waivedCountLicenseCritical;
	
	private int openCountAtTimePeriodEndSecurityModerate;
	private int openCountAtTimePeriodEndSecuritySevere;
	private int openCountAtTimePeriodEndSecurityCritical;
	private int openCountAtTimePeriodEndLicenseModerate;
	private int openCountAtTimePeriodEndLicenseSevere;
	private int openCountAtTimePeriodEndLicenseCritical;

	@Override
	public String toString() {
		return "Metric [applicationPublicId=" + applicationPublicId + ", evaluationCount=" + evaluationCount
				+ ", organizationName=" + organizationName + ", timePeriodStart=" + timePeriodStart + "]";
	}	

}
