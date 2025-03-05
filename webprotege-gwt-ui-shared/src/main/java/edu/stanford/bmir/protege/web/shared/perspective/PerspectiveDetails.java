package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PerspectiveDetails {


    @JsonCreator
    @Nonnull
    @JsonCreator
    public static PerspectiveDetails get(@JsonProperty("perspectiveId") @Nonnull PerspectiveId perspectiveId,
                                         @JsonProperty("label") @Nonnull LanguageMap label,
                                         @JsonProperty("favorite") boolean favorite,
                                         @JsonProperty("layout") @Nullable Node layout) {
        return new AutoValue_PerspectiveDetails(perspectiveId, label, favorite, layout);
    }

    @JsonProperty("perspectiveId")
    @Nonnull
    public abstract PerspectiveId getPerspectiveId();

    @JsonProperty("label")
    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonProperty("favorite")
    public abstract boolean isFavorite();

    @JsonProperty("layout")
    @Nullable
    protected abstract Node getLayoutInternal();

    @JsonIgnore
    @Nonnull
    public Optional<Node> getLayout() {
        return Optional.ofNullable(getLayoutInternal());
    }

    public PerspectiveDescriptor toPerspectiveDescriptor() {
        return PerspectiveDescriptor.get(getPerspectiveId(),
                                         getLabel(),
                                         isFavorite());
    }
}
