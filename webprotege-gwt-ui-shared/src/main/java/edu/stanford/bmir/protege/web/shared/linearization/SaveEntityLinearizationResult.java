package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.SaveEntityLinearization")
public class SaveEntityLinearizationResult implements Result {

}
