package edu.stanford.bmir.protege.web.client.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.gh.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.github.SetLinkedGitHubRepository")
public abstract class SetLinkedGitHubRepositoryResult implements Result {

    @JsonCreator
    public static SetLinkedGitHubRepositoryResult create() {
        return new AutoValue_SetLinkedGitHubRepositoryResult();
    }
}
