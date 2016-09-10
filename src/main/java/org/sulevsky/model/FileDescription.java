package org.sulevsky.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class FileDescription {

    private final FileMetadata fileMetadata;
    private final List<Reference> references;

    public FileDescription(FileMetadata fileMetadata,
                           List<Reference> references) {
        this.fileMetadata = fileMetadata;
        this.references = references;
    }

    public FileMetadata getFileMetadata() {
        return fileMetadata;
    }

    public List<Reference> getReferences() {
        return references;
    }
}
