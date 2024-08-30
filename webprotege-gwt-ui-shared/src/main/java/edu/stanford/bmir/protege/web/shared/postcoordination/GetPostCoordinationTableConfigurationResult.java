package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetTablePostCoordinationAxis")
public abstract class GetPostCoordinationTableConfigurationResult implements Result {

    public abstract PostCoordinationTableConfiguration getTableConfiguration();

    public abstract List<PostCoordinationTableAxisLabel> getLabels();

    @JsonCreator
    public static GetPostCoordinationTableConfigurationResult create(@JsonProperty("tableConfiguration") PostCoordinationTableConfiguration tableConfiguration, @JsonProperty("labels") List<PostCoordinationTableAxisLabel> labels) {
        return new AutoValue_GetPostCoordinationTableConfigurationResult(tableConfiguration, labels);
    }

}
