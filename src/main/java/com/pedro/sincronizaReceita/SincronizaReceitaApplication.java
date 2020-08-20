package com.pedro.sincronizaReceita;

import com.pedro.sincronizaReceita.service.CSVService;
import com.pedro.sincronizaReceita.service.SincronizaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class SincronizaReceitaApplication implements CommandLineRunner {

	@Autowired
	private SincronizaService syncService;

	@Autowired
	private CSVService csvService;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SincronizaReceitaApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		BufferedWriter writer = Files.newBufferedWriter(Paths.get("output/processed_file.csv"));
		syncService.process(csvService.read(args[0]), writer);
	}
}
