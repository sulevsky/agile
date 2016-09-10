package org.sulevsky

import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.web.WebAppConfiguration
import org.sulevsky.config.FileStorageConfig
import org.sulevsky.config.MongoDbConfig
import spock.lang.Shared
import spock.lang.Specification


//@SpringBootTest(classes = [Main,FileStorageConfig,MongoDbConfig])

//@SpringApplicationConfiguration(classes = [Main, FileStorageConfig, MongoDbConfig])
@WebIntegrationTest(["server.port=8081", "application.environment=integration"])

@SpringApplicationConfiguration(classes = [Main,FileStorageConfig,MongoDbConfig])
@ActiveProfiles(["integration"])
abstract class BaseIntegrationTest extends Specification {

    @Value('${server.port}')
    int port

    @Shared
    @ClassRule
    MongoRule mongoRule = MongoRule.INSTANCE
}