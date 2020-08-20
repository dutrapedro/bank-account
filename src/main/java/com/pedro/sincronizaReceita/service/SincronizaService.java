package com.pedro.sincronizaReceita.service;

import com.pedro.sincronizaReceita.CSVUtil;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;

@Service
public class SincronizaService {
    @Autowired
    CSVService csvService;
    ReceitaService receitaService;

    public static final Logger logger = LogManager.getLogger(SincronizaService.class);

    public SincronizaService() {
        receitaService = new ReceitaService();
    }

    public void process(CSVParser csvParser, BufferedWriter writer) throws IOException {
        CSVPrinter printer = csvService.getCsvPrinter(writer);

        // Usando forEach para não carregar todas as linhas de uma vez e sobrecarregar a memória
        csvParser.forEach(row -> csvService.write(printer, row, sync(row)));
    }

    private boolean sync(CSVRecord row) {
        boolean status = false;

        try {
            status = receitaService.atualizarConta(
                    row.get("agencia"),
                    row.get("conta"),
                    CSVUtil.parseDouble(row.get("saldo")),
                    row.get("status"));
        } catch (InterruptedException e) {
            logger.error("Error trying to update account error=", e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error in ReceitaService. error=",e.getMessage());
        }

        return status;
    }
}
