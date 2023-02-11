package org.sonatype.cs.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.DataExtractService;
import org.sonatype.cs.metrics.service.InsightsAnalysisService;
import org.sonatype.cs.metrics.service.LoaderService;
import org.sonatype.cs.metrics.service.SummaryPdfService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@SpringBootApplication
@EnableJpaRepositories
public class SuccessMetricsApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SuccessMetricsApplication.class);

    private LoaderService loaderService;
    private SummaryPdfService pdfService;
    private InsightsAnalysisService analysisService;
    private DataExtractService dataExtractService;

    private String runMode;
    private String activeProfile;
    private String pdfTemplate;
    private String port;
    private String server_address;
    private String contextPath;

    private boolean doAnalysis = true;
    private boolean successMetricsFileLoaded = false;
    private String timestamp;

    public SuccessMetricsApplication(
            LoaderService loaderService,
            SummaryPdfService pdfService,
            InsightsAnalysisService analysisService,
            DataExtractService dataExtractService,
            @Value("${spring.main.web-application-type}") String runMode,
            @Value("${spring.profiles.active}") String activeProfile,
            @Value("${pdf.htmltemplate}") String pdfTemplate,
            @Value("${server.port}") String port,
            @Value("${server.address:localhost}") String server_address,
            @Value("${server.servlet.context-path:}") String contextPath) {
        this.loaderService = loaderService;
        this.pdfService = pdfService;
        this.analysisService = analysisService;
        this.dataExtractService = dataExtractService;
        this.runMode = runMode;
        this.activeProfile = activeProfile;
        this.pdfTemplate = pdfTemplate;
        this.port = port;
        this.server_address = server_address;
        this.contextPath = contextPath;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SuccessMetricsApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Run mode: {}", runMode);
        log.info("Working directory: {}", System.getProperty("user.dir"));
        log.info("Active profile: {}", activeProfile);

        successMetricsFileLoaded = loaderService.loadAllMetrics(activeProfile);

        if (runMode.contains("SERVLET")) {
            // web app
            this.startUp();
        } else {
            // non-interactive mode
            this.timestamp =
                    DateTimeFormatter.ofPattern("ddMMyy_HHmm")
                            .format(LocalDateTime.now(ZoneId.systemDefault()));
            if (Objects.equals(activeProfile, "data")) {
                createDataFiles();
            } else {
                log.error("unknown profile");
            }
        }
    }

    public boolean isSuccessMetricsFileLoaded() {
        return this.successMetricsFileLoaded;
    }

    private void startUp() {
        log.info(
                "Ready for viewing at http://{}:{}{}",
                server_address,
                port,
                contextPath != null ? contextPath : "");
    }

    public String gettimestamp() {
        return this.timestamp;
    }

    private void createDataFiles() throws IOException, ParseException {
        String html = pdfService.parsePdfTemplate(pdfTemplate, doAnalysis);
        pdfService.generatePdfFromHtml(html, gettimestamp());
        analysisService.writeInsightsAnalysisData(gettimestamp());
        dataExtractService.writeDataExtract(gettimestamp());
    }
}
