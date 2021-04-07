package edu.stanford.bmir.protege.web.shared.search;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public class GetSearchSettingsAction implements ProjectAction<GetSearchSettingsResult> {

    private ProjectId projectId;

    private GetSearchSettingsAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @GwtSerializationConstructor
    private GetSearchSettingsAction() {
    }

    public static GetSearchSettingsAction create(ProjectId projectId) {
        return new GetSearchSettingsAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
