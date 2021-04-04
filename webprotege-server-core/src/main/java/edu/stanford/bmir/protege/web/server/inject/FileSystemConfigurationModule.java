package edu.stanford.bmir.protege.web.server.inject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import dagger.Module;
import dagger.Provides;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class FileSystemConfigurationModule {

    @Provides
    @UploadsDirectory
    public File provideUploadsDirectory(UploadsDirectoryProvider provider) {
        return provider.get();
    }


    @Provides
    public MustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
    }
}

