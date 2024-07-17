package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

@AutoValue
@GwtCompatible(serializable = true)
public  abstract class GridRowDataDto {

    @JsonCreator
    @Nonnull
    public static GridRowDataDto get(@JsonProperty(PropertyNames.SUBJECT) @Nullable FormSubjectDto subject,
                                     @JsonProperty(PropertyNames.CELLS) @Nonnull ImmutableList<GridCellDataDto> cellData) {
        return new AutoValue_GridRowDataDto(subject, cellData);
    }

    @Nonnull
    public static GridRowDataDto get(@Nonnull ImmutableList<GridCellDataDto> cellData) {
        return new AutoValue_GridRowDataDto(null, cellData);
    }


    @Nullable
    @JsonProperty(PropertyNames.SUBJECT)
    protected abstract FormSubjectDto getSubjectInternal();

    @Nonnull
    @JsonIgnore
    public Optional<FormSubjectDto> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @Nonnull
    @JsonProperty(PropertyNames.CELLS)
    public abstract ImmutableList<GridCellDataDto> getCells();

    /**
     * Determines whether this row contains cells that have been filtered empty
     * @return true if this row contains cells that have been filtered empty, otherwise false.
     */
    public boolean containsFilteredEmptyCells() {
        return getCells().stream()
                .anyMatch(GridCellDataDto::isFilteredEmpty);
    }

    public GridRowData toGridRowData() {
        return GridRowData.get(getSubject().map(FormSubjectDto::toFormSubject).orElse(null),
                getCells().stream().map(GridCellDataDto::toGridCellData).collect(toImmutableList()));
    }
}
