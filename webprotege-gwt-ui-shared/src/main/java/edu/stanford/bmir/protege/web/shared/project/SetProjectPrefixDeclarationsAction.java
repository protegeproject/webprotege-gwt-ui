package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2018
 */
@JsonTypeName("webprotege.projects.SetProjectPrefixDeclarations")
public class SetProjectPrefixDeclarationsAction implements ProjectAction<SetProjectPrefixDeclarationsResult> {

    private ChangeRequestId changeRequestId;

    private ProjectId projectId;

    private List<PrefixDeclaration> prefixDeclarations;

    @JsonCreator
    private SetProjectPrefixDeclarationsAction(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                               @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                               @JsonProperty("prefixDeclarations") @Nonnull List<PrefixDeclaration> prefixDeclarations) {
        this.changeRequestId = checkNotNull(changeRequestId);
        this.projectId = checkNotNull(projectId);
        this.prefixDeclarations = new ArrayList<>(checkNotNull(prefixDeclarations));
    }

    @GwtSerializationConstructor
    private SetProjectPrefixDeclarationsAction() {
    }

    @GwtIncompatible
    public static SetProjectPrefixDeclarationsAction create(@Nonnull ProjectId projectId,
                                                            @Nonnull List<PrefixDeclaration> prefixDeclarations) {
        return new SetProjectPrefixDeclarationsAction(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, prefixDeclarations);
    }

    public static SetProjectPrefixDeclarationsAction create(@Nonnull ChangeRequestId changeRequestId,
                                                            @Nonnull ProjectId projectId,
                                                            @Nonnull List<PrefixDeclaration> prefixDeclarations) {
        return new SetProjectPrefixDeclarationsAction(changeRequestId, projectId, prefixDeclarations);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the prefix declarations to set
     */
    @Nonnull
    public List<PrefixDeclaration> getPrefixDeclarations() {
        return new ArrayList<>(prefixDeclarations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, prefixDeclarations);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectPrefixDeclarationsAction)) {
            return false;
        }
        SetProjectPrefixDeclarationsAction other = (SetProjectPrefixDeclarationsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.prefixDeclarations.equals(other.prefixDeclarations);
    }


    @Override
    public String toString() {
        return toStringHelper("SetProjectPrefixDeclarationsAction")
                .addValue(projectId)
                .addValue(prefixDeclarations)
                .toString();
    }
}
