package org.sonatype.cs.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.DataExtractService;
import org.sonatype.cs.metrics.service.InsightsAnalysisService;
import org.sonatype.cs.metrics.service.LoaderService;
import org.sonatype.cs.metrics.service.SummaryPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class SuccessMetricsApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SuccessMetricsApplication.class);

    private boolean successMetricsFileLoaded = false;

    private String timestamp;

    @Value("${spring.main.web-application-type}")
    private String runMode;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${pdf.htmltemplate}")
    private String pdfTemplate;

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Autowired private LoaderService loaderService;

    @Autowired private SummaryPdfService pdfService;

    @Autowired private InsightsAnalysisService analysisService;

    @Autowired private DataExtractService dataExtractService;

    private boolean doAnalysis = true;

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

        if (isSuccessMetricsFileLoaded()) {
            if (runMode.contains("SERVLET")) {
                // web app
                this.startUp();
            } else {
                // non-interactive mode
                this.timestamp =
                        DateTimeFormatter.ofPattern("ddMMyy_HHmm")
                                .format(LocalDateTime.now(ZoneId.systemDefault()));
                if ("data".equals(activeProfile)) {
                    createDataFiles();
                } else {
                    log.error("unknown profile");
                }
            }
        } else {
            log.error("No data files found");
            System.exit(-1);
        }
    }

    public boolean isSuccessMetricsFileLoaded() {
        return this.successMetricsFileLoaded;
    }

    private void startUp() {
        log.info(
                "Ready for viewing at http://localhost:{}{}",
                port,
                contextPath != null ? contextPath : "");
    }

    public String gettimestamp() {
        return this.timestamp;
    }

    private void createDataFiles() throws IOException, ParseException {
        // case "pdf":
        String html = pdfService.parsePdfTemplate(pdfTemplate, doAnalysis);
        pdfService.generatePdfFromHtml(html, gettimestamp());

        // case "insights":
        analysisService.writeInsightsAnalysisData(gettimestamp());

        // csv data extract
        dataExtractService.writeDataExtract(gettimestamp());
    }
}
