package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.forms.SetFormRegionAccessRestrictions")
public abstract class SetFormRegionAccessRestrictionsResult implements Result {

    @JsonCreator
    public static SetFormRegionAccessRestrictionsResult get() {
        return new AutoValue_SetFormRegionAccessRestrictionsResult();
    }

}
