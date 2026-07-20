package com.monkeycode.rmdup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileScanner {

    public List<Path> scan(Path directory) throws IOException {
        List<Path> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.filter(Files::isRegularFile).forEach(file -> {
                try {
                    files.add(file);
                } catch (Exception e) {
                    System.err.println("Warning: cannot access file " + file + " - " + e.getMessage());
                }
            });
        }
        return files;
    }
}
