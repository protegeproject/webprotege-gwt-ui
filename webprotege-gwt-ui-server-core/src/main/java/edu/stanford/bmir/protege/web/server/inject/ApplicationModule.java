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
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {

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
    UserDetailsManager provideUserDetailsManager(UserDetailsManagerImpl impl) {
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
        var address = "http://localhost:8082/api/rpc";
        return JsonRpcEndPoint.get(URI.create(address));
    }

}
