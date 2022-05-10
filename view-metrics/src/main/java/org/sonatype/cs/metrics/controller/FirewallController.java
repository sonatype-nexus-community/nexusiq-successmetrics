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
import org.sonatype.cs.metrics.util.HelperService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

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
            model.addAttribute(
                    "totalQuarantinedComponents",
                    quarantinedComponentRepository.findDistinctDisplayName().size());

            List<String> formats = quarantinedComponentRepository.findDistinctFormat();
            TreeMap<String, List<Long>> formatThreatCounts = getThreatsByFormat(formats);
            model.addAttribute(
                    "quarantinedComponentsFormatVulnerabilityCounts", formatThreatCounts);

            LocalDateTime startDateTime = addEarliestQuarantinedWeekStartDate();
            LocalDateTime endDateTime = addLatestQuarantinedWeekEndDate();
            TreeMap<String, List<Long>> monthlyThreatCounts =
                    getThreatsByMonth(startDateTime, endDateTime);
            model.addAttribute("quarantinedComponentMonthlyThreatCounts", monthlyThreatCounts);
        }

        if (loaderService.isAutoreleasedFromQuarantineComponentsLoaded()) {
            List<AutoReleasedFromQuarantineComponent> autoReleasedFromQuarantinedComponents =
                    autoReleasedFromQuarantineComponentRepository.findAll();
            model.addAttribute(
                    "autoReleasedFromQuarantinedComponents", autoReleasedFromQuarantinedComponents);

            model.addAttribute(
                    "autoReleasedFromQuarantinedComponentsData",
                    !autoReleasedFromQuarantinedComponents.isEmpty());

            model.addAttribute(
                    "autoReleasedFromQuarantineCount",
                    autoReleasedFromQuarantineComponentRepository.findDistinctDisplayName().size());
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

    private TreeMap<String, List<Long>> getThreatsByMonth(
            LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TreeMap<String, List<Long>> monthlyVulnerabilityCounts =
                new TreeMap<>(Collections.reverseOrder());
        LocalDateTime loopDateTime = startDateTime;
        while (loopDateTime.isBefore(endDateTime)) {
            List<Long> threatCounts = new ArrayList<>();
            for (int threat = 0; threat <= 10; threat++) {
                threatCounts.add(
                        quarantinedComponentRepository.countByThreatLevelByQuarantineDateBetween(
                                threat,
                                loopDateTime,
                                loopDateTime
                                        .with(TemporalAdjusters.lastDayOfMonth())
                                        .with(LocalTime.MAX)));
                monthlyVulnerabilityCounts.put(
                        loopDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM")), threatCounts);
            }
            loopDateTime = loopDateTime.plusMonths(1);
        }
        return monthlyVulnerabilityCounts;
    }

    private TreeMap<String, List<Long>> getThreatsByFormat(List<String> formats) {
        TreeMap<String, List<Long>> formatVulnerabilityCounts = new TreeMap<>();
        formats.forEach(
                format -> {
                    List<Long> threatCounts = new ArrayList<>();
                    for (int threat = 0; threat <= 10; threat++) {
                        threatCounts.add(
                                quarantinedComponentRepository.countByFormatAndThreatLevel(
                                        format, threat));
                        formatVulnerabilityCounts.put(format, threatCounts);
                    }
                });
        return formatVulnerabilityCounts;
    }

    private LocalDateTime addLatestQuarantinedWeekEndDate() {
        LocalDateTime lastDateQuarantined =
                quarantinedComponentRepository
                        .findTop1ByOrderByQuarantineDateDesc()
                        .getQuarantineDate();
        LocalDateTime lastDateCleared =
                quarantinedComponentRepository.findTop1ByOrderByDateClearedDesc().getDateCleared();
        LocalDateTime latestDate = HelperService.latestDate(lastDateQuarantined, lastDateCleared);
        return latestDate.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    private LocalDateTime addEarliestQuarantinedWeekStartDate() {
        LocalDateTime earliestDate =
                quarantinedComponentRepository
                        .findTop1ByOrderByQuarantineDateAsc()
                        .getQuarantineDate();
        return earliestDate.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
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
