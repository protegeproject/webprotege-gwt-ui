package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CapabilityPresenterSelectorViewImpl extends Composite implements CapabilityPresenterSelectorView {

    private final static Logger logger = Logger.getLogger(CapabilityPresenterSelectorView.class.getName());

    interface CapabilityPresenterSelectorViewImplUiBinder extends UiBinder<HTMLPanel, CapabilityPresenterSelectorViewImpl> {

    }

    private static CapabilityPresenterSelectorViewImplUiBinder ourUiBinder = GWT.create(CapabilityPresenterSelectorViewImplUiBinder.class);

    private final List<String> typeIds = new ArrayList<>();

    private final List<String> labels = new ArrayList<>();

    @UiField
    ListBox typeIdSelector;

    @UiField
    SimplePanel presenterContainer;

    @Inject
    public CapabilityPresenterSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void addTypeId(String typeId, String label) {
        typeIds.add(typeId);
        labels.add(label);
        typeIdSelector.addItem(label, typeId);
    }

    @Override
    public void clearTypeIds() {
        typeIds.clear();
        labels.clear();
        typeIdSelector.clear();
    }

    @Override
    public void clearPresenter() {
    }

    @Override
    public void setCapabilityEditable(boolean editable) {
        typeIdSelector.setEnabled(editable);
    }

    @Override
    public void setSelectedTypeId(String selectedTypeId) {
        logger.info("Setting selected type id to " + selectedTypeId);
        for(int i = 0; i < typeIdSelector.getItemCount(); i++) {
            String value = typeIdSelector.getValue(i);
            logger.info("Value is " + value);
            if(value.equals(selectedTypeId)) {
                typeIdSelector.setSelectedIndex(i);
                break;
            }
        }
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getPresenterContainer() {
        return presenterContainer;
    }

    @Override
    public String getSelectedTypeId() {
        return Optional.ofNullable(typeIdSelector.getSelectedValue()).orElse("");
    }

    @Override
    public void setSelectedTypeIdChangedHandler(SelectedTypeIdChangedHandler handler) {
        typeIdSelector.addChangeHandler(evt -> handler.handleSelectedTypeIdChanged(getSelectedTypeId()));
    }
}