package com.pedro.sincronizaReceita;

import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;

public class CSVUtil {
    public static final char DOCUMENT_DELIMITER = ';';

    public static String[] getCsvDataToWrite(CSVRecord row, boolean syncStatus) {
        return Arrays.stream(getCsvHeaders()).map( header -> {
            return header.equals("sync_status") ? String.valueOf(syncStatus) : row.get(header);
        }).toArray(String[]::new);
    }

    public static String[] getCsvHeaders() {
        return new String[] {
                "agencia",
                "conta",
                "saldo",
                "status",
                "sync_status"
        };
    }

    public static Double parseDouble(String number) {
        return Double.parseDouble(number.replace(",", "."));
    }
}
