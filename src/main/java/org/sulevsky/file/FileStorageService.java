package org.sulevsky.file;

import com.google.common.io.ByteStreams;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.sulevsky.file.exceptions.FileExtractingException;
import org.sulevsky.model.FileDescription;
import org.sulevsky.model.FileMetadata;
import org.sulevsky.repository.FileRepository;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileStorageService {

    private final FileRepository fileRepository;
    private final DescriptionService descriptionService;

    @Autowired
    public FileStorageService(FileRepository fileRepository,
                              DescriptionService descriptionService) {
        this.fileRepository = fileRepository;
        this.descriptionService = descriptionService;
    }

    public FileDescription storeFile(MultipartFile file, FileDescription fileDescription) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new FileExtractingException("Could not get file from request", e);
        }
        String internalId = fileRepository.storeFile(inputStream, file.getOriginalFilename());
        FileDescription populatedFileDescription = populateDescriptionWithInternalData(file, fileDescription, internalId);
        descriptionService.saveDescription(populatedFileDescription);

        return fileDescription;
    }

    public ResponseEntity<byte[]> getFileById(String id) {
        FileDescription fileDescription = descriptionService.findDescription(id);
        GridFSDBFile gridFSDBFile = fileRepository.findFile(fileDescription.getFileMetadata().getInternalId());
        String contentType = fileDescription.getFileMetadata().getMimeType();

        byte[] binary = null;
        try {
            binary = ByteStreams.toByteArray(gridFSDBFile.getInputStream());
        } catch (IOException e) {
            throw new FileExtractingException("Failed to extract file", e);
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(binary);
    }

    public FileDescription getFileDescriptionById(String id) {
        return descriptionService.findDescription(id);
    }

    private FileDescription populateDescriptionWithInternalData(MultipartFile file, FileDescription fileDescription, String internalId) {

        byte[] binaryData;
        try {
            binaryData = file.getBytes();
        } catch (IOException e) {
            throw new FileExtractingException("Could not get file from request", e);
        }

        FileMetadata fileMetadata = fileDescription.getFileMetadata();
        fileMetadata.setInternalId(internalId);
        fileMetadata.setFileHash(calculateFileHash(binaryData));
        return fileDescription;
    }

    private String calculateFileHash(byte[] file) {
        return DigestUtils.md5DigestAsHex(file);
    }
}
