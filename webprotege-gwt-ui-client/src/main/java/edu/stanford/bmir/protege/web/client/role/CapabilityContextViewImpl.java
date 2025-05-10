package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;

public class CapabilityContextViewImpl extends Composite implements CapabilityContextView {

    interface CapabilityContextViewImplUiBinder extends UiBinder<HTMLPanel, CapabilityContextViewImpl> {

    }

    private static CapabilityContextViewImplUiBinder ourUiBinder = GWT.create(CapabilityContextViewImplUiBinder.class);

    @UiField
    ListBox contextSelectionBox;

    @UiField
    SimplePanel criteriaContainer;

    @Inject
    public CapabilityContextViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        contextSelectionBox.addItem("Any entity");
        contextSelectionBox.addItem("An entity that");
        contextSelectionBox.addChangeHandler(evt -> {
            updateView();
        });
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        updateView();
    }

    private void updateView() {
        criteriaContainer.setVisible(contextSelectionBox.getSelectedIndex() != 0);
    }

    @Override
    public AcceptsOneWidget getCriteriaContainer() {
        return criteriaContainer;
    }

    @Override
    public boolean isForAnyEntity() {
        return contextSelectionBox.getSelectedIndex() == 0;
    }

    @Override
    public void setForAnyEntity(boolean forAnyEntity) {
        if(forAnyEntity) {
            contextSelectionBox.setSelectedIndex(0);
        }
        else {
            contextSelectionBox.setSelectedIndex(1);
        }
        updateView();
    }
}