package org.sulevsky.repository;

import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileRepository {
    private final GridFsTemplate gridFsTemplate;

    @Autowired
    public FileRepository(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public String storeFile(InputStream inputStream, String fileName) {
        GridFSFile file = gridFsTemplate.store(inputStream, fileName);
        return file.getId().toString();
    }

    public GridFSFile findFile(String id) {
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where(id)));
        return file;//TODO
    }

}
