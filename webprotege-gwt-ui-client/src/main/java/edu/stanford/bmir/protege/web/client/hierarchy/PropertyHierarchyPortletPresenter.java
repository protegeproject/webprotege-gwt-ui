package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityPresenter;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.search.SearchModal;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.tag.TagVisibilityPresenter;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.CREATE_PROPERTY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.DELETE_PROPERTY;
import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.*;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;
import static org.semanticweb.owlapi.model.EntityType.*;

@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.PropertyHierarchy",
        title = "Property Hierarchy",
        tooltip = "Displays the object, data and annotation property hierarchies as a tree.")
public class PropertyHierarchyPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final UIAction createAction;

    @Nonnull
    private final UIAction deleteAction;

    @Nonnull
    private final UIAction searchAction;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final SelectedPathsModel selectedPathsModel;
    @Nonnull
    private final PropertyHierarchyPortletView view;

    @Nonnull
    private final EntityHierarchyModel objectPropertyHierarchyModel;

    @Nonnull
    private final EntityHierarchyModel dataPropertyHierarchyModel;

    @Nonnull
    private final EntityHierarchyModel annotationPropertyHierarchyModel;

    @Nonnull
    private final TreeWidget<EntityNode, OWLEntity> objectPropertyTree;

    @Nonnull
    private final TreeWidget<EntityNode, OWLEntity> dataPropertyTree;

    @Nonnull
    private final TreeWidget<EntityNode, OWLEntity> annotationPropertyTree;

    @Nonnull
    private final EntityNodeHtmlRenderer renderer;

    @Nonnull
    private final CreateEntityPresenter createEntityPresenter;

    @Nonnull
    private final DeleteEntitiesPresenter deleteEntitiesPresenter;

    @Nonnull
    private final EntityHierarchyContextMenuPresenterFactory contextMenuPresenterFactory;

    @Nonnull
    private final HierarchyActionStatePresenter actionStatePresenter;

    @Nonnull
    private final Provider<EntityHierarchyDropHandler> entityHierarchyDropHandlerProvider;

    @Nonnull
    private final FilterView filterView;

    @Nonnull
    private final TagVisibilityPresenter tagVisibilityPresenter;

    @Nonnull
    private final SearchModal searchModal;

    @Nonnull
    private final TreeWidgetUpdaterFactory updaterFactory;

    private boolean transmittingSelectionFromHierarchy = false;

    private boolean settingSelectionInHierarchy = false;

    @Inject
    public PropertyHierarchyPortletPresenter(@Nonnull SelectionModel selectionModel,
                                             @Nonnull SelectedPathsModel selectedPathsModel,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull Messages messages,
                                             @Nonnull PropertyHierarchyPortletView view,
                                             @Nonnull EntityHierarchyModel objectPropertyHierarchyModel,
                                             @Nonnull EntityHierarchyModel dataPropertyHierarchyModel,
                                             @Nonnull EntityHierarchyModel annotationPropertyHierarchyModel,
                                             @Nonnull TreeWidget<EntityNode, OWLEntity> objectPropertyTree,
                                             @Nonnull TreeWidget<EntityNode, OWLEntity> dataPropertyTree,
                                             @Nonnull TreeWidget<EntityNode, OWLEntity> annotationPropertyTree,
                                             @Nonnull EntityNodeHtmlRenderer renderer,
                                             @Nonnull CreateEntityPresenter createEntityPresenter,
                                             @Nonnull DeleteEntitiesPresenter deleteEntitiesPresenter,
                                             @Nonnull EntityHierarchyContextMenuPresenterFactory contextMenuPresenterFactory,
                                             @Nonnull HierarchyActionStatePresenter actionStatePresenter,
                                             @Nonnull Provider<EntityHierarchyDropHandler> entityHierarchyDropHandlerProvider,
                                             @Nonnull FilterView filterView,
                                             @Nonnull TagVisibilityPresenter tagVisibilityPresenter,
                                             @Nonnull DisplayNameRenderer displayNameRenderer,
                                             @Nonnull SearchModal searchModal,
                                             @Nonnull TreeWidgetUpdaterFactory updaterFactory,
                                             DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.selectedPathsModel = selectedPathsModel;
        this.view = view;
        this.messages = messages;
        this.createAction = new PortletAction(messages.create(), "wp-btn-g--create-property wp-btn-g--create", this::handleCreate);
        this.deleteAction = new PortletAction(messages.delete(), "wp-btn-g--delete-property wp-btn-g--delete", this::handleDelete);
        this.searchAction = new PortletAction(messages.search(), "wp-btn-g--search", this::handleSearch);
        this.objectPropertyHierarchyModel = objectPropertyHierarchyModel;
        this.dataPropertyHierarchyModel = dataPropertyHierarchyModel;
        this.annotationPropertyHierarchyModel = annotationPropertyHierarchyModel;
        this.objectPropertyTree = objectPropertyTree;
        this.dataPropertyTree = dataPropertyTree;
        this.annotationPropertyTree = annotationPropertyTree;
        this.renderer = renderer;
        this.createEntityPresenter = createEntityPresenter;
        this.deleteEntitiesPresenter = deleteEntitiesPresenter;
        this.contextMenuPresenterFactory = contextMenuPresenterFactory;
        this.actionStatePresenter = actionStatePresenter;
        this.entityHierarchyDropHandlerProvider = entityHierarchyDropHandlerProvider;
        this.filterView = filterView;
        this.tagVisibilityPresenter = tagVisibilityPresenter;
        this.searchModal = searchModal;
        this.updaterFactory = updaterFactory;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.addAction(createAction);
        portletUi.addAction(deleteAction);
        portletUi.addAction(searchAction);
        portletUi.setFilterView(filterView);

        createAction.setRequiresSelection(false);
        actionStatePresenter.registerAction(CREATE_PROPERTY, createAction);
        deleteAction.setRequiresSelection(true);
        actionStatePresenter.registerAction(DELETE_PROPERTY, deleteAction);

        startTree(ObjectPropertyHierarchyDescriptor.get(),
                  messages.hierarchy_objectproperties(),
                  eventBus,
                  objectPropertyHierarchyModel, objectPropertyTree);

        startTree(DataPropertyHierarchyDescriptor.get(),
                  messages.hierarchy_dataproperties(),
                  eventBus,
                  dataPropertyHierarchyModel, dataPropertyTree);

        startTree(AnnotationPropertyHierarchyDescriptor.get(),
                  messages.hierarchy_annotationproperties(),
                  eventBus,
                  annotationPropertyHierarchyModel, annotationPropertyTree);
        view.setSelectedHierarchy(ObjectPropertyHierarchyDescriptor.get());
        view.setHierarchyIdSelectedHandler(hierarchyDescriptor -> handleHierarchySwitched(hierarchyDescriptor));
        tagVisibilityPresenter.start(filterView, view);
        actionStatePresenter.start(eventBus);
        portletUi.setWidget(view);
        setSelectionInTree(getSelectedEntity());
    }

    /**
     * Starts and setups the specified model and tree using the specified event bus.  The renderer will be set and a
     * context menu will be installed.
     *
     * @param hierarchyDescriptor The hierarchy descriptor
     * @param label       The label for the tree
     * @param eventBus    The event bus
     * @param model       The model
     * @param treeWidget  The tree
     */
    private void startTree(@Nonnull HierarchyDescriptor hierarchyDescriptor,
                           @Nonnull String label,
                           @Nonnull WebProtegeEventBus eventBus,
                           @Nonnull EntityHierarchyModel model,
                           @Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget) {
        model.start(eventBus, hierarchyDescriptor);
        eventBus.addProjectEventHandler(getProjectId(),
                                        DisplayNameSettingsChangedEvent.ON_DISPLAY_LANGUAGE_CHANGED,
                                        event -> {
                                            renderer.setDisplayLanguage(event.getDisplayNameSettings());
                                            treeWidget.setRenderer(renderer);
                                        });
        treeWidget.setRenderer(renderer);
        treeWidget.setModel(GraphTreeNodeModel.create(model, EntityNode::getEntity));
        treeWidget.addSelectionChangeHandler(event -> handleSelectionChanged(event, treeWidget));
        contextMenuPresenterFactory.create(model,
                                           treeWidget,
                                           createAction,
                                           deleteAction,
                        getProjectId())
                .install();
        EntityHierarchyDropHandler entityHierarchyDropHandler = entityHierarchyDropHandlerProvider.get();
        treeWidget.setDropHandler(entityHierarchyDropHandler);
        entityHierarchyDropHandler.start(hierarchyDescriptor);
        view.addHierarchy(hierarchyDescriptor,
                          label,
                          treeWidget);
        TreeWidgetUpdater updater = updaterFactory.create(treeWidget, model);
        updater.start(eventBus);
    }

    private void handleSelectionChanged(SelectionChangeEvent selectionChangeEvent, TreeWidget<EntityNode, OWLEntity> treeWidget) {
        if (!treeWidget.isAttached()) {
            return;
        }
        GWT.log("[PropertyHierarchyPortletPresenter] handling selection changed in tree ");
        transmitSelectionFromTree();
    }

    private void handleHierarchySwitched(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        GWT.log("[PropertyHierarchyPortletPresenter] handling hierarchy switched");
        transmitSelectionFromTree();
    }

    private void transmitSelectionFromTree() {
        boolean selPresent = view.getSelectedHierarchy().map(h -> !h.getSelectedKeys().isEmpty()).orElse(false);
        actionStatePresenter.setSelectionPresent(selPresent);
        if (settingSelectionInHierarchy) {
            return;
        }
        try {
            transmittingSelectionFromHierarchy = true;
            view.getSelectedHierarchy().ifPresent(tree -> {
                selectedPathsModel.setSelectedPaths(tree.getSelectedKeyPaths());
                Optional<OWLEntity> sel = tree.getFirstSelectedKey();
                if (!sel.equals(getSelectedEntity())) {
                    sel.ifPresent(entity -> {
                        GWT.log("[PropertyHierarchyPortletPresenter] Transmitting selection from tree " + entity);
                        getSelectionModel().setSelection(entity);

                    });
                }
                if (!sel.isPresent()) {
                    selectedPathsModel.clearSelectedPaths();
                    GWT.log("[PropertyHierarchyPortletPresenter] Transmitting empty selection from tree");
                    getSelectionModel().clearSelection();
                }
            });
        } finally {
            transmittingSelectionFromHierarchy = false;
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        setSelectionInTree(entity);
    }

    @Override
    protected void handleReloadRequest() {
        objectPropertyTree.reload();
        dataPropertyTree.reload();
        annotationPropertyTree.reload();
    }

    private void setSelectionInTree(Optional<OWLEntity> entity) {
        if (transmittingSelectionFromHierarchy) {
            return;
        }
        try {
            GWT.log("[PropertyHierarchyPortletPresenter] Setting selection in tree for " + entity);
            settingSelectionInHierarchy = true;
            entity.ifPresent(sel -> setSelectionInTree(sel));
        } finally {
            settingSelectionInHierarchy = false;
        }
    }

    private void setSelectionInTree(@Nonnull OWLEntity sel) {
        if (sel.isOWLObjectProperty()) {
            view.setSelectedHierarchy(ObjectPropertyHierarchyDescriptor.get());
            selectedPathsModel.setSelectedPaths(objectPropertyTree.getSelectedKeyPaths());
            if (!objectPropertyTree.getSelectedKeys().contains(sel)) {
                objectPropertyTree.revealTreeNodesForKey(sel, REVEAL_FIRST);
            }
        }
        else if (sel.isOWLDataProperty()) {
            view.setSelectedHierarchy(DataPropertyHierarchyDescriptor.get());
            selectedPathsModel.setSelectedPaths(dataPropertyTree.getSelectedKeyPaths());
            if (!dataPropertyTree.getSelectedKeys().contains(sel)) {
                dataPropertyTree.revealTreeNodesForKey(sel, REVEAL_FIRST);
            }
        }
        else if (sel.isOWLAnnotationProperty()) {
            view.setSelectedHierarchy(AnnotationPropertyHierarchyDescriptor.get());
            selectedPathsModel.setSelectedPaths(annotationPropertyTree.getSelectedKeyPaths());
            if (!annotationPropertyTree.getSelectedKeys().contains(sel)) {
                annotationPropertyTree.revealTreeNodesForKey(sel, REVEAL_FIRST);
            }
        }
    }

    private void handleCreate() {
        view.getSelectedHierarchyDescriptor().ifPresent(hierarchyId -> {
            if (hierarchyId.equals(OBJECT_PROPERTY_HIERARCHY)) {
                handleCreateObjectProperty();
            }
            else if (hierarchyId.equals(DATA_PROPERTY_HIERARCHY)) {
                handleCreateDataProperty();
            }
            else if (hierarchyId.equals(ANNOTATION_PROPERTY_HIERARCHY)) {
                handleCreateAnnotationProperty();
            }
        });
    }

    private void handleCreateAnnotationProperty() {
        createEntityPresenter.createEntities(ANNOTATION_PROPERTY,
                                             getSelectedAnnotationProperties().stream().findFirst(),
                                             CreateEntitiesInHierarchyHandler.get(annotationPropertyTree));
    }

    private void handleCreateDataProperty() {
        createEntityPresenter.createEntities(DATA_PROPERTY,
                                             getSelectedDataProperties().stream().findFirst(),
                                             CreateEntitiesInHierarchyHandler.get(dataPropertyTree));
    }

    private void handleCreateObjectProperty() {
        createEntityPresenter.createEntities(OBJECT_PROPERTY,
                                             getSelectedObjectProperties().stream().findFirst(),
                                             CreateEntitiesInHierarchyHandler.get(objectPropertyTree));
    }

    private ImmutableSet<OWLObjectProperty> getSelectedObjectProperties() {
        return objectPropertyTree.getSelectedKeys()
                .stream()
                .filter(sel -> sel instanceof OWLObjectProperty)
                .map(sel -> (OWLObjectProperty) sel)
                .collect(toImmutableSet());
    }


    private ImmutableSet<OWLDataProperty> getSelectedDataProperties() {
        return dataPropertyTree.getSelectedKeys()
                .stream()
                .filter(sel -> sel instanceof OWLDataProperty)
                .map(sel -> (OWLDataProperty) sel)
                .collect(toImmutableSet());
    }


    private ImmutableSet<OWLAnnotationProperty> getSelectedAnnotationProperties() {
        return annotationPropertyTree.getSelectedKeys()
                .stream()
                .filter(sel -> sel instanceof OWLAnnotationProperty)
                .map(sel -> (OWLAnnotationProperty) sel)
                .collect(toImmutableSet());
    }

    private void handleDelete() {
        view.getSelectedHierarchy().ifPresent(treeWidget -> deleteEntitiesPresenter.start(treeWidget));
    }

    private void handleSearch() {
        searchModal.setEntityTypes(OBJECT_PROPERTY, DATA_PROPERTY, ANNOTATION_PROPERTY);
        searchModal.showModal();
    }
}
