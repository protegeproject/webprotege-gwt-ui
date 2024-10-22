package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;

import javax.annotation.Nonnull;

public interface ScaleValueSelectionView extends IsWidget, HasBusy {

    @Nonnull
    AcceptsOneWidget getHierarchyContainer();
}
