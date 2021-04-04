package edu.stanford.bmir.protege.web.server.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Ticker;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.server.app.ApplicationSettingsManager;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.download.DownloadGeneratorExecutor;
import edu.stanford.bmir.protege.web.server.download.FileTransferExecutor;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.mail.*;
import edu.stanford.bmir.protege.web.server.owlapi.NonCachingDataFactory;
import edu.stanford.bmir.protege.web.server.upload.*;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
@Module
public class ApplicationModule {

    private static final int MAX_FILE_DOWNLOAD_THREADS = 5;

    private static final int INDEX_UPDATING_THREADS = 10;


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
    @ApplicationSingleton
    @ApplicationDataFactory
    public OWLDataFactory provideOWLDataFactory() {
        return new NonCachingDataFactory(new OWLDataFactoryImpl());
    }

    @Provides
    @ApplicationDataFactory
    @ApplicationSingleton
    public OWLEntityProvider provideOWLProvider(@ApplicationDataFactory OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Provides
    @ApplicationSingleton
    public WebProtegeProperties provideWebProtegeProperties(WebProtegePropertiesProvider povider) {
        return povider.get();
    }

    @Provides
    public SendMail provideSendMail(SendMailImpl manager) {
        return manager;
    }

    @Provides
    public MessagingExceptionHandler provideMessagingExceptionHandler(MessagingExceptionHandlerImpl handler) {
        return handler;
    }

    @Provides
    @DownloadGeneratorExecutor
    @ApplicationSingleton
    public ExecutorService provideDownloadGeneratorExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        // Might prove to be too much of a bottle neck.  For now, this limits the memory we need
        // to generate downloads
        var executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Generator"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Download-Generator-Service");
        return executor;
    }

    @Provides
    @FileTransferExecutor
    @ApplicationSingleton
    public ExecutorService provideFileTransferExecutorService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newFixedThreadPool(MAX_FILE_DOWNLOAD_THREADS, r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName(thread.getName().replace("thread", "Download-Streamer"));
            return thread;
        });
        executorsRegistry.registerService(executor, "Download-Streaming-Service");
        return executor;
    }

    @Provides
    public ApplicationSettings provideApplicationSettings(ApplicationSettingsManager manager) {
        return manager.getApplicationSettings();
    }

    @ApplicationSingleton
    @Provides
    ApplicationDisposablesManager provideApplicationDisposableObjectManager(DisposableObjectManager disposableObjectManager) {
        return new ApplicationDisposablesManager(disposableObjectManager);
    }

    @Provides
    Ticker provideTicker() {
        return Ticker.systemTicker();
    }

    @Provides
    DocumentResolver provideDocumentResolver(DocumentResolverImpl impl) {
        return impl;
    }

}
