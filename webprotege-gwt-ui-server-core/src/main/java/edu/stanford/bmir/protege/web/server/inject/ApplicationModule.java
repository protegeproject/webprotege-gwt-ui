package edu.stanford.bmir.protege.web.server.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.app.*;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcEndPoint;
import edu.stanford.bmir.protege.web.server.upload.MinioStorageService;
import edu.stanford.bmir.protege.web.server.upload.StorageService;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import io.minio.MinioClient;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {

    private static final Logger logger = Logger.getLogger(ApplicationModule.class.getName());

    @ApplicationSingleton
    @Provides
    public ObjectMapper provideObjectMapper(ObjectMapperProvider provider) {
        return provider.get();
    }

    @Provides
    public DispatchServiceExecutor provideDispatchServiceExecutor(DispatchServiceExecutorImpl impl) {
        return impl;
    }

    @Provides
    @ApplicationDataFactory
    @ApplicationSingleton
    public OWLEntityProvider provideOWLProvider(@ApplicationDataFactory OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Provides
    ApplicationNameSupplier provideApplicationNameSupplier(ApplicationNameSupplierImpl impl) {
        return impl;
    }

    @Provides
    ApplicationSettingsChecker provideApplicationSettingsChecker(ApplicationSettingsCheckerImpl impl) {
        return impl;
    }

    @Provides
    AccessManager provideAccessManager(AccessManagerImpl impl) {
        return impl;
    }

    @ApplicationSingleton
    @Provides
    HttpClient provideHttpClient() {
        return HttpClient.newBuilder()
                         .version(HttpClient.Version.HTTP_1_1)
                         .followRedirects(HttpClient.Redirect.NORMAL)
                         .connectTimeout(Duration.ofSeconds(20))
                         .build();
    }

    @Provides
    JsonRpcEndPoint provideJsonRpcEndPoint() {
        // API Gateway
        var endPoint = getEndPoint();
        var address =  (endPoint != null ? endPoint : "http://localhost:7777") + "/api/execute";
        return JsonRpcEndPoint.get(URI.create(address));
    }

    private static String getEndPoint() {
        var env = System.getenv("WEBPROTEGE_GWT_API_GATEWAY_END_POINT");
        if(env != null) {
            return env;
        }
        // Try the prior used env var name
        return System.getenv("webprotege.gwt-api-gateway.endPoint" );
    }

    @Provides
    @ApplicationSingleton
    StorageService provideStorageService(MinioStorageService storageService) {
        return storageService;
    }

    @Provides
    @ApplicationSingleton
    MinioClient provideMinioClient() {
        var accessKey = getMinioAccessKey();
        var secretKey = getMinioSecretKey();
        var endPoint = getMinioEndPoint();
        return MinioClient.builder()
                          .credentials(accessKey, secretKey)
                          .endpoint(endPoint)
                          .build();
    }

    private static String getMinioEndPoint() {
        var env = System.getenv("WEBPROTEGE_MINIO_ENDPOINT");
        if(env != null) {
            return env;
        }
        logger.warning("WEBPROTEGE_MINIO_ENDPOINT environment variable is not set. Checking for minio.endPoint system property or using default. Please specify the environment variable.");
        return System.getProperty("minio.endPoint" , "http://localhost:9000" );
    }

    private static String getMinioSecretKey() {
        var env = System.getenv("WEBPROTEGE_MINIO_SECRET_KEY");
        if(env != null) {
            return env;
        }
        logger.warning("WEBPROTEGE_MINIO_SECRET_KEY environment variable is not set. Checking for minio.secretKey system property or using default. Please specify the environment variable.");
        // Legacy support
        return System.getProperty("minio.secretKey" , "webprotege" );
    }

    private static String getMinioAccessKey() {
        var env = System.getenv("WEBPROTEGE_MINIO_ACCESS_KEY");
        if(env != null) {
            return env;
        }
        logger.warning("WEBPROTEGE_MINIO_ACCESS_KEY environment variable is not set. Checking for minio.secretKey system property or using default. Please specify the environment variable.");
        // Legacy support
        return System.getProperty("minio.accessKey" , "webprotege" );
    }

}
