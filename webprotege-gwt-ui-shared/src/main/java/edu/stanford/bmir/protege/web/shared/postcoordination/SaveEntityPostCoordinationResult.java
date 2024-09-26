package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static edu.stanford.bmir.protege.web.shared.postcoordination.SaveEntityPostCoordinationAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class SaveEntityPostCoordinationResult  implements Result {
}
