package org.sonatype.cs.getmetrics.service;

import com.opencsv.CSVWriter;

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
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileIoService {
    private static final Logger log = LoggerFactory.getLogger(FileIoService.class);

    private final String metricsDir;

    public FileIoService(@Value("${metricsDir}") String metricsDir) {
        this.metricsDir = metricsDir;
    }

    public void writeCsvFile(String filename, List<String[]> data) {
        String metricsFile = metricsDir + "/" + filename;
        File file = new File(metricsFile);
        boolean isFirstTimeWriting = !(file.exists() && !file.isDirectory());

        try (BufferedWriter writer =
                        Files.newBufferedWriter(
                                Paths.get(metricsFile),
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND);
                CSVWriter csvWriter = new CSVWriter(writer)) {
            if (!isFirstTimeWriting) {
                data.remove(0);
            }
            csvWriter.writeAll(data);
        } catch (IOException e) {
            log.error("Error writing file", e);
            System.exit(1);
        }

        log.info("Created file: {}", file.getAbsolutePath());
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
                new File(metricsDir + File.separator + FilenameInfo.SUCCESS_METRICS_CSV_FILE);
        Files.copy(content, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(content);
        log.info("Created file: {}", outputFile.toPath());

        try (Stream<String> stream = Files.lines(outputFile.toPath())) {
            if (stream.count() <= 1) {
                log.warn(
                        "The file {} contains no data, either there is no data to fetch or the user"
                                + " doesn't have the appropriate permissions to fetch it.",
                        outputFile.toPath());
            }
        } catch (IOException e) {
            log.error("Error writing file", e);
            System.exit(1);
        }
    }
}
