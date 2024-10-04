package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.ScaleAllowMultiValue;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;

import java.util.List;

public interface ScaleValueSelectionView extends IsWidget, HasBusy {

    void clear();

    List<String> getText();

    void setTopClass(String scaleTopClass);
}
