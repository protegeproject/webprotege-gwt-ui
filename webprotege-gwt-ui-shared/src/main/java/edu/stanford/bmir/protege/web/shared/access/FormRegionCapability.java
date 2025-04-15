package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("FormRegionCapability")
public abstract class FormRegionCapability implements Capability {


}
