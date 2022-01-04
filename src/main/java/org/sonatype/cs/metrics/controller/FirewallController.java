package org.sonatype.cs.metrics.controller;

import java.io.IOException;
import java.util.List;

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

@Controller
public class FirewallController {
    private static final Logger log = LoggerFactory.getLogger(FirewallController.class);

    @Autowired
    private DbService dbService;

    @Autowired
    private FileIoService fileIoService;

    @Autowired
    private LoaderService loaderService;
    
    @Value("${data.dir}")
	private String dataDir;


    @GetMapping({"/firewall"})
	public String firewall(Model model) throws IOException {
        log.info("In FirewallController");

        /* Firewall list reports (loaded at startup) */
        if (loaderService.quarantinedComponentsLoaded) {
        	List<DbRowStr> quarantinedComponents =  dbService.runSqlStr(SqlStatements.QuarantinedComponents);
            model.addAttribute("quarantinedComponents", quarantinedComponents);
            
            if (quarantinedComponents.size() > 0) {
		        model.addAttribute("quarantinedComponentsData", true);
	        }
	        else {
		        model.addAttribute("quarantinedComponentsData", false);
	        }
        }
        
        if (loaderService.autoreleasedFromQuarantineComponentsLoaded) {
	        List<DbRowStr> autoReleasedFromQuarantinedComponents =  dbService.runSqlStr(SqlStatements.AutoReleasedFromQuarantinedComponents);
	        model.addAttribute("autoReleasedFromQuarantinedComponents", autoReleasedFromQuarantinedComponents);
	        
	        if (autoReleasedFromQuarantinedComponents.size() > 0) {
		        model.addAttribute("autoReleasedFromQuarantinedComponentsData", true);
	        }
	        else {
		        model.addAttribute("autoReleasedFromQuarantinedComponentsData", false);
	        }
        }

        /* Firewall summary reports (read the file in here directly) */
        List<String> quarantinedComponentsSummary = fileIoService.readFWCsvFile(dataDir + "/" + DataLoaderParams.qcsDatafile);
        List<String> autoReleasedFromQuarantinedComponentsSummary = fileIoService.readFWCsvFile(dataDir + "/" + DataLoaderParams.afqsDatafile);
     
        String[] qcs = quarantinedComponentsSummary.get(1).split(",");
        String[] afqc = autoReleasedFromQuarantinedComponentsSummary.get(1).split(",");
        
        model.addAttribute("quarantinedComponentsSummary", qcs);
        model.addAttribute("autoReleasedFromQuarantinedComponentsSummary", afqc);
        

        return "firewall";
    }
}
