package com.pedro.sincronizaReceita.service;

import com.pedro.sincronizaReceita.CSVUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CSVService {
    public static final Logger logger = LogManager.getLogger(CSVService.class);

    public CSVPrinter getCsvPrinter(BufferedWriter writer) throws IOException {
        return new CSVPrinter(writer, CSVFormat.DEFAULT
                .withDelimiter(CSVUtil.DOCUMENT_DELIMITER)
                .withHeader(CSVUtil.getCsvHeaders())
                .withIgnoreHeaderCase()
                .withTrim());
    }

    public CSVParser read(String path) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withDelimiter(CSVUtil.DOCUMENT_DELIMITER)
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            return csvParser;
        } catch (IOException e) {
            logger.error(String.format("Error parsing CSV. path=%s", path));
        }

        return null;
    }

    public void write(CSVPrinter printer, CSVRecord row, boolean syncStatus) {
        try {
            printer.printRecord(CSVUtil.getCsvDataToWrite(row, syncStatus));
            printer.flush();
        } catch (IOException e) {
            logger.error("Error trying to write line to CSV. error=", e.getMessage());
        }
    }
}
