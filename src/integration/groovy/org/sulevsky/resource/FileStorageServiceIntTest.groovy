package org.sulevsky.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.sulevsky.BaseIntegrationTest
import org.sulevsky.model.FileDescription

class FileStorageServiceIntTest extends BaseIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate()

    @Autowired
    GridFsTemplate gridFsTemplate

    def "should store file's binary data"() {
        given:
        def fileName = "photo.jpg"
        def resourceToStore = new ClassPathResource(fileName)

        when:
        def response = restTemplate.postForEntity("http://localhost:$port/files/store", multipartRequest(resourceToStore), String.class)

        then:
        response.statusCode == HttpStatus.CREATED
        def files = gridFsTemplate.find(null)
        files.size() == 1
        def stored = new byte[10000]
        def read = new byte[10000]
        files[0].inputStream.read(read) == resourceToStore.inputStream.read(stored)
        read == stored
    }

    def "should respond with description while storing"() {
        given:
        def fileName = "photo.jpg"
        def resourceToStore = new ClassPathResource(fileName)

        when:
        def response = restTemplate.postForObject("http://localhost:$port/files/store", multipartRequest(resourceToStore), FileDescription.class)

        then:
//        response.statusCode == HttpStatus.CREATED
//        def responseBody = response.body
        response["fileMetadata"]["fileName"] == resourceToStore.filename
//        response.fileMetadata.mimeType == resourceToStore.m
//        response.fileMetadata.size == resourceToStore
//        response.fileMetadata.uploadDateTime == resourceToStore.filename
//        response.references == references

    }

    def "should store file's metadata"() {

    }

    def "should not store empty file"() {

    }

    private multipartRequest(Resource resource) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", resource);
        parts
    }
}