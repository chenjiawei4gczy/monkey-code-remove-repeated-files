package com.monkeycode.rmdup;

import java.nio.file.Path;

public sealed interface MoveResult {
    record Success(Path source, Path destination) implements MoveResult {}
    record Failure(Path source, String reason) implements MoveResult {}
}
