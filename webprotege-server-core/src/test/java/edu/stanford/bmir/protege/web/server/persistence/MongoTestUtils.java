package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.server.inject.MongoClientProvider;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class MongoTestUtils {

    private static final String TEST_DB_NAME = "webprotege-test";

    public static MongoClient createMongoClient() {
        return new MongoClientProvider(Optional.of("localhost"),
                                       Optional.of(27017),
                                       Optional.empty(),
                                       Optional.empty(), new ApplicationDisposablesManager(new DisposableObjectManager())).get();
    }

    public static String getTestDbName() {
        return TEST_DB_NAME;
    }
}
