package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

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
