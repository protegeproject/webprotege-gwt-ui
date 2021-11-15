package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.forms.SetEntityFormDescriptor")
public abstract class SetEntityFormDescriptorResult implements Result {

    @JsonCreator
    public static SetEntityFormDescriptorResult get() {
        return new AutoValue_SetEntityFormDescriptorResult();
    }
}
