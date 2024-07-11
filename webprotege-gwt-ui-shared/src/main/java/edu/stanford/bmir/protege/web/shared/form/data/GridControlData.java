package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GridControlData")
public abstract class GridControlData implements ComplexFormControlValue {

    @Nonnull
    public static GridControlData get(@JsonProperty(PropertyNames.CONTROL) @Nonnull GridControlDescriptor descriptor,
                                      @JsonProperty(PropertyNames.ROWS) @Nonnull Page<GridRowData> rows,
                                      @JsonProperty(PropertyNames.ORDERING) @Nonnull ImmutableSet<FormRegionOrdering> ordering) {
        return new AutoValue_GridControlData(descriptor, rows, ordering);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    public abstract GridControlDescriptor getDescriptor();

    @Nonnull
    public abstract Page<GridRowData> getRows();

    @Nonnull
    public abstract ImmutableSet<FormRegionOrdering> getOrdering();
}
