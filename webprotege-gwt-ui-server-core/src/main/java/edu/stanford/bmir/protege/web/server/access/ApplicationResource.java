package edu.stanford.bmir.protege.web.server.access;


import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public final class ApplicationResource implements Resource {

    private static final ApplicationResource INSTANCE = new ApplicationResource();

    private ApplicationResource() {

    }

    public static ApplicationResource get() {
        return INSTANCE;
    }

    @Override
    public Optional<ProjectId> getProjectId() {
        return Optional.empty();
    }

    @Override
    public boolean isApplication() {
        return true;
    }

    @Override
    public boolean isProject() {
        return false;
    }

    @Override
    public boolean isProject(ProjectId projectId) {
        return false;
    }

    @Override
    public int hashCode() {
        return 22;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj instanceof ApplicationResource;
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationResource")
                .toString();
    }
}

