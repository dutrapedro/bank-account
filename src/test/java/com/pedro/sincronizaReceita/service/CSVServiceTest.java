package com.pedro.sincronizaReceita.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.pedro.sincronizaReceita.TestUtil;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CSVServiceTest {
    private static CSVService csvService;

    @BeforeAll
    public static void setup() {
        csvService = new CSVService();
    }

    @AfterEach
    public void afterEach() throws IOException {
        TestUtil.cleanFolder("src/test/out");
    }

    @Test
    public void writeShouldWriteACsvRowCorrectly() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/test/out/test.csv"));
        CSVPrinter csvPrinter = csvService.getCsvPrinter(writer);
        CSVParser parser = csvService.read("src/test/resources/sample.csv");
        CSVRecord record = parser.getRecords().get(0);

        csvService.write(csvPrinter, record, false);
        parser = csvService.read("src/test/out/test.csv");
        record = parser.getRecords().get(0);

        assertThat(record.get("agencia")).isEqualTo("0101");
        assertThat(record.get("conta")).isEqualTo("12225-6");
        assertThat(record.get("saldo")).isEqualTo("100,00");
        assertThat(record.get("status")).isEqualTo("A");
        assertThat(record.get("sync_status")).isEqualTo("false");
    }

    @Test
    public void readShouldReturnAListOfCSVRecordsWhenCSVFileExists() throws IOException {
        assertThat(csvService.read("src/test/resources/sample.csv").getRecords().size()).isEqualTo(5);
    }

    @Test
    public void readShouldWriteAnErrorLogWhenExceptionIsThrown() {
        Logger logger = (Logger) LoggerFactory.getLogger(CSVService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        csvService.read("/a/wrong/path/file.csv");

        List<ILoggingEvent> logsList = listAppender.list;

        assertThat(logsList.get(0).getMessage()).isEqualTo("Error parsing CSV. path=/a/wrong/path/file.csv");
        assertThat(logsList.get(0).getLevel()).isEqualTo(Level.ERROR);
    }

    @Test
    public void readShouldReturnAnEmptyArrayWhenExceptionIsThrown() {
        assertThat(csvService.read("/a/wrong/path/file.csv")).isEqualTo(null);
    }
}
