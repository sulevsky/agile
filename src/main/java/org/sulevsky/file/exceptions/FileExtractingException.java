package org.sulevsky.file.exceptions;

public class FileExtractingException extends RuntimeException {
    public FileExtractingException(String message, Throwable e) {
        super(message, e);
    }
}
