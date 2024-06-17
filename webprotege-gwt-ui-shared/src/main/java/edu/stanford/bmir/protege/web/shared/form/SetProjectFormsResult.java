package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-15
 */
@JsonTypeName("webprotege.forms.SetProjectForms")
@AutoValue
@GwtCompatible(serializable = true)
public abstract class SetProjectFormsResult implements Result {

    public static SetProjectFormsResult get() {
        return new AutoValue_SetProjectFormsResult();
    }
}
