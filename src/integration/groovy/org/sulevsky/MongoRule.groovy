package org.sulevsky

import com.mongodb.Mongo
import cz.jirutka.spring.embedmongo.EmbeddedMongoBuilder
import de.flapdoodle.embed.mongo.distribution.Version
import org.junit.rules.ExternalResource

public class MongoRule extends ExternalResource {

    public static final MongoRule INSTANCE = new MongoRule(mongoStarter: new MongoStarter())

    private static final int MONGO_PORT = 27018
    private static final String MONGO_HOST = "localhost"

    private MongoStarter mongoStarter

    @Override
    protected void before() throws Throwable {
        mongoStarter.start()
    }

    public static class MongoStarter implements AutoCloseable {

        private Mongo mongo

        public synchronized void start() {

            if (!isMongoStarted()) {
                mongo = new EmbeddedMongoBuilder()
                        .version(Version.V3_3_1)
                        .bindIp(MONGO_HOST)
                        .port(MONGO_PORT)
                        .build()
                mongo.getDB("test").addUser("test", "test".toCharArray())
            }
        }

        private boolean isMongoStarted() {
            mongo != null
        }

        @Override
        public synchronized void close() {
            mongo.close()
        }
    }
}
