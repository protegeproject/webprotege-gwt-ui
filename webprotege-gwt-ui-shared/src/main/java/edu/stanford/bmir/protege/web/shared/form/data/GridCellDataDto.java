package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridCellDataDto implements HasFilterState {

    @JsonCreator
    public static GridCellDataDto get(@JsonProperty(PropertyNames.COLUMN_ID) @Nonnull GridColumnId columnId,
                                      @JsonProperty(PropertyNames.VALUES) @Nullable Page<FormControlDataDto> values,
                                      @JsonProperty(PropertyNames.FILTER_STATE) @Nonnull FilterState filterState) {
        return new AutoValue_GridCellDataDto(columnId, values, filterState);
    }

    @Nonnull
    @JsonProperty(PropertyNames.COLUMN_ID)
    public abstract GridColumnId getColumnId();

    @Nonnull
    @JsonProperty(PropertyNames.VALUES)
    public abstract Page<FormControlDataDto> getValues();

    @Nonnull
    @Override
    @JsonProperty(PropertyNames.FILTER_STATE)
    public abstract FilterState getFilterState();

    /**
     * Determines whether this cell data is filtered empty
     * @return true if this cel data is filtered empty (all values have been filtered out)
     * otherwise false.
     */
    @JsonIgnore
    public boolean isFilteredEmpty() {
        return getFilterState().equals(FilterState.FILTERED)
                && getValues().getPageElements().isEmpty();
    }

    public GridCellData toGridCellData() {
        return GridCellData.get(getColumnId(),
                getValues().transform(FormControlDataDto::toFormControlData));
    }
}
