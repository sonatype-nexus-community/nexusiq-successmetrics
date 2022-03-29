package org.sonatype.cs.metrics.service;

import org.sonatype.cs.metrics.model.DbRow;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.sonatype.cs.metrics.model.Mttr;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbService {

    private JdbcTemplate jtm;

    public DbService(JdbcTemplate jtm) {
        this.jtm = jtm;
    }

    public void runSqlLoad(String stmt) {
        jtm.execute(stmt);
    }

    public List<DbRow> runSql(String tableName, String stmt) {
        stmt = stmt.replace("<?>", tableName);
        return jtm.query(stmt, new BeanPropertyRowMapper<>(DbRow.class));
    }

    public List<DbRowStr> runSqlStr(String stmt) {
        return jtm.query(stmt, new BeanPropertyRowMapper<>(DbRowStr.class));
    }

    public List<Mttr> runSqlMttr(String tableName, String stmt) {
        stmt = stmt.replace("<?>", tableName);
        return jtm.query(stmt, new BeanPropertyRowMapper<>(Mttr.class));
    }
}
