package org.sonatype.cs.metrics.service;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityDataService {
    @Autowired private DbService dbService;

    public Map<String, Object> getSecurityViolations(String tableName) {
        Map<String, Object> model = new HashMap<>();

        List<DbRow> securityViolations =
                dbService.runSql(tableName, SqlStatements.SECURITYVIOLATIONS);
        List<DbRow> discoveredSecurityViolations =
                dbService.runSql(tableName, SqlStatements.DISCOVEREDSECURITYVIOLATIONS);
        List<DbRow> openSecurityViolations =
                dbService.runSql(tableName, SqlStatements.OPENSECURITYVIOLATIONS);
        List<DbRow> fixedSecurityViolations =
                dbService.runSql(tableName, SqlStatements.FIXEDSECURITYVIOLATIONS);
        List<DbRow> waivedSecurityViolations =
                dbService.runSql(tableName, SqlStatements.WAIVEDSECURITYVIOLATIONS);

        model.put("securityViolationsChart", securityViolations);
        model.put("discoveredSecurityViolationsChart", discoveredSecurityViolations);
        model.put("openSecurityViolationsChart", openSecurityViolations);
        model.put("fixedSecurityViolationsChart", fixedSecurityViolations);
        model.put("waivedSecurityViolationsChart", waivedSecurityViolations);

        DbRow discoveredSecurityViolationsTotal =
                dbService.runSql(tableName, SqlStatements.DISCOVEREDSECURITYVIOLATIONSTOTAL).get(0);
        DbRow openSecurityViolationsTotal =
                dbService.runSql(tableName, SqlStatements.OPENSECURITYVIOLATIONSTOTAL).get(0);
        DbRow fixedSecurityViolationsTotal =
                dbService.runSql(tableName, SqlStatements.FIXEDSECURITYVIOLATIONSTOTAL).get(0);
        DbRow waivedSecurityViolationsTotal =
                dbService.runSql(tableName, SqlStatements.WAIVEDSECURITYVIOLATIONSTOTAL).get(0);

        model.put("discoveredSecurityViolationsTotal", discoveredSecurityViolationsTotal);
        model.put("openSecurityViolationsTotal", openSecurityViolationsTotal);
        model.put("fixedSecurityViolationsTotal", fixedSecurityViolationsTotal);
        model.put("waivedSecurityViolationsTotal", waivedSecurityViolationsTotal);

        int discoveredSecurityTotal =
                discoveredSecurityViolationsTotal.getPointA()
                        + discoveredSecurityViolationsTotal.getPointB()
                        + discoveredSecurityViolationsTotal.getPointC();
        int fixedSecurityTotal =
                fixedSecurityViolationsTotal.getPointA()
                        + fixedSecurityViolationsTotal.getPointB()
                        + fixedSecurityViolationsTotal.getPointC();
        int waivedSecurityTotal =
                waivedSecurityViolationsTotal.getPointA()
                        + waivedSecurityViolationsTotal.getPointB()
                        + waivedSecurityViolationsTotal.getPointC();

        model.put("discoveredSecurityTotal", discoveredSecurityTotal);
        model.put("fixedSecurityTotal", fixedSecurityTotal);
        model.put("waivedSecurityTotal", waivedSecurityTotal);

        int discoveredSecurityCriticalTotal = discoveredSecurityViolationsTotal.getPointA();
        int fixedSecurityCriticalTotal = fixedSecurityViolationsTotal.getPointA();
        int waivededSecurityCriticalTotal = waivedSecurityViolationsTotal.getPointA();

        model.put("discoveredSecurityCriticalTotal", discoveredSecurityCriticalTotal);
        model.put("fixedSecurityCriticalTotal", fixedSecurityCriticalTotal);
        model.put("waivededSecurityCriticalTotal", waivededSecurityCriticalTotal);

        return model;
    }
}
