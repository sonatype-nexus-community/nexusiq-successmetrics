package org.sonatype.cs.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.AutoReleasedFromQuarantineComponent;
import org.sonatype.cs.metrics.model.QuarantinedComponent;
import org.sonatype.cs.metrics.repository.AutoReleasedFromQuarantinedComponentRepository;
import org.sonatype.cs.metrics.repository.QuarantinedComponentRepository;
import org.sonatype.cs.metrics.service.FileIoService;
import org.sonatype.cs.metrics.service.LoaderService;
import org.sonatype.cs.metrics.util.DataLoaderParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class FirewallController {
    private static final Logger log = LoggerFactory.getLogger(FirewallController.class);

    private QuarantinedComponentRepository quarantinedComponentRepository;
    private AutoReleasedFromQuarantinedComponentRepository
            autoReleasedFromQuarantineComponentRepository;
    private LoaderService loaderService;
    private String metricsDir;

    public FirewallController(
            LoaderService loaderService,
            QuarantinedComponentRepository quarantinedComponentRepository,
            AutoReleasedFromQuarantinedComponentRepository
                    autoReleasedFromQuarantineComponentRepository,
            @Value("${metrics.dir}") String metricsDir) {
        this.loaderService = loaderService;
        this.quarantinedComponentRepository = quarantinedComponentRepository;
        this.autoReleasedFromQuarantineComponentRepository =
                autoReleasedFromQuarantineComponentRepository;
        this.metricsDir = metricsDir;
    }

    @GetMapping({"/firewall", "/firewall.html"})
    public String firewall(Model model) throws IOException {
        log.info("In FirewallController");

        /* Firewall list reports (loaded at startup) */
        if (loaderService.isQuarantinedComponentsLoaded()) {
            List<QuarantinedComponent> quarantinedComponents =
                    quarantinedComponentRepository.findAll();
            model.addAttribute("quarantinedComponents", quarantinedComponents);
            model.addAttribute("quarantinedComponentsData", !quarantinedComponents.isEmpty());
        }

        if (loaderService.isAutoreleasedFromQuarantineComponentsLoaded()) {
            List<AutoReleasedFromQuarantineComponent> autoReleasedFromQuarantinedComponents =
                    autoReleasedFromQuarantineComponentRepository.findAll();
            model.addAttribute(
                    "autoReleasedFromQuarantinedComponents", autoReleasedFromQuarantinedComponents);

            model.addAttribute(
                    "autoReleasedFromQuarantinedComponentsData",
                    !autoReleasedFromQuarantinedComponents.isEmpty());
        }

        /* Firewall summary reports (read the files in here directly) */
        List<String> quarantinedComponentsSummary = loadFile(DataLoaderParams.QCSDATAFILE);
        List<String> autoReleasedFromQuarantinedComponentsSummary =
                loadFile(DataLoaderParams.AFQCSDATAFILE);

        String[] qcs = quarantinedComponentsSummary.get(1).split(",");
        String[] afqc = autoReleasedFromQuarantinedComponentsSummary.get(1).split(",");

        model.addAttribute("quarantinedComponentsSummary", qcs);
        model.addAttribute("autoReleasedFromQuarantinedComponentsSummary", afqc);

        return "firewall";
    }

    private List<String> loadFile(String filename) throws IOException {

        String filepath =
                Paths.get(System.getProperty("user.dir"))
                        .resolve(Paths.get(metricsDir).resolve(filename))
                        .toString();

        log.info("Loading file: {}", filepath);

        List<String> summary = FileIoService.fileToStringList(filepath);

        log.info("Loaded file: {}", filepath);

        return summary;
    }
}
