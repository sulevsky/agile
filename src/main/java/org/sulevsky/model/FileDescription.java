package org.sulevsky.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileDescription {

    private FileMetadata fileMetadata;
    private List<Reference> references;

    @JsonCreator
    public FileDescription(@JsonProperty("fileMetadata") FileMetadata fileMetadata,
                           @JsonProperty("references") List<Reference> references) {
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
