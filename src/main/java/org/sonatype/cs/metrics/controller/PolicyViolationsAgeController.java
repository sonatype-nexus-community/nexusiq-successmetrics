package org.sonatype.cs.metrics.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.sonatype.cs.metrics.service.DbService;
import org.sonatype.cs.metrics.util.HelperService;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyViolationsAgeController {
	private static final Logger log = LoggerFactory.getLogger(PolicyViolationsAgeController.class);

    @Autowired
    private DbService dbService;

    @Autowired
    private HelperService helperService;

    @GetMapping({ "/violationsage" })
    public String policyViolationsAge(Model model) {

        log.info("In PolicyViolationsAgeController");

        List<DbRowStr> age7Data =  dbService.runSqlStr(SqlStatements.PolicyViolationsAge7);
		List<DbRowStr> age30Data = dbService.runSqlStr(SqlStatements.PolicyViolationsAge30);
		List<DbRowStr> age60Data =  dbService.runSqlStr(SqlStatements.PolicyViolationsAge60);
        List<DbRowStr> age90Data =  dbService.runSqlStr(SqlStatements.PolicyViolationsAge90);

        Map<String, Object> age7Map = helperService.dataMap("age7", age7Data);
        Map<String, Object> age30Map = helperService.dataMap("age30", age30Data);
        Map<String, Object> age60Map = helperService.dataMap("age60", age60Data);
        Map<String, Object> age90Map = helperService.dataMap("age90", age90Data);

        model.mergeAttributes(age7Map);
        model.mergeAttributes(age30Map);
        model.mergeAttributes(age60Map);
        model.mergeAttributes(age90Map);

        model.addAttribute("status", true);

        return "policyViolationsAge";
    }
}
