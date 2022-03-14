package org.sonatype.cs.metrics.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;

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
    private int riskRatio;

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
    private int securityRiskRatio;

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
    private int licenseRiskRatio;

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
    private int securityCriticalRiskRatio;

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
    private int licenseCriticalRiskRatio;

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
    private int securitySevereRiskRatio;

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
    private int licenseSevereRiskRatio;

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
    private int securityModerateRiskRatio;

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
    private int licenseModerateRiskRatio;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTotalApplicationsOnboard() {
        return totalApplicationsOnboard;
    }

    public void setTotalApplicationsOnboard(int totalApplicationsOnboard) {
        this.totalApplicationsOnboard = totalApplicationsOnboard;
    }

    public int getTotalScans() {
        return totalScans;
    }

    public void setTotalScans(int totalScans) {
        this.totalScans = totalScans;
    }

    public int getTotalUniqueApplicationsScaned() {
        return totalUniqueApplicationsScaned;
    }

    public void setTotalUniqueApplicationsScaned(int totalUniqueApplicationsScaned) {
        this.totalUniqueApplicationsScaned = totalUniqueApplicationsScaned;
    }

    public int getRiskRatio() {
        return riskRatio;
    }

    public void setRiskRatio(int riskRatio) {
        this.riskRatio = riskRatio;
    }

    public int getBacklogRate() {
        return backlogRate;
    }

    public void setBacklogRate(int backlogRate) {
        this.backlogRate = backlogRate;
    }

    public float getDiscoveryRate() {
        return discoveryRate;
    }

    public void setDiscoveryRate(float discoveryRate) {
        this.discoveryRate = discoveryRate;
    }

    public int getAllDiscovered() {
        return allDiscovered;
    }

    public void setAllDiscovered(int allDiscovered) {
        this.allDiscovered = allDiscovered;
    }

    public int getAllOpen() {
        return allOpen;
    }

    public void setAllOpen(int allOpen) {
        this.allOpen = allOpen;
    }

    public int getAllFixed() {
        return allFixed;
    }

    public void setAllFixed(int allFixed) {
        this.allFixed = allFixed;
    }

    public int getAllWaived() {
        return allWaived;
    }

    public void setAllWaived(int allWaived) {
        this.allWaived = allWaived;
    }

    public int getMttrCritical() {
        return mttrCritical;
    }

    public void setMttrCritical(int mttrCritical) {
        this.mttrCritical = mttrCritical;
    }

    public int getMttrSevere() {
        return mttrSevere;
    }

    public void setMttrSevere(int mttrSevere) {
        this.mttrSevere = mttrSevere;
    }

    public int getMttrModerate() {
        return mttrModerate;
    }

    public void setMttrModerate(int mttrModerate) {
        this.mttrModerate = mttrModerate;
    }

    public int getSecurityRiskRatio() {
        return securityRiskRatio;
    }

    public void setSecurityRiskRatio(int securityRiskRatio) {
        this.securityRiskRatio = securityRiskRatio;
    }

    public int getSecurityBacklogRate() {
        return securityBacklogRate;
    }

    public void setSecurityBacklogRate(int securityBacklogRate) {
        this.securityBacklogRate = securityBacklogRate;
    }

    public float getSecurityDiscoveryRate() {
        return securityDiscoveryRate;
    }

    public void setSecurityDiscoveryRate(float securityDiscoveryRate) {
        this.securityDiscoveryRate = securityDiscoveryRate;
    }

    public int getSecurityDiscovered() {
        return securityDiscovered;
    }

    public void setSecurityDiscovered(int securityDiscovered) {
        this.securityDiscovered = securityDiscovered;
    }

    public int getSecurityOpen() {
        return securityOpen;
    }

    public void setSecurityOpen(int securityOpen) {
        this.securityOpen = securityOpen;
    }

    public int getSecurityFixed() {
        return securityFixed;
    }

    public void setSecurityFixed(int securityFixed) {
        this.securityFixed = securityFixed;
    }

    public int getSecurityWaived() {
        return securityWaived;
    }

    public void setSecurityWaived(int securityWaived) {
        this.securityWaived = securityWaived;
    }

    public int getLicenseRiskRatio() {
        return licenseRiskRatio;
    }

    public void setLicenseRiskRatio(int licenseRiskRatio) {
        this.licenseRiskRatio = licenseRiskRatio;
    }

    public int getLicenseBacklogRate() {
        return licenseBacklogRate;
    }

    public void setLicenseBacklogRate(int licenseBacklogRate) {
        this.licenseBacklogRate = licenseBacklogRate;
    }

    public float getLicenseDiscoveryRate() {
        return licenseDiscoveryRate;
    }

    public void setLicenseDiscoveryRate(float licenseDiscoveryRate) {
        this.licenseDiscoveryRate = licenseDiscoveryRate;
    }

    public int getLicenseDiscovered() {
        return licenseDiscovered;
    }

    public void setLicenseDiscovered(int licenseDiscovered) {
        this.licenseDiscovered = licenseDiscovered;
    }

    public int getLicenseOpen() {
        return licenseOpen;
    }

    public void setLicenseOpen(int licenseOpen) {
        this.licenseOpen = licenseOpen;
    }

    public int getLicenseFixed() {
        return licenseFixed;
    }

    public void setLicenseFixed(int licenseFixed) {
        this.licenseFixed = licenseFixed;
    }

    public int getLicenseWaived() {
        return licenseWaived;
    }

    public void setLicenseWaived(int licenseWaived) {
        this.licenseWaived = licenseWaived;
    }

    public int getSecurityCriticalRiskRatio() {
        return securityCriticalRiskRatio;
    }

    public void setSecurityCriticalRiskRatio(int securityCriticalRiskRatio) {
        this.securityCriticalRiskRatio = securityCriticalRiskRatio;
    }

    public int getSecurityCriticalBacklogRate() {
        return securityCriticalBacklogRate;
    }

    public void setSecurityCriticalBacklogRate(int securityCriticalBacklogRate) {
        this.securityCriticalBacklogRate = securityCriticalBacklogRate;
    }

    public float getSecurityCriticalDiscoveryRate() {
        return securityCriticalDiscoveryRate;
    }

    public void setSecurityCriticalDiscoveryRate(float securityCriticalDiscoveryRate) {
        this.securityCriticalDiscoveryRate = securityCriticalDiscoveryRate;
    }

    public int getSecurityCriticalDiscovered() {
        return securityCriticalDiscovered;
    }

    public void setSecurityCriticalDiscovered(int securityCriticalDiscovered) {
        this.securityCriticalDiscovered = securityCriticalDiscovered;
    }

    public int getSecurityCriticalOpen() {
        return securityCriticalOpen;
    }

    public void setSecurityCriticalOpen(int securityCriticalOpen) {
        this.securityCriticalOpen = securityCriticalOpen;
    }

    public int getSecurityCriticalFixed() {
        return securityCriticalFixed;
    }

    public void setSecurityCriticalFixed(int securityCriticalFixed) {
        this.securityCriticalFixed = securityCriticalFixed;
    }

    public int getSecurityCriticalWaived() {
        return securityCriticalWaived;
    }

    public void setSecurityCriticalWaived(int securityCriticalWaived) {
        this.securityCriticalWaived = securityCriticalWaived;
    }

    public int getLicenseCriticalRiskRatio() {
        return licenseCriticalRiskRatio;
    }

    public void setLicenseCriticalRiskRatio(int licenseCriticalRiskRatio) {
        this.licenseCriticalRiskRatio = licenseCriticalRiskRatio;
    }

    public int getLicenseCriticalBacklogRate() {
        return licenseCriticalBacklogRate;
    }

    public void setLicenseCriticalBacklogRate(int licenseCriticalBacklogRate) {
        this.licenseCriticalBacklogRate = licenseCriticalBacklogRate;
    }

    public float getLicenseCriticalDiscoveryRate() {
        return licenseCriticalDiscoveryRate;
    }

    public void setLicenseCriticalDiscoveryRate(float licenseCriticalDiscoveryRate) {
        this.licenseCriticalDiscoveryRate = licenseCriticalDiscoveryRate;
    }

    public int getLicenseCriticalDiscovered() {
        return licenseCriticalDiscovered;
    }

    public void setLicenseCriticalDiscovered(int licenseCriticalDiscovered) {
        this.licenseCriticalDiscovered = licenseCriticalDiscovered;
    }

    public int getLicenseCriticalOpen() {
        return licenseCriticalOpen;
    }

    public void setLicenseCriticalOpen(int licenseCriticalOpen) {
        this.licenseCriticalOpen = licenseCriticalOpen;
    }

    public int getLicenseCriticalFixed() {
        return licenseCriticalFixed;
    }

    public void setLicenseCriticalFixed(int licenseCriticalFixed) {
        this.licenseCriticalFixed = licenseCriticalFixed;
    }

    public int getLicenseCriticalWaived() {
        return licenseCriticalWaived;
    }

    public void setLicenseCriticalWaived(int licenseCriticalWaived) {
        this.licenseCriticalWaived = licenseCriticalWaived;
    }

    public int getSecuritySevereRiskRatio() {
        return securitySevereRiskRatio;
    }

    public void setSecuritySevereRiskRatio(int securitySevereRiskRatio) {
        this.securitySevereRiskRatio = securitySevereRiskRatio;
    }

    public int getSecuritySevereBacklogRate() {
        return securitySevereBacklogRate;
    }

    public void setSecuritySevereBacklogRate(int securitySevereBacklogRate) {
        this.securitySevereBacklogRate = securitySevereBacklogRate;
    }

    public float getSecuritySevereDiscoveryRate() {
        return securitySevereDiscoveryRate;
    }

    public void setSecuritySevereDiscoveryRate(float securitySevereDiscoveryRate) {
        this.securitySevereDiscoveryRate = securitySevereDiscoveryRate;
    }

    public int getSecuritySevereDiscovered() {
        return securitySevereDiscovered;
    }

    public void setSecuritySevereDiscovered(int securitySevereDiscovered) {
        this.securitySevereDiscovered = securitySevereDiscovered;
    }

    public int getSecuritySevereOpen() {
        return securitySevereOpen;
    }

    public void setSecuritySevereOpen(int securitySevereOpen) {
        this.securitySevereOpen = securitySevereOpen;
    }

    public int getSecuritySevereFixed() {
        return securitySevereFixed;
    }

    public void setSecuritySevereFixed(int securitySevereFixed) {
        this.securitySevereFixed = securitySevereFixed;
    }

    public int getSecuritySevereWaived() {
        return securitySevereWaived;
    }

    public void setSecuritySevereWaived(int securitySevereWaived) {
        this.securitySevereWaived = securitySevereWaived;
    }

    public int getLicenseSevereRiskRatio() {
        return licenseSevereRiskRatio;
    }

    public void setLicenseSevereRiskRatio(int licenseSevereRiskRatio) {
        this.licenseSevereRiskRatio = licenseSevereRiskRatio;
    }

    public int getLicenseSevereBacklogRate() {
        return licenseSevereBacklogRate;
    }

    public void setLicenseSevereBacklogRate(int licenseSevereBacklogRate) {
        this.licenseSevereBacklogRate = licenseSevereBacklogRate;
    }

    public float getLicenseSevereDiscoveryRate() {
        return licenseSevereDiscoveryRate;
    }

    public void setLicenseSevereDiscoveryRate(float licenseSevereDiscoveryRate) {
        this.licenseSevereDiscoveryRate = licenseSevereDiscoveryRate;
    }

    public int getLicenseSevereDiscovered() {
        return licenseSevereDiscovered;
    }

    public void setLicenseSevereDiscovered(int licenseSevereDiscovered) {
        this.licenseSevereDiscovered = licenseSevereDiscovered;
    }

    public int getLicenseSevereOpen() {
        return licenseSevereOpen;
    }

    public void setLicenseSevereOpen(int licenseSevereOpen) {
        this.licenseSevereOpen = licenseSevereOpen;
    }

    public int getLicenseSevereFixed() {
        return licenseSevereFixed;
    }

    public void setLicenseSevereFixed(int licenseSevereFixed) {
        this.licenseSevereFixed = licenseSevereFixed;
    }

    public int getLicenseSevereWaived() {
        return licenseSevereWaived;
    }

    public void setLicenseSevereWaived(int licenseSevereWaived) {
        this.licenseSevereWaived = licenseSevereWaived;
    }

    public int getSecurityModerateRiskRatio() {
        return securityModerateRiskRatio;
    }

    public void setSecurityModerateRiskRatio(int securityModerateRiskRatio) {
        this.securityModerateRiskRatio = securityModerateRiskRatio;
    }

    public int getSecurityModerateBacklogRate() {
        return securityModerateBacklogRate;
    }

    public void setSecurityModerateBacklogRate(int securityModerateBacklogRate) {
        this.securityModerateBacklogRate = securityModerateBacklogRate;
    }

    public float getSecurityModerateDiscoveryRate() {
        return securityModerateDiscoveryRate;
    }

    public void setSecurityModerateDiscoveryRate(float securityModerateDiscoveryRate) {
        this.securityModerateDiscoveryRate = securityModerateDiscoveryRate;
    }

    public int getSecurityModerateDiscovered() {
        return securityModerateDiscovered;
    }

    public void setSecurityModerateDiscovered(int securityModerateDiscovered) {
        this.securityModerateDiscovered = securityModerateDiscovered;
    }

    public int getSecurityModerateOpen() {
        return securityModerateOpen;
    }

    public void setSecurityModerateOpen(int securityModerateOpen) {
        this.securityModerateOpen = securityModerateOpen;
    }

    public int getSecurityModerateFixed() {
        return securityModerateFixed;
    }

    public void setSecurityModerateFixed(int securityModerateFixed) {
        this.securityModerateFixed = securityModerateFixed;
    }

    public int getSecurityModerateWaived() {
        return securityModerateWaived;
    }

    public void setSecurityModerateWaived(int securityModerateWaived) {
        this.securityModerateWaived = securityModerateWaived;
    }

    public int getLicenseModerateRiskRatio() {
        return licenseModerateRiskRatio;
    }

    public void setLicenseModerateRiskRatio(int licenseModerateRiskRatio) {
        this.licenseModerateRiskRatio = licenseModerateRiskRatio;
    }

    public int getLicenseModerateBacklogRate() {
        return licenseModerateBacklogRate;
    }

    public void setLicenseModerateBacklogRate(int licenseModerateBacklogRate) {
        this.licenseModerateBacklogRate = licenseModerateBacklogRate;
    }

    public float getLicenseModerateDiscoveryRate() {
        return licenseModerateDiscoveryRate;
    }

    public void setLicenseModerateDiscoveryRate(float licenseModerateDiscoveryRate) {
        this.licenseModerateDiscoveryRate = licenseModerateDiscoveryRate;
    }

    public int getLicenseModerateDiscovered() {
        return licenseModerateDiscovered;
    }

    public void setLicenseModerateDiscovered(int licenseModerateDiscovered) {
        this.licenseModerateDiscovered = licenseModerateDiscovered;
    }

    public int getLicenseModerateOpen() {
        return licenseModerateOpen;
    }

    public void setLicenseModerateOpen(int licenseModerateOpen) {
        this.licenseModerateOpen = licenseModerateOpen;
    }

    public int getLicenseModerateFixed() {
        return licenseModerateFixed;
    }

    public void setLicenseModerateFixed(int licenseModerateFixed) {
        this.licenseModerateFixed = licenseModerateFixed;
    }

    public int getLicenseModerateWaived() {
        return licenseModerateWaived;
    }

    public void setLicenseModerateWaived(int licenseModerateWaived) {
        this.licenseModerateWaived = licenseModerateWaived;
    }
}
