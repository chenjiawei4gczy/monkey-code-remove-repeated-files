package com.monkeycode.rmdup;

import java.util.Objects;

public final class FileKey {
    private final String fileName;
    private final long fileSize;

    public FileKey(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String fileName() {
        return fileName;
    }

    public long fileSize() {
        return fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileKey)) return false;
        FileKey fileKey = (FileKey) o;
        return fileSize == fileKey.fileSize && Objects.equals(fileName, fileKey.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileSize);
    }

    @Override
    public String toString() {
        return "FileKey[fileName=" + fileName + ", fileSize=" + fileSize + "]";
    }
}
