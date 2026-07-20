package com.monkeycode.rmdup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {
    private static final String USAGE = "Usage: java -jar rmdup.jar <directoryA> <directoryB> <directoryC>";

    public static void main(String[] args) {
        Path[] paths = parseArgs(args);
        if (paths == null) {
            System.exit(1);
            return;
        }

        Path pathA = paths[0];
        Path pathB = paths[1];
        Path pathC = paths[2];

        try {
            Files.createDirectories(pathC);
        } catch (IOException e) {
            System.err.println("Error: cannot create directory C: " + pathC + " - " + e.getMessage());
            System.exit(1);
            return;
        }

        FileIndex indexB = new FileIndex();
        try {
            indexB.build(pathB);
        } catch (IOException e) {
            System.err.println("Error: cannot traverse directory B: " + pathB + " - " + e.getMessage());
            System.exit(1);
            return;
        }

        FileScanner scanner = new FileScanner();
        List<Path> filesA;
        try {
            filesA = scanner.scan(pathA);
        } catch (IOException e) {
            System.err.println("Error: cannot traverse directory A: " + pathA + " - " + e.getMessage());
            System.exit(1);
            return;
        }

        DuplicateFinder finder = new DuplicateFinder();
        List<Path> duplicates;
        try {
            duplicates = finder.findDuplicates(pathA, indexB);
        } catch (IOException e) {
            System.err.println("Error: duplicate detection failed - " + e.getMessage());
            System.exit(1);
            return;
        }

        Report report = new Report();
        report.addScanned(filesA.size());

        if (duplicates.isEmpty()) {
            System.out.println("No duplicate files found.");
            report.printSummary();
            System.exit(0);
            return;
        }

        System.out.println("Found " + duplicates.size() + " duplicate file(s). Moving to " + pathC + " ...");
        System.out.println();

        FileMover mover = new FileMover();
        for (Path file : duplicates) {
            MoveResult result = mover.move(file, pathA, pathC);
            if (result instanceof MoveResult.Success s) {
                System.out.println("Moved: " + pathA.relativize(s.source())
                        + " -> " + pathC.relativize(s.destination()));
                report.addMoved(s);
            } else if (result instanceof MoveResult.Failure f) {
                System.err.println("Failed: " + pathA.relativize(f.source()) + " - " + f.reason());
                report.addFailed(f);
            }
        }

        report.printSummary();

        if (!report.getFailed().isEmpty()) {
            System.exit(1);
        }
    }

    private static Path[] parseArgs(String[] args) {
        if (args.length < 3) {
            System.err.println(USAGE);
            return null;
        }
        Path pathA = Paths.get(args[0]).toAbsolutePath().normalize();
        Path pathB = Paths.get(args[1]).toAbsolutePath().normalize();
        Path pathC = Paths.get(args[2]).toAbsolutePath().normalize();

        if (!Files.isDirectory(pathA)) {
            System.err.println("Error: directory A does not exist: " + pathA);
            return null;
        }
        if (!Files.isDirectory(pathB)) {
            System.err.println("Error: directory B does not exist: " + pathB);
            return null;
        }

        return new Path[]{pathA, pathB, pathC};
    }
}
