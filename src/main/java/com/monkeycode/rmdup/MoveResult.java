package com.monkeycode.rmdup;

import java.nio.file.Path;

public interface MoveResult {

    final class Success implements MoveResult {
        private final Path source;
        private final Path destination;

        public Success(Path source, Path destination) {
            this.source = source;
            this.destination = destination;
        }

        public Path source() {
            return source;
        }

        public Path destination() {
            return destination;
        }
    }

    final class Failure implements MoveResult {
        private final Path source;
        private final String reason;

        public Failure(Path source, String reason) {
            this.source = source;
            this.reason = reason;
        }

        public Path source() {
            return source;
        }

        public String reason() {
            return reason;
        }
    }
}
