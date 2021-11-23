package org.sonatype.cs.metrics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.Mttr;
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TotalsDataService {
	
	@Autowired
	private DbService dbService;
	
	@Autowired
	private HelperService helperService;
	
	@Autowired
	private SecurityDataService securityViolationsDataService;
	
	@Autowired
	private LicenseDataService licenseViolationsDataService;

	
	
	public Map<String, Object> getSummaryData(String tableName) {
		Map<String, Object> model = new HashMap<>();
		
		Map<String, Object> secModel = securityViolationsDataService.getSecurityViolations(tableName);
		Map<String, Object> licModel = licenseViolationsDataService.getLicenseViolations(tableName);

//		int dst = (int) secModel.get("discoveredSecurityTotal");
//		int fst = (int) secModel.get("fixedSecurityTotal");
//		int wst = (int) secModel.get("waivedSecurityTotal");
//
//		int dlt = (int) licModel.get("discoveredLicenseTotal");
//		int flt = (int) licModel.get("fixedLicenseTotal");
//		int wlt = (int) licModel.get("waivedLicenseTotal");
//		
//		int fixedWaived = fst + wst + flt + wlt;
//		int discovered = dst + dlt;
//		float fixRate = (((float) (fixedWaived) / discovered) * 100);
//
//		model.put("fixRate", String.format("%.0f", fixRate));
		
		
//		float backlogReductionRate = (((float) (fixedWaived) / discovered));
		
//		model.put("backlogReductionRate", String.format("%.2f", backlogReductionRate));

		int dsct = (int) secModel.get("discoveredSecurityCriticalTotal");
		int fsct = (int) secModel.get("fixedSecurityCriticalTotal");
		int wsct = (int) secModel.get("waivededSecurityCriticalTotal");

		int dlct = (int) licModel.get("discoveredLicenseCriticalTotal");
		int flct = (int) licModel.get("fixedLicenseCriticalTotal");
		int wlct = (int) licModel.get("waivededLicenseCriticalTotal");

		int fixedWaivedCritical = fsct + wsct + flct + wlct;
		int discoveredCritical = dsct + dlct;
		int fixedCritical = fsct + flct;

		float backlogReductionRateCritical = 0;
		
		if (fixedCritical > 0 && discoveredCritical > 0) {
			backlogReductionRateCritical = (((float) (fixedCritical) / discoveredCritical) * 100);
		}
				model.put("backlogReductionRateCritical", backlogReductionRateCritical);
		model.put("discoveredCritical", discoveredCritical);
		model.put("mttrAvg", this.MttrAvg(tableName));

		return model;
	}

	private String[] MttrAvg(String tableName) {
		List<Float> pointA = new ArrayList<>();
		List<Float> pointB = new ArrayList<>();
		List<Float> pointC = new ArrayList<>();


		List<Mttr> mttrPoints = this.getMttr(tableName);

		for (Mttr dp : mttrPoints) {
			pointA.add(dp.getPointA());
			pointB.add(dp.getPointB());
			pointC.add(dp.getPointC());
		}

		String mttrCriticalAvg = String.format("%.0f", helperService.getPointsAverage(pointA));
		String mttrSevereAvg = String.format("%.0f", helperService.getPointsAverage(pointB));
		String mttrModerateAvg = String.format("%.0f", helperService.getPointsAverage(pointC));

		String[] values = new String[] { mttrCriticalAvg, mttrSevereAvg, mttrModerateAvg };
		return values;
	}
	
	private List<Mttr> getMttr(String tableName) {

		List<Mttr> mttr = new ArrayList<Mttr>();

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
	
	private String calculateRiskRatioAnalysis(String tableName) {
		List<Float> pointA = new ArrayList<>();

		List<DbRow> riskRatioAnalysis = dbService.runSql(tableName, SqlStatements.RiskRatioAnalysis);

		for (DbRow dp : riskRatioAnalysis) {
			pointA.add((float)dp.getPointA());
		}

		String rr = String.format("%.2f", helperService.getPointsAverage(pointA));
	
		return rr;
	}
}
