package org.sulevsky.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.sulevsky.model.FileDescription;

public interface FileDescriptionRepository extends MongoRepository<FileDescription, String> {
}
