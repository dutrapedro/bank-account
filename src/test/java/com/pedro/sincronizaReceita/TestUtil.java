package com.pedro.sincronizaReceita;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Paths;

public class TestUtil {
    public static void cleanFolder(String path) throws IOException {
        FileUtils.cleanDirectory(Paths.get(path).toFile());
    }
}
