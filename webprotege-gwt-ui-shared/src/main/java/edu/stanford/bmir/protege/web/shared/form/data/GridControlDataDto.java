package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GridControlDataDto")
public abstract class GridControlDataDto implements FormControlDataDto, HasFilterState {

    @JsonCreator
    @Nonnull
    public static GridControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull GridControlDescriptor descriptor,
                                         @JsonProperty(PropertyNames.ROWS) @Nonnull Page<GridRowDataDto> rows,
                                         @JsonProperty(PropertyNames.ORDERING) @Nonnull ImmutableSet<FormRegionOrdering> ordering,
                                         @JsonProperty(PropertyNames.DEPTH) int depth,
                                         @JsonProperty(PropertyNames.FILTER_STATE) @Nonnull FilterState filterState) {
        return new AutoValue_GridControlDataDto(depth, descriptor, rows, ordering, filterState);
    }

    @Nonnull
    @JsonProperty(PropertyNames.CONTROL)
    public abstract GridControlDescriptor getDescriptor();

    @Nonnull
    @JsonProperty(PropertyNames.ROWS)
    public abstract Page<GridRowDataDto> getRows();

    @Nonnull
    @JsonProperty(PropertyNames.ORDERING)
    public abstract ImmutableSet<FormRegionOrdering> getOrdering();

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public GridControlData toFormControlData() {
        return GridControlData.get(getDescriptor(),
                                   getRows().transform(GridRowDataDto::toGridRowData),
                                   getOrdering());
    }

    @Nonnull
    @Override
    @JsonProperty(PropertyNames.FILTER_STATE)
    public abstract FilterState getFilterState();

    /**
     * Determines whether this grid is empty because it has been filtered empty.
     * @return true if this grid is empty and this is due to filtering
     */
    public boolean isFilteredEmpty() {
        return getFilterState().equals(FilterState.FILTERED) &&
                getRows().getTotalElements() == 0;
    }
}
