package org.sulevsky.file;

import org.springframework.stereotype.Service;
import org.sulevsky.model.FileDescription;
import org.sulevsky.repository.FileDescriptionRepository;

@Service
public class DescriptionService {
    private final FileDescriptionRepository fileDescriptionRepository;

    public DescriptionService(FileDescriptionRepository fileDescriptionRepository) {
        this.fileDescriptionRepository = fileDescriptionRepository;
    }

    public void saveDescription(FileDescription fileDescription){

    }
    public FileDescription findDescription(String id){
        return null;
    }
}
