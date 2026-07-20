package com.monkeycode.rmdup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMover {

    public MoveResult move(Path sourceFile, Path baseA, Path baseC) {
        try {
            Path relativePath = baseA.relativize(sourceFile);
            Path targetPath = baseC.resolve(relativePath);
            Path parentDir = targetPath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            Files.move(sourceFile, targetPath);
            return new MoveResult.Success(sourceFile, targetPath);
        } catch (IOException e) {
            return new MoveResult.Failure(sourceFile, e.getMessage());
        }
    }
}
