package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.sonatype.cs.metrics.service.DbService;
import org.sonatype.cs.metrics.util.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class ApplicationEvaluationsController {
    private static final Logger log =
            LoggerFactory.getLogger(ApplicationEvaluationsController.class);

    @Autowired private DbService dbService;

    @GetMapping({"/evaluations", "/evaluations.html"})
    public String applicationEvaluations(
            Model model, @RequestParam(name = "date", required = false) String comparisonDate) {

        log.info("In ApplicationEvaluationsController");
        if (comparisonDate == null) {
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            comparisonDate = dateObj.format(formatter);
        }

        String ApplicationEvaluationsAge7 =
                String.format(
                        "select application_name as pointA, evaluation_date as pointB, stage as"
                            + " pointC from application_evaluation where"
                            + " parsedatetime(evaluation_date, 'yyyy-MM-dd', 'en') >="
                            + " PARSEDATETIME('%s', 'yyyy-MM-dd') - INTERVAL '7' DAY order by 1",
                        comparisonDate);
        String ApplicationEvaluationsAge30 =
                String.format(
                        "select application_name as pointA, evaluation_date as pointB, stage as"
                            + " pointC from application_evaluation where"
                            + " parsedatetime(evaluation_date, 'yyyy-MM-dd', 'en') >"
                            + " PARSEDATETIME('%s', 'yyyy-MM-dd') - INTERVAL '30' DAY and"
                            + " PARSEDATETIME(evaluation_date, 'yyyy-MM-dd', 'en') <"
                            + " parsedatetime('%s', 'yyyy-MM-dd') - INTERVAL '7' DAY order by 1",
                        comparisonDate, comparisonDate);
        String ApplicationEvaluationsAge60 =
                String.format(
                        "select application_name as pointA, evaluation_date as pointB, stage as"
                            + " pointC from application_evaluation where"
                            + " parsedatetime(evaluation_date, 'yyyy-MM-dd', 'en') >"
                            + " PARSEDATETIME('%s', 'yyyy-MM-dd', 'en') - INTERVAL '90' DAY and"
                            + " PARSEDATETIME(evaluation_date, 'yyyy-MM-dd', 'en') <"
                            + " parsedatetime('%s', 'yyyy-MM-dd', 'en') - INTERVAL '30' DAY order"
                            + " by 1",
                        comparisonDate, comparisonDate);
        String ApplicationEvaluationsAge90 =
                String.format(
                        "select application_name as pointA, evaluation_date as pointB, stage as"
                            + " pointC from application_evaluation where"
                            + " parsedatetime(evaluation_date, 'yyyy-MM-dd', 'en') <="
                            + " PARSEDATETIME('%s', 'yyyy-MM-dd', 'en') - INTERVAL '90' DAY order"
                            + " by 1",
                        comparisonDate);

        List<DbRowStr> age7Data = dbService.runSqlStr(ApplicationEvaluationsAge7);
        List<DbRowStr> age30Data = dbService.runSqlStr(ApplicationEvaluationsAge30);
        List<DbRowStr> age60Data = dbService.runSqlStr(ApplicationEvaluationsAge60);
        List<DbRowStr> age90Data = dbService.runSqlStr(ApplicationEvaluationsAge90);

        Map<String, Object> age7Map = HelperService.dataMap("age7", age7Data);
        Map<String, Object> age30Map = HelperService.dataMap("age30", age30Data);
        Map<String, Object> age60Map = HelperService.dataMap("age60", age60Data);
        Map<String, Object> age90Map = HelperService.dataMap("age90", age90Data);

        model.mergeAttributes(age7Map);
        model.mergeAttributes(age30Map);
        model.mergeAttributes(age60Map);
        model.mergeAttributes(age90Map);

        model.addAttribute("status", true);

        return "applicationEvaluations";
    }
}
