package edu.stanford.bmir.protege.web.server.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Ticker;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.server.app.ApplicationSettingsManager;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeRecordTranslator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeRecordTranslatorImpl;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.server.download.DownloadGeneratorExecutor;
import edu.stanford.bmir.protege.web.server.download.FileTransferExecutor;
import edu.stanford.bmir.protege.web.server.form.EntityFormRepository;
import edu.stanford.bmir.protege.web.server.form.EntityFormRepositoryImpl;
import edu.stanford.bmir.protege.web.server.form.EntityFormSelectorRepository;
import edu.stanford.bmir.protege.web.server.form.EntityFormSelectorRepositoryImpl;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.mail.*;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.server.owlapi.NonCachingDataFactory;
import edu.stanford.bmir.protege.web.server.project.*;
import edu.stanford.bmir.protege.web.server.upload.*;
import edu.stanford.bmir.protege.web.server.user.*;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    @ApplicationSingleton
    public HasGetUserIdByUserIdOrEmail provideHasGetUserIdByUserIdOrEmail(UserDetailsManager manager) {
        return manager;
    }

    @Provides
    public HasUserIds providesHasUserIds() {
        return Collections::emptySet;
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
    @UploadedOntologiesCacheService
    @ApplicationSingleton
    public ScheduledExecutorService provideUploadedOntologiesCacheService(ApplicationExecutorsRegistry executorsRegistry) {
        var executor = Executors.newSingleThreadScheduledExecutor();
        executorsRegistry.registerService(executor, "Uploaded-Ontologies-Cache-Service");
        return executor;
    }

    @Provides
    public ApplicationSettings provideApplicationSettings(ApplicationSettingsManager manager) {
        return manager.getApplicationSettings();
    }

    @Provides
    LiteralStyle provideDefaultLiteralStyle() {
        return LiteralStyle.REGULAR;
    }

    @Provides
    HttpLinkRenderer provideDefaultHttpLinkRenderer(DefaultHttpLinkRenderer renderer) {
        return renderer;
    }

    @Provides
    LiteralRenderer provideLiteralRenderer(MarkdownLiteralRenderer renderer) {
        return renderer;
    }

    @Provides
    ItemStyleProvider provideItemStyleProvider(DefaultItemStyleProvider provider) {
        return provider;
    }

    @Provides
    NestedAnnotationStyle provideNestedAnnotationStyle() {
        return NestedAnnotationStyle.COMPACT;
    }

    @ApplicationSingleton
    @Provides
    ApplicationDisposablesManager provideApplicationDisposableObjectManager(DisposableObjectManager disposableObjectManager) {
        return new ApplicationDisposablesManager(disposableObjectManager);
    }

    @ApplicationSingleton
    @Provides
    BuiltInPrefixDeclarations provideBuiltInPrefixDeclarations(@Nonnull BuiltInPrefixDeclarationsLoader loader) {
        return loader.getBuiltInPrefixDeclarations();
    }

    @Provides
    @DormantProjectTime
    @ApplicationSingleton
    long providesProjectDormantTime(WebProtegeProperties properties) {
        return properties.getProjectDormantTime();
    }

    @Provides
    Ticker provideTicker() {
        return Ticker.systemTicker();
    }

    @Provides
    @ApplicationSingleton
    UploadedOntologiesCache provideUploadedOntologiesCache(UploadedOntologiesProcessor processor,
                                                           @UploadedOntologiesCacheService ScheduledExecutorService cacheService,
                                                           Ticker ticker,
                                                           ApplicationDisposablesManager disposableObjectManager) {
        var cache = new UploadedOntologiesCache(processor, ticker, cacheService);
        cache.start();
        disposableObjectManager.register(cache);
        return cache;
    }

    @Provides
    DocumentResolver provideDocumentResolver(DocumentResolverImpl impl) {
        return impl;
    }


    @Provides
    OntologyChangeRecordTranslator provideOntologyChangeRecordTranslator(OntologyChangeRecordTranslatorImpl impl) {
        return impl;
    }

    @Provides
    EntityFormRepository provideEntityFormRepository(EntityFormRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    EntityFormSelectorRepository provideFormSelectorRepository(EntityFormSelectorRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }
}
