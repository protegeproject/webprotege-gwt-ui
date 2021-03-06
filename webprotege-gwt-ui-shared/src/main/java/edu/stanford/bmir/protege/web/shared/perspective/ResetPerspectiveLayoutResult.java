package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
@JsonTypeName("webprotege.perspectives.ResetPerspectiveLayout")
public class ResetPerspectiveLayoutResult implements Result {

    private PerspectiveLayout resetLayout;

    @GwtSerializationConstructor
    private ResetPerspectiveLayoutResult() {
    }

    private ResetPerspectiveLayoutResult(@Nonnull PerspectiveLayout resetLayout) {
        this.resetLayout = checkNotNull(resetLayout);
    }

    public static ResetPerspectiveLayoutResult create(@Nonnull PerspectiveLayout resetLayout) {
        return new ResetPerspectiveLayoutResult(resetLayout);
    }

    @Nonnull
    public PerspectiveLayout getResetLayout() {
        return resetLayout;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resetLayout);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ResetPerspectiveLayoutResult)) {
            return false;
        }
        ResetPerspectiveLayoutResult other = (ResetPerspectiveLayoutResult) obj;
        return Objects.equals(resetLayout, other.resetLayout);
    }


    @Override
    public String toString() {
        return toStringHelper("ResetPerspectiveLayoutResult" )
                .addValue(resetLayout)
                .toString();
    }
}
