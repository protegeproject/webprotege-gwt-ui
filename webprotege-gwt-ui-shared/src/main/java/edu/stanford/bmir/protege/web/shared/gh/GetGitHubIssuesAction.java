package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-18
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.issues.GetGitHubIssues")
public abstract class GetGitHubIssuesAction implements ProjectAction<GetGitHubIssuesResult> {

    @JsonCreator
    public static GetGitHubIssuesAction get(@JsonProperty("projectId") ProjectId projectId,
                                            @JsonProperty("entity") OWLEntity entity) {
        return new AutoValue_GetGitHubIssuesAction(projectId, entity);
    }

    public abstract OWLEntity getEntity();
}
