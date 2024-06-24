package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridRowData implements Comparable<GridRowData> {


    @JsonCreator
    public static GridRowData get(@JsonProperty(PropertyNames.SUBJECT) @Nullable FormEntitySubject subject,
                                  @JsonProperty(PropertyNames.CELLS) @Nonnull ImmutableList<GridCellData> cellData) {
        return new AutoValue_GridRowData(subject, cellData);
    }


    @Nullable
    @JsonProperty(PropertyNames.SUBJECT)
    protected abstract FormEntitySubject getSubjectInternal();

    @Nonnull
    public Optional<FormEntitySubject> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @Nonnull
    @JsonProperty(PropertyNames.CELLS)
    public abstract ImmutableList<GridCellData> getCells();

    @Override
    public int compareTo(GridRowData o) {
        ImmutableList<GridCellData> cells = getCells();
        ImmutableList<GridCellData> otherCells = o.getCells();
        for(int i = 0; i < cells.size() && i < otherCells.size(); i++) {
            GridCellData cellData = cells.get(i);
            GridCellData otherCellData = otherCells.get(i);
            int diff = cellData.compareTo(otherCellData);
            if(diff != 0) {
                return diff;
            }
        }
        return 0;
    }
}
