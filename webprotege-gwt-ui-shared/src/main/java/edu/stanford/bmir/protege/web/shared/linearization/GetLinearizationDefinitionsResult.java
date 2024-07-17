package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetLinearizationDefinitions")
public abstract class GetLinearizationDefinitionsResult implements Result {

     public abstract List<LinearizationDefinition> getDefinitionList();

     @JsonCreator
     public static GetLinearizationDefinitionsResult create(@JsonProperty("definitionList") List<LinearizationDefinition> definitionsResponses) {
          return new AutoValue_GetLinearizationDefinitionsResult(definitionsResponses);
     }
}
