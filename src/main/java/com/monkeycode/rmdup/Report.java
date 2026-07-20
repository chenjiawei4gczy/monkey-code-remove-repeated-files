package com.monkeycode.rmdup;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private int totalScanned;
    private final List<MoveResult.Success> moved = new ArrayList<>();
    private final List<MoveResult.Failure> failed = new ArrayList<>();

    public void addScanned(int count) {
        this.totalScanned = count;
    }

    public void addMoved(MoveResult.Success result) {
        moved.add(result);
    }

    public void addFailed(MoveResult.Failure result) {
        failed.add(result);
    }

    public List<MoveResult.Failure> getFailed() {
        return failed;
    }

    public void printSummary() {
        System.out.println();
        System.out.println("=== Summary ===");
        System.out.println("Total files scanned in directory A: " + totalScanned);
        System.out.println("Duplicate files found: " + (moved.size() + failed.size()));
        System.out.println("Files successfully moved: " + moved.size());
        System.out.println("Files failed to move: " + failed.size());

        if (!failed.isEmpty()) {
            System.out.println();
            System.out.println("Failed files:");
            for (MoveResult.Failure f : failed) {
                System.out.println("  " + f.source() + " - " + f.reason());
            }
        }
    }
}
