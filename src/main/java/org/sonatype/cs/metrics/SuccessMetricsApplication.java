package org.sonatype.cs.metrics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.service.InsightsAnalysisService;
import org.sonatype.cs.metrics.service.LoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SuccessMetricsApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SuccessMetricsApplication.class);
	
	public static boolean successMetricsFileLoaded = false;

	private String timestamp;
//	
	@Value("${spring.main.web-application-type}")
	private String runMode;

	@Value("${spring.profiles.active}")
	private String activeProfile;
	
	@Value("${iq.sm.csvfile}")
	private boolean iqSmCsvfile;
	
	@Value("${iq.sm.period}")
	private String iqSmPeriod;

	@Value("${server.port}")
	private String port;
	
	@Value("${server.servlet.context-path:}")
	private String contextPath;
	
	@Autowired
	private LoaderService loaderService;

	@Autowired
  	private InsightsAnalysisService analysisService;


	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(SuccessMetricsApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Run mode: " + runMode);
		log.info("Working directory: " + System.getProperty("user.dir"));
		log.info("Active profile: " + activeProfile);

		if (iqSmCsvfile) {
			loaderService.createSmDatafile(iqSmPeriod);
		}

		successMetricsFileLoaded = loaderService.loadSuccessMetricsData();

		if (runMode.contains("SERVLET")) {
			// web app
			loaderService.loadReports2();
			this.startUp();
		} 
		else {
			// non-interactive mode
			if (successMetricsFileLoaded) {
				this.timestamp = DateTimeFormatter.ofPattern("ddMMyy_HHmm").format(LocalDateTime.now());

				switch (activeProfile){
					case "pdf":
						System.out.println("Insights PDF output is no longer supported");
						break;
					case "insights":
						analysisService.writeInsightsAnalysisData(this.timestamp);
						break;
					default:
						log.error("unknown profile");
						break;
				}
			}
			else {
				log.error("No data file found");
			}
		}
	}
	
	private void startUp() {

		if (successMetricsFileLoaded){
			log.info("Ready for viewing at http://localhost:{}{}", port, contextPath != null ? contextPath : "");
		}
		else {
			log.error("No data files found");
			System.exit(-1);
		}
	}	

	public String gettimestamp(){
		return this.timestamp;
	}
}
