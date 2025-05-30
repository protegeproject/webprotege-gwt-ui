package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.role.CapabilityValueEditor;
import edu.stanford.bmir.protege.web.client.role.CapabilityValueEditorFactory;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class HierarchyDescriptorRuleViewImpl extends Composite implements HierarchyDescriptorRuleView {

    interface HierarchyDescriptorRuleViewImplUiBinder extends UiBinder<HTMLPanel, HierarchyDescriptorRuleViewImpl> {

    }

    private static HierarchyDescriptorRuleViewImplUiBinder ourUiBinder = GWT.create(HierarchyDescriptorRuleViewImplUiBinder.class);

    @UiField
    SimplePanel hierarchyDescriptorContainer;

    @UiField(provided = true)
    ValueListFlexEditorImpl<Capability> capabilityListEditor;

    @UiField
    SimplePanel perspectiveChooserContainer;

    @Inject
    public HierarchyDescriptorRuleViewImpl(CapabilityValueEditorFactory valueEditorFactory) {
        capabilityListEditor = new ValueListFlexEditorImpl<>(valueEditorFactory);
        capabilityListEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        capabilityListEditor.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setCapabilities(List<Capability> capabilities) {
        capabilityListEditor.setValue(capabilities);
    }

    @Override
    public List<Capability> getCapabilities() {
        return capabilityListEditor.getValue().orElse(Collections.emptyList());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHierarchyDescriptorContainer() {
        return hierarchyDescriptorContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getPerspectiveChooserContainer() {
        return perspectiveChooserContainer;
    }
}