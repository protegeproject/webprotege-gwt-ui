package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.PortletId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("PortletCardContentDescriptor")
public abstract class PortletCardContentDescriptor implements CardContentDescriptor {

    @JsonCreator
    public static PortletCardContentDescriptor create(@JsonProperty("portletId") PortletId portletId) {
        return new AutoValue_PortletCardContentDescriptor(portletId);
    }

    @JsonProperty("portletId")
    @Nonnull
    public abstract PortletId getPortletId();

    @Override
    public <R> R accept(CardContentDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
