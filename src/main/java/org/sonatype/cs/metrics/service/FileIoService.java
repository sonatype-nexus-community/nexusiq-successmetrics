package org.sonatype.cs.metrics.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Service
public class FileIoService {
	private static final Logger log = LoggerFactory.getLogger(FileIoService.class);

	@Value("${reports.outputdir}")
	private String outputdir;

	@Value("${data.dir}")
	private String datadir;
	
	@Value("${data.successmetrics}")
	private String successmetricsFile;
	
	public void writeInsightsCsvFile(String csvFilename, List<String[]> csvData, String beforeDateRange, String afterDateRange) throws IOException {
		
		String[] header = { "Measure", beforeDateRange, afterDateRange, "Delta", "Change (%)", "xTimes"};
		
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilename));
	
			writer.write(String.join(",", header));
			writer.newLine();
			
			for (String[] array : csvData) {
				//log.info("- " + Arrays.toString(array));
				writer.write(String.join(",", Arrays.asList(array)));
				writer.newLine();
		    }	
			
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	public void writeSuccessMetricsPdfFile(String pdfFilename, String html) throws IOException {
		OutputStream outputStream = new FileOutputStream(pdfFilename);
		
	    ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocumentFromString(html);
	    renderer.layout();
	    renderer.createPDF(outputStream);
	
	    outputStream.close();
	    
	    return;
	}
	
	
	public String makeFilename(String prefix, String extension) throws IOException {
		LocalDateTime instance = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy_HHmm");
	
	    String filename = prefix + "-" + formatter.format(instance) + "." + extension;
	
	    String reportsdir = datadir + File.separator + outputdir;
	    		
	    Path path = Paths.get(reportsdir);
	
	    if (!Files.exists(path)){
	      Files.createDirectory(path);
	    }
	
	    String filepath = reportsdir + File.separator + filename;

	    return filepath;
	}
	
	public String readJsonAsString(String filename) throws IOException {
		
		String jsonString = null;
		
		if (this.fileExists(filename)) {
			jsonString = new String(Files.readAllBytes(Paths.get(filename)));
		}
		
		return jsonString;
	}
	
	public boolean fileExists(String filename) throws IOException {
		boolean exists = false;
		
		File f = new File(filename);
		
		if (f.exists() && f.length() > 0){
			exists = true;
		}
		else {
			throw new IOException("Failed to read file : " + filename);
		}
		
		return exists;
	}

	public void writeSuccessMetricsFile(InputStream content) throws IOException {
	    File outputFile = new File(datadir + File.separator + successmetricsFile);
	    java.nio.file.Files.copy(content, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    IOUtils.closeQuietly(content);
		return;
	}

	public List<String> readFWCsvFile(String filename) throws IOException{
 		List<String> lines = Files.readAllLines(Paths.get(filename));
		
//		for (String line : lines) {
//			log.info(line);
//		}
		
		return lines;
	}

}
