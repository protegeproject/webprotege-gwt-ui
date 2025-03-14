package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.PortletId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CustomContent")
public abstract class CustomContentDescriptor implements EntityCardContentDescriptor {

    @JsonCreator
    public static CustomContentDescriptor create(@JsonProperty("customContentId") CustomContentId customContentId) {
        return new AutoValue_CustomContentDescriptor(customContentId);
    }

    @JsonProperty("customContentId")
    @Nonnull
    public abstract CustomContentId getCustomContentId();

    @Override
    public <R> R accept(EntityCardContentDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
