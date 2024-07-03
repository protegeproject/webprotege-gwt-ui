package edu.stanford.bmir.protege.web.shared.form;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GridColumnBindingMissingException extends RuntimeException implements IsSerializable {

    private FormRegionId columnId;

    public GridColumnBindingMissingException(FormRegionId columnId) {
        super("Grid column binding missing for " + columnId + ".  Form is not configured properly");
        this.columnId = checkNotNull(columnId);
    }

    @GwtSerializationConstructor
    private GridColumnBindingMissingException() {
    }

    @Nonnull
    public FormRegionId getColumnId() {
        return columnId;
    }
}
