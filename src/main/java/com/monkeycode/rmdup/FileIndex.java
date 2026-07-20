package com.monkeycode.rmdup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileIndex {
    private final Map<FileKey, Path> index = new HashMap<>();
    private final FileScanner scanner = new FileScanner();

    public void build(Path directoryB) throws IOException {
        for (Path file : scanner.scan(directoryB)) {
            try {
                long size = Files.size(file);
                String name = file.getFileName().toString();
                index.put(new FileKey(name, size), file);
            } catch (IOException e) {
                System.err.println("Warning: cannot read file " + file + " - " + e.getMessage());
            }
        }
    }

    public boolean contains(FileKey key) {
        return index.containsKey(key);
    }
}
