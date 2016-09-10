package org.sulevsky.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.sulevsky.file.FileStorageService;
import org.sulevsky.file.exceptions.EmptyFileException;
import org.sulevsky.model.FileDescription;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/files")
public class FilesResource {

    private final FileStorageService fileStorageService;

    public FilesResource(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    //        Component interface allows clients to store files by using standard http/web mechanisms preferably POST + multipart/form-data. During storage, file metadata are extracted and persisted alone (like filename, extension, mime type, size). Storing of file results in response with file descriptor - metadata and links.
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileDescription storeFile(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("Empty file");
        }

        return fileStorageService.storeFile(file);
    }

//        Anyone is able to download the file using standard HTTP method for downloads. Clients has ability to force download in browser using standard http headers (e.g image request opens download dialog in browser). Also original attributes are preserved during download as original filename and so on.

    @ResponseBody
    @RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFileById(@PathParam("id") String id) {

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(new byte[2]);
    }

    //        Sometimes is usefull to get description of a file without downloading it. For this use case component interface provides method to describe the file upon given file id. The result structure (File descriptor) will contain metadata and links to do operations above the file.
    @ResponseBody
    @RequestMapping(value = "/files/{id}/description", method = RequestMethod.GET, produces = "application/json")
    public FileDescription getFileDescriptionById(String id) {
        return fileStorageService.getFileDescriptionById(id);
    }
}
