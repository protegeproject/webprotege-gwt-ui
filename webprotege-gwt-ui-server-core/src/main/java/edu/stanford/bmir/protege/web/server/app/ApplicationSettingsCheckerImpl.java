package edu.stanford.bmir.protege.web.server.app;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-04
 */
public class ApplicationSettingsCheckerImpl implements ApplicationSettingsChecker {

    @Inject
    public ApplicationSettingsCheckerImpl() {
    }

    @Override
    public boolean isProperlyConfigured() {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getConfigurationErrors() {
        return Collections.emptyList();
    }
}
