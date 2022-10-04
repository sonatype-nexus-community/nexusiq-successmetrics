package org.sonatype.cs.metrics.service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.cs.metrics.model.DataExtractObject;
import org.sonatype.cs.metrics.util.CustomBeanToCSVMappingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Service
public class FileIoService {

    private static final Logger log = LoggerFactory.getLogger(FileIoService.class);

    @Value("${reports.outputdir}")
    private String outputdir;

    @Value("${metrics.dir}")
    private String metricsDir;

    @Value("${data.successmetrics}")
    private String successmetricsFile;

    public void writeInsightsCsvFile(
            String csvFilename,
            List<String[]> csvData,
            String beforeDateRange,
            String afterDateRange)
            throws IOException {

        String[] header = {
            "Measure", beforeDateRange, afterDateRange, "Delta", "Change (%)", "xTimes"
        };

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilename))) {
            writer.write(String.join(",", header));
            writer.newLine();

            for (String[] array : csvData) {
                writer.write(String.join(",", Arrays.asList(array)));
                writer.newLine();
            }
        }
    }

    public void writeSuccessMetricsPdfFile(String pdfFilename, String html) throws IOException {
        OutputStream outputStream = new FileOutputStream(pdfFilename);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    public String makeFilename(String prefix, String extension, String timestamp)
            throws IOException {
        String filename = prefix + "-" + timestamp + "." + extension;

        String reportsdir = "." + File.separator + outputdir;

        Path path = Paths.get(reportsdir);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        return reportsdir + File.separator + filename;
    }

    public static String readJsonAsString(String filename) throws IOException {

        String jsonString = null;

        if (!fileExists(filename)) {
            throw new IOException("Failed to find file : " + filename);
        }
        jsonString =
                new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.ISO_8859_1);

        return jsonString;
    }

    public static boolean fileExists(String filename) {
        File f = new File(filename);
        return (f.exists() && f.length() > 0);
    }

    public void writeSuccessMetricsFile(InputStream content) throws IOException {
        File outputFile = new File(outputdir + File.separator + successmetricsFile);
        java.nio.file.Files.copy(content, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(content);
    }

    public void writeDataExtractCsvFile(String csvFilename, List<DataExtractObject> deoList) {

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilename));

            CustomBeanToCSVMappingStrategy<DataExtractObject> mappingStrategy =
                    new CustomBeanToCSVMappingStrategy<>();
            mappingStrategy.setType(DataExtractObject.class);
            StatefulBeanToCsvBuilder<DataExtractObject> builder =
                    new StatefulBeanToCsvBuilder<DataExtractObject>(writer);
            StatefulBeanToCsv<DataExtractObject> beanWriter =
                    builder.withMappingStrategy(mappingStrategy)
                            .withApplyQuotesToAll(false)
                            .build();
            beanWriter.write(deoList);

            writer.close();
        } catch (IOException e) {
            log.info("Data Extract CSV failed with IOException " + e);
        } catch (CsvDataTypeMismatchException e) {
            log.info("Data Extract CSV failed with CsvDataTypeMismatchException " + e);
        } catch (CsvRequiredFieldEmptyException e) {
            log.info("Data Extract CSV failed with CsvRequiredFieldEmptyException " + e);
        }
        return;
    }
}
