package org.sonatype.cs.metrics.service;

import org.sonatype.cs.metrics.model.Mttr;
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TotalsDataService {

    private DbService dbService;
    private SecurityDataService securityViolationsDataService;
    private LicenseDataService licenseViolationsDataService;

    public TotalsDataService(
            DbService dbService,
            SecurityDataService securityViolationsDataService,
            LicenseDataService licenseViolationsDataService) {
        this.dbService = dbService;
        this.securityViolationsDataService = securityViolationsDataService;
        this.licenseViolationsDataService = licenseViolationsDataService;
    }

    public Map<String, Object> getSummaryData(String tableName) {
        Map<String, Object> model = new HashMap<>();

        Map<String, Object> secModel =
                securityViolationsDataService.getSecurityViolations(tableName);
        Map<String, Object> licModel = licenseViolationsDataService.getLicenseViolations(tableName);

        int dsct = (int) secModel.get("discoveredSecurityCriticalTotal");
        int fsct = (int) secModel.get("fixedSecurityCriticalTotal");

        int dlct = (int) licModel.get("discoveredLicenseCriticalTotal");
        int flct = (int) licModel.get("fixedLicenseCriticalTotal");

        int discoveredCritical = dsct + dlct;
        int fixedCritical = fsct + flct;

        float backlogReductionRateCritical = 0;

        if (fixedCritical > 0 && discoveredCritical > 0) {
            backlogReductionRateCritical = (((float) fixedCritical / discoveredCritical) * 100);
        }
        model.put("backlogReductionRateCritical", backlogReductionRateCritical);
        model.put("discoveredCritical", discoveredCritical);
        model.put("mttrAvg", this.mttrAvg(tableName));

        return model;
    }

    private String[] mttrAvg(String tableName) {
        List<Float> pointA = new ArrayList<>();
        List<Float> pointB = new ArrayList<>();
        List<Float> pointC = new ArrayList<>();

        List<Mttr> mttrPoints = this.getMttr(tableName);

        for (Mttr dp : mttrPoints) {
            pointA.add(dp.getPointA());
            pointB.add(dp.getPointB());
            pointC.add(dp.getPointC());
        }

        String mttrCriticalAvg =
                String.format("%.0f", (float) HelperService.getPointsAverage(pointA));
        String mttrSevereAvg =
                String.format("%.0f", (float) HelperService.getPointsAverage(pointB));
        String mttrModerateAvg =
                String.format("%.0f", (float) HelperService.getPointsAverage(pointC));

        return new String[] {mttrCriticalAvg, mttrSevereAvg, mttrModerateAvg};
    }

    private List<Mttr> getMttr(String tableName) {

        List<Mttr> mttr = new ArrayList<>();

        List<Mttr> points = dbService.runSqlMttr(tableName, SqlStatements.MTTR2);

        for (Mttr dp : points) {
            Mttr cp = new Mttr();
            cp.setLabel(dp.getLabel());
            cp.setPointA(dp.getPointA() / 86400000);
            cp.setPointB(dp.getPointB() / 86400000);
            cp.setPointC(dp.getPointC() / 86400000);

            mttr.add(cp);
        }

        return mttr;
    }
}
