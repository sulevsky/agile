package org.sulevsky.file;

import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.sulevsky.file.exceptions.FileExtractingException;
import org.sulevsky.model.FileDescription;
import org.sulevsky.model.FileMetadata;
import org.sulevsky.repository.FileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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


    public FileDescription storeFile(MultipartFile file) {

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new FileExtractingException("Could not get file from request", e);
        }
        String internalId = fileRepository.storeFile(inputStream, file.getOriginalFilename());
        FileDescription fileDescription = createDescription(file, internalId);
        descriptionService.saveDescription(fileDescription);

        return fileDescription;
    }

    public GridFSFile getFileById(String id) {
        //TODO combine with description
//        fileDescriptionRepository.findOne(id);
        return fileRepository.findFile(id);
    }

    public FileDescription getFileDescriptionById(String id) {
        return descriptionService.findDescription(id);
    }

    private FileDescription createDescription(MultipartFile file, String internalId) {

        byte[] binaryData;
        try {
            binaryData = file.getBytes();
        } catch (IOException e) {
            throw new FileExtractingException("Could not get file from request", e);
        }
        FileMetadata fileMetadata = FileMetadata
                .fileMetadata()
                .withInternalId(internalId)
                .withFileName(file.getOriginalFilename())
                .withMimeType(file.getContentType())
                .withSize(file.getSize())
                .withFileHash(calculateFileHash(binaryData))
                .build();
        return new FileDescription(fileMetadata, new ArrayList<>());//TODO references
    }

    private String calculateFileHash(byte[] file){
        return DigestUtils.md5DigestAsHex(file);
    }
}
