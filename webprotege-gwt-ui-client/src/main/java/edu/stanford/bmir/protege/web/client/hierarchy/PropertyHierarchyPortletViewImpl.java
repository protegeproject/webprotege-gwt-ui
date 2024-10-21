package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 4 Dec 2017
 */
public class PropertyHierarchyPortletViewImpl extends Composite implements PropertyHierarchyPortletView {
    interface PropertyHierarchyPortletViewImplUiBinder extends UiBinder<HTMLPanel, PropertyHierarchyPortletViewImpl> {
    }

    private static PropertyHierarchyPortletViewImplUiBinder ourUiBinder = GWT.create(PropertyHierarchyPortletViewImplUiBinder.class);

    @UiField
    TabBar switcher;

    @UiField
    SimplePanel hierarchyContainer;

    private final List<HierarchyDescriptor> hierarchyDescriptors = new ArrayList<>();

    private final List<TreeWidget<EntityNode, OWLEntity>> views = new ArrayList<>();

    @Nonnull
    private HierarchySelectedHandler hierarchySelectedHandler = hierarchyDescriptor -> {};

    @Inject
    public PropertyHierarchyPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        switcher.addSelectionHandler(selectionEvent -> handleTabSelectionChanged());
    }

    private void handleTabSelectionChanged() {
        int selection = switcher.getSelectedTab();
        HierarchyDescriptor hierarchyDescriptor = hierarchyDescriptors.get(selection);
        IsWidget view = views.get(selection);
        hierarchyContainer.setWidget(view);
        hierarchySelectedHandler.handleHierarchyDescriptorSelected(hierarchyDescriptor);
    }

    @Override
    public void addHierarchy(@Nonnull HierarchyDescriptor hierarchyDescriptor,
                             @Nonnull String label,
                             @Nonnull TreeWidget<EntityNode, OWLEntity> view) {
        switcher.addTab(checkNotNull(label));
        hierarchyDescriptors.add(checkNotNull(hierarchyDescriptor));
        views.add(checkNotNull(view));
    }

    @Override
    public void setHierarchyIdSelectedHandler(@Nonnull HierarchySelectedHandler hierarchySelectedHandler) {
        this.hierarchySelectedHandler = checkNotNull(hierarchySelectedHandler);
    }

    @Override
    public void setSelectedHierarchy(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        int selection = hierarchyDescriptors.indexOf(hierarchyDescriptor);
        if(switcher.getSelectedTab() == selection) {
            return;
        }
        GWT.log("[PropertyHierarchyPortletViewImpl] Switching tab to " + hierarchyDescriptor);
        switcher.selectTab(selection);
        IsWidget view = views.get(selection);
        hierarchyContainer.setWidget(view);
    }

    @Override
    public Optional<HierarchyDescriptor> getSelectedHierarchyDescriptor() {
        return Optional.of(hierarchyDescriptors.get(switcher.getSelectedTab()));
    }

    @Override
    public Optional<TreeWidget<EntityNode, OWLEntity>> getSelectedHierarchy() {
        return Optional.of(views.get(switcher.getSelectedTab()));
    }
}