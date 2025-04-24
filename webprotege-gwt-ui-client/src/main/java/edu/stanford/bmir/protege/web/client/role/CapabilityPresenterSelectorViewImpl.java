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

public class CapabilityPresenterSelectorViewImpl extends Composite implements CapabilityPresenterSelectorView {

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