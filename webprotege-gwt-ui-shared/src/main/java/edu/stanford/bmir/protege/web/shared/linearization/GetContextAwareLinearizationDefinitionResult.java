package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

import static edu.stanford.bmir.protege.web.shared.linearization.GetContextAwareLinearizationDefinitionAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetContextAwareLinearizationDefinitionResult implements Result {

    @JsonCreator
    public static GetContextAwareLinearizationDefinitionResult create(@JsonProperty("definitionList") List<LinearizationDefinition> definitionsResponses,
                                                                      @JsonProperty("canEditResiduals") boolean canEditResiduals) {
        return new AutoValue_GetContextAwareLinearizationDefinitionResult(definitionsResponses, canEditResiduals);
    }

    public abstract List<LinearizationDefinition> getDefinitionList();
    public abstract boolean getCanEditResiduals();
}
