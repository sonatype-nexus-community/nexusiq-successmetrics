package org.sonatype.cs.getmetrics.service;

import org.springframework.stereotype.Service;

import javax.json.JsonObject;
import javax.json.JsonReader;

@Service
public interface CsvFileService {
    public void makeCsvFile(FileIoService f, JsonReader r);

    public void makeCsvFile(FileIoService f, JsonObject o);
}
