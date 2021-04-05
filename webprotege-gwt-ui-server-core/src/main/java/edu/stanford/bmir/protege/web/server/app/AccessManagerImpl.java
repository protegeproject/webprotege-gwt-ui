package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.Resource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.shared.access.ActionId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-04
 */
public class AccessManagerImpl implements AccessManager {

    @Inject
    public AccessManagerImpl() {
    }

    @Nonnull
    @Override
    public Set<ActionId> getActionClosure(@Nonnull Subject subject,
                                          @Nonnull Resource resource) {
        return Collections.emptySet();
    }
}
