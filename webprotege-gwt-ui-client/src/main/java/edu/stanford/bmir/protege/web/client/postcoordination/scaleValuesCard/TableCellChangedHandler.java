package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;

public interface TableCellChangedHandler {
    void handleTableCellChanged(boolean isOnMultipleRows, CheckboxValue checkboxValue, String tableAxisIri);
}
