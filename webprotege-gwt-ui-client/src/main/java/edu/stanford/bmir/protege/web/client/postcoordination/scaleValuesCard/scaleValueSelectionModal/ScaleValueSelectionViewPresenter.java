package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.ScaleAllowMultiValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class ScaleValueSelectionViewPresenter {

    private ScaleValueSelectionView view;
    private ScaleAllowMultiValue scaleAllowMultiValue;
    private String scaleTopClass;

    @Nonnull
    private final ModalManager modalManager;

    @Inject
    public ScaleValueSelectionViewPresenter(ScaleValueSelectionView view,
                                            @Nonnull ModalManager modalManager) {
        this.view = view;
        this.modalManager = modalManager;
    }

    @Nonnull
    public ScaleValueSelectionView getView() {
        return view;
    }

    public void start() {
    }

    public List<String> getSelections() {
        return view.getText();
    }

    public void setAllowMultiValue(ScaleAllowMultiValue scaleAllowMultiValue) {
        this.scaleAllowMultiValue = scaleAllowMultiValue;
    }

    public void setScaleTopClass(String scaleTopClass) {
        this.scaleTopClass = scaleTopClass;
        this.view.setTopClass(scaleTopClass);
    }
}
