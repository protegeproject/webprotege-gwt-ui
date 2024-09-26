package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static edu.stanford.bmir.protege.web.shared.postcoordination.SaveEntityPostCoordinationAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class SaveEntityPostCoordinationAction extends AbstractHasProjectAction<ProcessUploadedPostCoordinationResult> {

    public final static String CHANNEL = "webprotege.postcoordination.AddEntitySpecificationRevision";

    @JsonCreator
    public static SaveEntityPostCoordinationAction create(@JsonProperty("projectId")
                                                          ProjectId projectId,
                                                          @JsonProperty("entitySpecification")
                                                          WhoficEntityPostCoordinationSpecification entitySpecification) {
        return new AutoValue_SaveEntityPostCoordinationAction(projectId, entitySpecification);
    }


    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("entitySpecification")
    public abstract WhoficEntityPostCoordinationSpecification getEntitySpecification();
}
