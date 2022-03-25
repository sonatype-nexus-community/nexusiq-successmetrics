package org.sonatype.cs.metrics.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataExtractObject {

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "Date")
    private LocalDate date;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "TotalApplicationsOnboard")
    private int totalApplicationsOnboard;

    @CsvBindByPosition(position = 2)
    @CsvBindByName(column = "TotalScans")
    private int totalScans;

    @CsvBindByPosition(position = 3)
    @CsvBindByName(column = "UniqueApplicationsScanned")
    private int totalUniqueApplicationsScaned;

    @CsvBindByPosition(position = 4)
    @CsvBindByName(column = "RiskRatioOverall")
    private Double riskRatio;

    @CsvBindByPosition(position = 5)
    @CsvBindByName(column = "BacklogRateOverall")
    private int backlogRate;

    @CsvBindByPosition(position = 6)
    @CsvBindByName(column = "DiscoveryRateOverall")
    private float discoveryRate;

    @CsvBindByPosition(position = 7)
    @CsvBindByName(column = "TotalDiscovered")
    private int allDiscovered;

    @CsvBindByPosition(position = 8)
    @CsvBindByName(column = "TotalOpen")
    private int allOpen;

    @CsvBindByPosition(position = 9)
    @CsvBindByName(column = "TotalFixed")
    private int allFixed;

    @CsvBindByPosition(position = 10)
    @CsvBindByName(column = "TotalWaived")
    private int allWaived;

    @CsvBindByPosition(position = 11)
    @CsvBindByName(column = "MTTRCritical")
    private int mttrCritical;

    @CsvBindByPosition(position = 12)
    @CsvBindByName(column = "MTTRSevere")
    private int mttrSevere;

    @CsvBindByPosition(position = 13)
    @CsvBindByName(column = "MTTRModerate")
    private int mttrModerate;

    @CsvBindByPosition(position = 14)
    @CsvBindByName(column = "SecurityRiskRatio")
    private Double securityRiskRatio;

    @CsvBindByPosition(position = 15)
    @CsvBindByName(column = "SecurityBacklogRate")
    private int securityBacklogRate;

    @CsvBindByPosition(position = 16)
    @CsvBindByName(column = "SecurityDiscoveryRate")
    private float securityDiscoveryRate;

    @CsvBindByPosition(position = 17)
    @CsvBindByName(column = "SecurityDiscovered")
    private int securityDiscovered;

    @CsvBindByPosition(position = 18)
    @CsvBindByName(column = "SecurityOpen")
    private int securityOpen;

    @CsvBindByPosition(position = 19)
    @CsvBindByName(column = "SecurityFixed")
    private int securityFixed;

    @CsvBindByPosition(position = 20)
    @CsvBindByName(column = "SecurityWaived")
    private int securityWaived;

    @CsvBindByPosition(position = 21)
    @CsvBindByName(column = "LicenseRiskRatio")
    private Double licenseRiskRatio;

    @CsvBindByPosition(position = 22)
    @CsvBindByName(column = "LicenseBacklogRate")
    private int licenseBacklogRate;

    @CsvBindByPosition(position = 23)
    @CsvBindByName(column = "LicenseDiscoveryRate")
    private float licenseDiscoveryRate;

    @CsvBindByPosition(position = 24)
    @CsvBindByName(column = "LicenseDiscovered")
    private int licenseDiscovered;

    @CsvBindByPosition(position = 25)
    @CsvBindByName(column = "LicenseOpen")
    private int licenseOpen;

    @CsvBindByPosition(position = 26)
    @CsvBindByName(column = "LicenseFixed")
    private int licenseFixed;

    @CsvBindByPosition(position = 27)
    @CsvBindByName(column = "LicenseWaived")
    private int licenseWaived;

    @CsvBindByPosition(position = 28)
    @CsvBindByName(column = "SecurityCriticalRiskRatio")
    private Double securityCriticalRiskRatio;

    @CsvBindByPosition(position = 29)
    @CsvBindByName(column = "SecurityCriticalBacklogRate")
    private int securityCriticalBacklogRate;

    @CsvBindByPosition(position = 30)
    @CsvBindByName(column = "SecurityCriticalDiscoveryRate")
    private float securityCriticalDiscoveryRate;

    @CsvBindByPosition(position = 31)
    @CsvBindByName(column = "SecurityCriticalDiscovered")
    private int securityCriticalDiscovered;

    @CsvBindByPosition(position = 32)
    @CsvBindByName(column = "SecurityCriticalOpen")
    private int securityCriticalOpen;

    @CsvBindByPosition(position = 33)
    @CsvBindByName(column = "SecurityCriticalFixed")
    private int securityCriticalFixed;

    @CsvBindByPosition(position = 34)
    @CsvBindByName(column = "SecurityCriticalWaived")
    private int securityCriticalWaived;

    @CsvBindByPosition(position = 35)
    @CsvBindByName(column = "LicenseCriticalRiskRatio")
    private Double licenseCriticalRiskRatio;

    @CsvBindByPosition(position = 36)
    @CsvBindByName(column = "LicenseCriticalBacklogRate")
    private int licenseCriticalBacklogRate;

    @CsvBindByPosition(position = 37)
    @CsvBindByName(column = "LicenseCriticalDiscoveryRate")
    private float licenseCriticalDiscoveryRate;

    @CsvBindByPosition(position = 38)
    @CsvBindByName(column = "LicenseCriticalDiscovered")
    private int licenseCriticalDiscovered;

    @CsvBindByPosition(position = 39)
    @CsvBindByName(column = "LicenseCriticalOpen")
    private int licenseCriticalOpen;

    @CsvBindByPosition(position = 40)
    @CsvBindByName(column = "LicenseCriticalFixed")
    private int licenseCriticalFixed;

    @CsvBindByPosition(position = 41)
    @CsvBindByName(column = "LicenseCriticalWaived")
    private int licenseCriticalWaived;

    @CsvBindByPosition(position = 42)
    @CsvBindByName(column = "SecuritySevereRiskRatio")
    private Double securitySevereRiskRatio;

    @CsvBindByPosition(position = 43)
    @CsvBindByName(column = "SecuritySevereBacklogRate")
    private int securitySevereBacklogRate;

    @CsvBindByPosition(position = 44)
    @CsvBindByName(column = "SecuritySevereDiscoveryRate")
    private float securitySevereDiscoveryRate;

    @CsvBindByPosition(position = 45)
    @CsvBindByName(column = "SecuritySevereDiscovered")
    private int securitySevereDiscovered;

    @CsvBindByPosition(position = 46)
    @CsvBindByName(column = "SecuritySevereOpen")
    private int securitySevereOpen;

    @CsvBindByPosition(position = 47)
    @CsvBindByName(column = "SecuritySevereFixed")
    private int securitySevereFixed;

    @CsvBindByPosition(position = 48)
    @CsvBindByName(column = "SecuritySevereWaived")
    private int securitySevereWaived;

    @CsvBindByPosition(position = 49)
    @CsvBindByName(column = "LicenseSevereRiskRatio")
    private Double licenseSevereRiskRatio;

    @CsvBindByPosition(position = 50)
    @CsvBindByName(column = "LicenseSevereBacklogRate")
    private int licenseSevereBacklogRate;

    @CsvBindByPosition(position = 51)
    @CsvBindByName(column = "LicenseSevereDiscoveryRate")
    private float licenseSevereDiscoveryRate;

    @CsvBindByPosition(position = 52)
    @CsvBindByName(column = "LicenseSevereDiscovered")
    private int licenseSevereDiscovered;

    @CsvBindByPosition(position = 53)
    @CsvBindByName(column = "LicenseSevereOpen")
    private int licenseSevereOpen;

    @CsvBindByPosition(position = 54)
    @CsvBindByName(column = "LicenseSevereFixed")
    private int licenseSevereFixed;

    @CsvBindByPosition(position = 55)
    @CsvBindByName(column = "LicenseSevereWaived")
    private int licenseSevereWaived;

    @CsvBindByPosition(position = 56)
    @CsvBindByName(column = "SecurityModerateRiskRatio")
    private Double securityModerateRiskRatio;

    @CsvBindByPosition(position = 57)
    @CsvBindByName(column = "SecurityModerateBacklogRate")
    private int securityModerateBacklogRate;

    @CsvBindByPosition(position = 58)
    @CsvBindByName(column = "SecurityModerateDiscoveryRate")
    private float securityModerateDiscoveryRate;

    @CsvBindByPosition(position = 59)
    @CsvBindByName(column = "SecurityModerateDiscovered")
    private int securityModerateDiscovered;

    @CsvBindByPosition(position = 60)
    @CsvBindByName(column = "SecurityModerateOpen")
    private int securityModerateOpen;

    @CsvBindByPosition(position = 61)
    @CsvBindByName(column = "SecurityModerateFixed")
    private int securityModerateFixed;

    @CsvBindByPosition(position = 62)
    @CsvBindByName(column = "SecurityModerateWaived")
    private int securityModerateWaived;

    @CsvBindByPosition(position = 63)
    @CsvBindByName(column = "LicenseModerateRiskRatio")
    private Double licenseModerateRiskRatio;

    @CsvBindByPosition(position = 64)
    @CsvBindByName(column = "LicenseModerateBacklogRate")
    private int licenseModerateBacklogRate;

    @CsvBindByPosition(position = 65)
    @CsvBindByName(column = "LicenseModerateDiscoveryRate")
    private float licenseModerateDiscoveryRate;

    @CsvBindByPosition(position = 66)
    @CsvBindByName(column = "LicenseModerateDiscovered")
    private int licenseModerateDiscovered;

    @CsvBindByPosition(position = 67)
    @CsvBindByName(column = "LicenseModerateOpen")
    private int licenseModerateOpen;

    @CsvBindByPosition(position = 68)
    @CsvBindByName(column = "LicenseModerateFixed")
    private int licenseModerateFixed;

    @CsvBindByPosition(position = 69)
    @CsvBindByName(column = "LicenseModerateWaived")
    private int licenseModerateWaived;
}
