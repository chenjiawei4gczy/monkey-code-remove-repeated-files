package com.monkeycode.rmdup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DuplicateFinder {
    private final FileScanner scanner = new FileScanner();

    public List<Path> findDuplicates(Path directoryA, FileIndex indexB) throws IOException {
        List<Path> duplicates = new ArrayList<>();
        for (Path file : scanner.scan(directoryA)) {
            try {
                long size = Files.size(file);
                String name = file.getFileName().toString();
                FileKey key = new FileKey(name, size);
                if (indexB.contains(key)) {
                    duplicates.add(file);
                }
            } catch (IOException e) {
                System.err.println("Warning: cannot read file " + file + " - " + e.getMessage());
            }
        }
        return duplicates;
    }
}
