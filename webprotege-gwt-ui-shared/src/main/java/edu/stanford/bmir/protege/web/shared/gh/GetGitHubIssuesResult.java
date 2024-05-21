package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-18
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.issues.GetGitHubIssues")
public abstract class GetGitHubIssuesResult implements Result {

    @JsonCreator
    public static GetGitHubIssuesResult get(@JsonProperty("issues") List<GitHubIssue> issues) {
        return new AutoValue_GetGitHubIssuesResult(issues);
    }

    public abstract List<GitHubIssue> getIssues();
}
