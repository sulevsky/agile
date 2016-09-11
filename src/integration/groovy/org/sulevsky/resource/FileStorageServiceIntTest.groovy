package org.sulevsky.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.sulevsky.BaseIntegrationTest
import org.sulevsky.model.FileDescription
import org.sulevsky.model.FileMetadata
import org.sulevsky.model.Reference
import org.sulevsky.repository.FileDescriptionRepository

class FileStorageServiceIntTest extends BaseIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate()

    @Autowired
    GridFsTemplate gridFsTemplate

    @Autowired
    FileDescriptionRepository fileDescriptionRepository

    def "should store file's binary data"() {
        given:
        def requestBody = testRequestBody()

        when:
        def response = restTemplate.postForEntity("http://localhost:$port/files/store", requestBody, String.class)

        then:
        response.statusCode == HttpStatus.CREATED
        def files = gridFsTemplate.find(null)
        files.size() == 1
        def stored = new byte[10000]
        def read = new byte[10000]
        files[0].inputStream.read(read) == requestBody.getFirst("file").inputStream.read(stored)
        read == stored
    }

    def "should respond with description while storing"() {
        given:
        def requestBody = testRequestBody()

        when:
        def response = restTemplate.postForObject("http://localhost:$port/files/store", requestBody, Map.class)

        then:
        response["fileMetadata"]["fileName"] == testFileDescription().fileMetadata.fileName
        response["references"].size == testFileDescription().references.size()

    }

    def "should store file's description"() {
        given:
        def requestBody = testRequestBody()

        when:
        restTemplate.postForObject("http://localhost:$port/files/store", requestBody, Map.class)

        then:
        def savedDescription = fileDescriptionRepository.findAll()[0]
        savedDescription.references.size() == testFileDescription().references.size()
        savedDescription.fileMetadata.fileName == testFileDescription().fileMetadata.fileName
    }

    def "should not store empty file"() {
        given:
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>()
        parts.add("file", new byte[0])
        parts.add("fileDescription", testFileDescription())


        when:
        def response = restTemplate.postForEntity("http://localhost:$port/files/store", parts, Map.class)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
        def files = gridFsTemplate.find(null)
        files.size() == 0
    }

    private testRequestBody() {
        def resourceToStore = new ClassPathResource("photo.jpg")
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>()
        parts.add("file", resourceToStore)
        parts.add("fileDescription", testFileDescription())
        parts
    }

    private testFileDescription() {
        def fileMetadata = new FileMetadata("some_int_Id", "some_file_name.jpg", "image/jpeg", 100L, "some_hash")
        def references = [new Reference("ref1", "comment"), new Reference("ref2", "mobile")]
        new FileDescription(fileMetadata, references)
    }
}