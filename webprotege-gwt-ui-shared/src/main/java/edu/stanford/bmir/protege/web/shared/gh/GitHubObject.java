package edu.stanford.bmir.protege.web.shared.gh;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-18
 *
 * A marker class for GitHubObjects
 */
public interface GitHubObject {

    long id();

    @Nonnull
    String nodeId();
}
