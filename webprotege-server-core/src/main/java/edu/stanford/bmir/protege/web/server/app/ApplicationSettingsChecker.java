package edu.stanford.bmir.protege.web.server.app;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 20 Dec 2017
 */
public interface ApplicationSettingsChecker {

    /**
     * Checks to see whether the application settings is properly configured.
     * @return true if the application settings is properly configured, otherwise false.
     */
    boolean isProperlyConfigured();

    /**
     * Gets a list of configuration error messages.
     * @return A list of configuration error messages.  If there are no configuration errors
     * then the list will be empty.
     */
    @Nonnull
    List<String> getConfigurationErrors();
}
