package edu.stanford.bmir.protege.web.shared.linearization;


import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetLinearizationDefinitions")
public class GetLinearizationDefinitionsAction implements Action<GetLinearizationDefinitionsResult> {

    public static GetLinearizationDefinitionsAction create(){
        return new AutoValue_GetLinearizationDefinitionsAction();
    }
}
