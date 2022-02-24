package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DbRowStr;
import org.sonatype.cs.metrics.service.DbService;
import org.sonatype.cs.metrics.service.FileIoService;
import org.sonatype.cs.metrics.service.LoaderService;
import org.sonatype.cs.metrics.util.DataLoaderParams;
import org.sonatype.cs.metrics.util.SqlStatements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class FirewallController {
    private static final Logger log = LoggerFactory.getLogger(FirewallController.class);

    @Autowired private DbService dbService;

    @Autowired private LoaderService loaderService;

    @Value("${metrics.dir}")
    private String metricsDir;

    @GetMapping({"/firewall", "/firewall.html"})
    public String firewall(Model model) throws IOException {
        log.info("In FirewallController");

        /* Firewall list reports (loaded at startup) */
        if (loaderService.isQuarantinedComponentsLoaded()) {
            List<DbRowStr> quarantinedComponents =
                    dbService.runSqlStr(SqlStatements.QUARANTINEDCOMPONENTS);
            model.addAttribute("quarantinedComponents", quarantinedComponents);
            model.addAttribute("quarantinedComponentsData", quarantinedComponents.isEmpty());
        }

        if (loaderService.isAutoreleasedFromQuarantineComponentsLoaded()) {
            List<DbRowStr> autoReleasedFromQuarantinedComponents =
                    dbService.runSqlStr(SqlStatements.AUTORELEASEDFROMQUARANTINEDCOMPONENTS);
            model.addAttribute(
                    "autoReleasedFromQuarantinedComponents", autoReleasedFromQuarantinedComponents);

            model.addAttribute(
                    "autoReleasedFromQuarantinedComponentsData",
                    autoReleasedFromQuarantinedComponents.isEmpty());
        }

        /* Firewall summary reports (read the file in here directly) */
        List<String> quarantinedComponentsSummary =
                FileIoService.fileToStringList(metricsDir + "/" + DataLoaderParams.QSCOMPDATAFILE);
        List<String> autoReleasedFromQuarantinedComponentsSummary =
                FileIoService.fileToStringList(metricsDir + "/" + DataLoaderParams.AFQSDATAFILE);

        String[] qcs = quarantinedComponentsSummary.get(1).split(",");
        String[] afqc = autoReleasedFromQuarantinedComponentsSummary.get(1).split(",");

        model.addAttribute("quarantinedComponentsSummary", qcs);
        model.addAttribute("autoReleasedFromQuarantinedComponentsSummary", afqc);

        return "firewall";
    }
}
