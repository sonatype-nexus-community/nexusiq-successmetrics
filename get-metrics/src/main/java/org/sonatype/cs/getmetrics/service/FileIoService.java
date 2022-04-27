package org.sonatype.cs.getmetrics.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.getmetrics.util.FilenameInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Service
public class FileIoService {
    private static final Logger log = LoggerFactory.getLogger(FileIoService.class);

    private String metricsDir;

    public FileIoService(@Value("${metricsDir}") String metricsDir) {
        this.metricsDir = metricsDir;
    }

    public void writeCsvFile(String filename, List<String[]> data) {

        String metricsFile = metricsDir + "/" + filename;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(metricsFile))) {
            for (String[] array : data) {
                writer.write(String.join(",", Arrays.asList(array)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Created file: {}", metricsFile);
    }

    public void initMetricsDir() throws IOException {

        Path metricsDirPath = Paths.get(metricsDir);
        File metricsDirectory = new File(metricsDir);

        if (Files.exists(metricsDirPath) && metricsDirectory.list().length > 0) {
            FileUtils.cleanDirectory(metricsDirectory);
        }

        if (Files.notExists(metricsDirPath)) {
            Files.createDirectory(metricsDirPath);
        }
    }

    public void writeSuccessMetricsFile(InputStream content) throws IOException {
        File outputFile =
                new File(metricsDir + File.separator + FilenameInfo.successMetricsCsvFile);
        java.nio.file.Files.copy(content, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(content);
        log.info("Created file: {}", outputFile.toPath());
    }
}
