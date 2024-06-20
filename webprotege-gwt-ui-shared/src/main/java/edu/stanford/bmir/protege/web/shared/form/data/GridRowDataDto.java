package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

@AutoValue
@GwtCompatible(serializable = true)
public  abstract class GridRowDataDto {

    @JsonCreator
    @Nonnull
    public static GridRowDataDto get(@JsonProperty("subject") @Nullable FormSubjectDto subject,
                                     @JsonProperty("cells") @Nonnull ImmutableList<GridCellDataDto> cellData) {
        return new AutoValue_GridRowDataDto(subject, cellData);
    }

    @Nonnull
    public static GridRowDataDto get(@Nonnull ImmutableList<GridCellDataDto> cellData) {
        return new AutoValue_GridRowDataDto(null, cellData);
    }


    @Nullable
    protected abstract FormSubjectDto getSubjectInternal();

    @Nonnull
    public Optional<FormSubjectDto> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @Nonnull
    @JsonProperty("cells")
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
