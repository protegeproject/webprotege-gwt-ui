package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetTablePostCoordinationAxis")
public abstract class GetPostCoordinationTableConfigurationAction implements Action<GetPostCoordinationTableConfigurationResult> {

    public static GetPostCoordinationTableConfigurationAction create(String entityType){
        return new AutoValue_GetPostCoordinationTableConfigurationAction(entityType);
    }

    public abstract String getEntityType();
}
