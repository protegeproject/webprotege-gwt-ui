package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetPostCoordinationAxisToGenericScale")
public abstract class GetPostCoordinationAxisToGenericScaleResult implements Result {

    public abstract List<PostCoordinationAxisToGenericScale> getPostCoordinationAxisToGenericScales();
}
