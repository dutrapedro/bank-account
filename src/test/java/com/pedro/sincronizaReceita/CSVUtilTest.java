package com.pedro.sincronizaReceita;

import com.pedro.sincronizaReceita.service.CSVService;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CSVUtilTest {
    @Test
    public void parseDoubleShouldReturnADoubleValue() {
        String value = "1500,56";

        assertThat(CSVUtil.parseDouble(value)).isInstanceOf(Double.class);
        assertThat(CSVUtil.parseDouble(value)).isEqualTo(1500.56);
    }

    @Test
    public void parseDoubleShouldThrowFormatErrorWhenStringCannotBeParsed() {
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            CSVUtil.parseDouble("10,0,00");
        });

        assertThat(exception.getMessage()).isEqualTo("multiple points");
    }

    @Test
    public void getCsvDataToWriteShouldReturnCorrectData() throws IOException {
        CSVParser parser = new CSVService().read("src/test/resources/sample.csv");
        CSVRecord record = parser.getRecords().get(0);

        String[] content = new String[]{
                "0101",
                "12225-6",
                "100,00",
                "A",
                "false"
        };

        assertThat(CSVUtil.getCsvDataToWrite(record, false)).hasSameElementsAs(Arrays.asList(content));

    }

    @Test
    public void getCsvHeadersShouldReturnCorrectHeaders() {
        String[] headers = new String[]{
                "agencia",
                "conta",
                "saldo",
                "status",
                "sync_status"
        };
        assertThat(CSVUtil.getCsvHeaders()).hasSameElementsAs(Arrays.asList(headers));
    }
}
