package org.sulevsky.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class FileMetadata {
    private final String internalId;//TODO do not expose
    private final String fileName;
    private final String mimeType;
    private final Long size;
    private final String fileHash;
    private final LocalDateTime uploadDateTime;

//    @JsonCreator
    public FileMetadata(String internalId,
                        @JsonProperty("fileName") String fileName,
                        @JsonProperty("mimeType") String mimeType,
                        @JsonProperty("size") Long size,
                        String fileHash,
                        @JsonProperty("uploadDateTime") LocalDateTime uploadDateTime) {
        this.internalId = internalId;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.fileHash = fileHash;
        this.uploadDateTime = uploadDateTime;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSize() {
        return size;
    }

    public String getFileHash() {
        return fileHash;
    }

    public LocalDateTime getUploadDateTime() {
        return uploadDateTime;
    }

    public static Builder fileMetadata() {
        return new Builder();
    }

    public static class Builder {
        private String internalId;
        private String fileName;
        private String mimeType;
        private Long size;
        private String fileHash;
        private LocalDateTime uploadDateTime;

        public Builder withInternalId(String internalId) {
            this.internalId = internalId;
            return this;
        }

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder withSize(Long size) {
            this.size = size;
            return this;
        }

        public Builder withFileHash(String fileHash) {
            this.fileHash = fileHash;
            return this;
        }

        public Builder withUploadDateTime(LocalDateTime uploadDateTime) {
            this.uploadDateTime = uploadDateTime;
            return this;
        }

        public FileMetadata build() {
            return new FileMetadata(
                    internalId,
                    fileName,
                    mimeType,
                    size,
                    fileHash,
                    uploadDateTime);
        }
    }
}
