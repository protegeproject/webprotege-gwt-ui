package edu.stanford.bmir.protege.web.server.access;


import edu.stanford.bmir.protege.web.shared.access.ActionId;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Jan 2017
 */
public interface AccessManager {

    /**
     * Gets the action closure for the specified subject and resource pair.
     * @param subject The subject.
     * @param resource The resource.
     * @return A collection of action ids that belong to the role closure of the specified subject and resource pair.
     */
    @Nonnull
    Set<ActionId> getActionClosure(@Nonnull Subject subject,
                                   @Nonnull Resource resource);
}