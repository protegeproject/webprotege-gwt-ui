package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import static edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsWithLinearizationAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetClassAncestorsWithLinearizationAction implements Action<GetClassAncestorsWithLinearizationResult> {

    public final static String CHANNEL = "webprotege.entities.GetClassAncestorsWithLinearization";

    @JsonCreator
    public static GetClassAncestorsWithLinearizationAction create(@JsonProperty("classIri") IRI classIri,
                                                                  @JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetClassAncestorsWithLinearizationAction(classIri, projectId);
    }

    public abstract IRI getClassIri();
    public abstract ProjectId getProjectId();
}
