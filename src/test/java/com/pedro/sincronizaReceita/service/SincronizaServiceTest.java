package com.pedro.sincronizaReceita.service;

import com.pedro.sincronizaReceita.TestUtil;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SincronizaServiceTest {
    private static SincronizaService service;

    @BeforeAll
    public static void setup() {
        service = new SincronizaService();
        service.csvService = new CSVService();
    }

    @AfterEach
    public void afterEach() throws IOException {
        TestUtil.cleanFolder("src/test/out");
    }

    @Test
    public void processShouldSyncAccountsAndWriteCSVWithStatus() throws IOException {
        CSVService csvService = new CSVService();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/test/out/test.csv"));
        CSVParser parser = csvService.read("src/test/resources/sample.csv");

        service.process(parser, writer);
        List<CSVRecord> records = csvService
                .read("src/test/out/test.csv")
                .getRecords();

        assertThat(records.size()).isEqualTo(5);
    }
}
