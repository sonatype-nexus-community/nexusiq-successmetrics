package org.sonatype.cs.metrics.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import org.sonatype.cs.metrics.repository.QuarantinedComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CsvToRepositoryService {
    @Autowired public QuarantinedComponentRepository quarantinedComponentRepository;
    Logger log = Logger.getLogger(CsvToRepositoryService.class.getName());

    public <T> Boolean load(String path, JpaRepository<T, Long> repository, Class<T> type)
            throws IOException {
        log.info(() -> "Loading file: " + path);
        try {
            List<T> allItems = readFromFile(Files.newBufferedReader(Paths.get(path)), type);
            allItems.forEach(repository::save);
            return repository.count() > 0;
        } catch (IOException e) {
            log.warning(() -> "File not found: " + path);
        }
        return false;
    }

    public <T> List<T> readFromFile(Reader reader, Class<T> type) {
        return new CsvToBeanBuilder<T>(reader)
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withType(type)
                .build()
                .parse();
    }
}
