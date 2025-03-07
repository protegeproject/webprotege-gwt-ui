package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityPresenter;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.search.SearchModal;
import edu.stanford.bmir.protege.web.client.searchIcd.SearchIcdModal;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.tag.TagVisibilityPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.DisplayContext;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyDescriptorAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyDescriptorResult;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_CLASS;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.DELETE_CLASS;
import static edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent.ON_DISPLAY_LANGUAGE_CHANGED;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;
import static org.semanticweb.owlapi.model.EntityType.CLASS;


@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.ClassHierarchy",
        title = "Class Hierarchy",
        tooltip = "Displays the class hierarchy as a tree.")
public class ClassHierarchyPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final Logger logger = Logger.getLogger("ClassHierarchyPortletPresenter");

    @Nonnull
    private final SearchModal searchModal;

    @Nonnull
    private final SearchIcdModal searchIcdModal;

    private final Messages messages;

    private final EntityHierarchyModel hierarchyModel;
    @Nonnull
    private final EntityNodeHtmlRenderer renderer;

    private final UIAction searchActionIcd;

    private final UIAction createClassAction;

    private final UIAction deleteClassAction;

    private final UIAction searchAction;

    @Nonnull
    private final CreateEntityPresenter createEntityPresenter;

    @Nonnull
    private final DeleteEntitiesPresenter deleteEntitiesPresenter;

    @Nonnull
    private final EntityHierarchyContextMenuPresenterFactory contextMenuPresenterFactory;

    @Nonnull
    private final HierarchyActionStatePresenter actionStatePresenter;

    @Nonnull
    private final TreeWidget<EntityNode, OWLEntity> treeWidget;

    @Nonnull
    private final EntityHierarchyDropHandler dropHandler;

    @Nonnull
    private final FilterView filterView;

    @Nonnull
    private final TagVisibilityPresenter tagVisibilityPresenter;
    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;
    @Nonnull
    private final DispatchServiceManager dispatch;
    @Nonnull
    private final SelectedPathsModel selectedPathsModel;

    private boolean transmittingSelectionFromTree = false;
    private TreeWidgetUpdater updater;

    private boolean settingSelectionInTree = false;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public ClassHierarchyPortletPresenter(@Nonnull final ProjectId projectId,
                                          @Nonnull SelectionModel selectionModel,
                                          @Nonnull SearchModal searchModal,
                                          @Nonnull SearchIcdModal searchIcdModal,
                                          @Nonnull Messages messages,
                                          @Nonnull EntityHierarchyModel hierarchyModel,
                                          @Nonnull EntityHierarchyContextMenuPresenterFactory contextMenuPresenterFactory,
                                          @Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget,
                                          @Nonnull EntityNodeHtmlRenderer renderer,
                                          @Nonnull CreateEntityPresenter createEntityPresenter,
                                          @Nonnull DeleteEntitiesPresenter deleteEntitiesPresenter,
                                          @Nonnull HierarchyActionStatePresenter actionStatePresenter,
                                          @Nonnull EntityHierarchyDropHandler dropHandler,
                                          @Nonnull FilterView filterView,
                                          @Nonnull TagVisibilityPresenter tagVisibilityPresenter,
                                          @Nonnull DisplayNameRenderer displayNameRenderer,
                                          @Nonnull DisplayNameSettingsManager displayNameSettingsManager,
                                          @Nonnull TreeWidgetUpdaterFactory updaterFactory,
                                          @Nonnull DispatchServiceManager dispatch,
                                          @Nonnull LoggedInUserManager loggedInUserManager,
                                          @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                          @Nonnull SelectedPathsModel selectedPathsModel) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.searchModal = searchModal;
        this.searchIcdModal = searchIcdModal;
        this.messages = checkNotNull(messages);
        this.hierarchyModel = checkNotNull(hierarchyModel);
        this.contextMenuPresenterFactory = checkNotNull(contextMenuPresenterFactory);
        this.treeWidget = checkNotNull(treeWidget);
        this.renderer = checkNotNull(renderer);
        this.createEntityPresenter = checkNotNull(createEntityPresenter);

        this.createClassAction = new PortletAction(messages.create(),
                "wp-btn-g--create-class wp-btn-g--create",
                this::handleCreateSubClasses);

        this.deleteClassAction = new PortletAction(messages.delete(),
                "wp-btn-g--delete-class wp-btn-g--delete",
                this::handleDelete);

        this.searchActionIcd = new PortletAction(messages.searchIcd(),
                "wp-btn-g--searchIcd",
                this::handleIcdSearch);
        this.searchAction = new PortletAction(messages.search(),
                "wp-btn-g--search",
                this::handleSearch);
        this.deleteEntitiesPresenter = deleteEntitiesPresenter;
        this.actionStatePresenter = actionStatePresenter;
        this.dropHandler = dropHandler;
        this.filterView = checkNotNull(filterView);
        this.tagVisibilityPresenter = checkNotNull(tagVisibilityPresenter);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.dispatch = dispatch;
        this.selectedPathsModel = selectedPathsModel;
        this.treeWidget.addSelectionChangeHandler(this::transmitSelectionFromTree);
        this.updater = updaterFactory.create(treeWidget, hierarchyModel);
    }


    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        setSelectionInTree(entityData);
    }

    @Override
    protected void handleReloadRequest() {
        Optional<OWLEntity> firstSelectedKey = treeWidget.getFirstSelectedKey();
        treeWidget.setModel(GraphTreeNodeModel.create(hierarchyModel, EntityNode::getEntity));
        firstSelectedKey.ifPresent(sel -> treeWidget.revealTreeNodesForKey(sel, REVEAL_FIRST));
    }

    @Override
    public void startPortlet(@Nonnull PortletUi portletUi,
                             @Nonnull WebProtegeEventBus eventBus) {
        portletUi.addAction(createClassAction);
       // portletUi.addAction(deleteClassAction);
        portletUi.addAction(searchActionIcd);
        portletUi.addAction(searchAction);
        portletUi.setWidget(treeWidget);
        portletUi.setFilterView(filterView);

        createClassAction.setRequiresSelection(false);
        actionStatePresenter.registerAction(CREATE_CLASS, createClassAction);
        /* TODO uncomment this after delete is implemented*/
       // deleteClassAction.setRequiresSelection(true);
       // actionStatePresenter.registerAction(DELETE_CLASS, deleteClassAction);

        permissionChecker.hasPermission(DELETE_CLASS, deleteClassAction::setVisible);

        actionStatePresenter.start(eventBus);

        renderer.setDisplayLanguage(displayNameSettingsManager.getLocalDisplayNameSettings());

        logger.info("Getting display context");
        DisplayContext displayContext = fillDisplayContextBuilder().build();
        dispatch.execute(GetHierarchyDescriptorAction.create(getProjectId(), displayContext),
                result -> setupHierarchy(eventBus, result));
        eventBus.addProjectEventHandler(getProjectId(), ON_DISPLAY_LANGUAGE_CHANGED, this::handleDisplayLanguageChanged);
        updater.start(eventBus);
    }

    private void setupHierarchy(WebProtegeEventBus eventBus, GetHierarchyDescriptorResult result) {
        HierarchyDescriptor hierarchyDescriptor = result.getHierarchyDescriptor();
        logger.info("Setting up hierarchy for descriptor: " + hierarchyDescriptor);
        if (hierarchyDescriptor == null) {
            hierarchyDescriptor = ClassHierarchyDescriptor.get();
        }
        hierarchyModel.start(eventBus, hierarchyDescriptor);
        dropHandler.start(hierarchyDescriptor);
        treeWidget.setRenderer(renderer);
        treeWidget.setModel(GraphTreeNodeModel.create(hierarchyModel,
                node -> node.getEntity()));
        treeWidget.setDropHandler(this.dropHandler);
        contextMenuPresenterFactory.create(hierarchyModel,
                        treeWidget,
                        createClassAction,
                        deleteClassAction,
                        getProjectId())
                .install();
        treeWidget.addAttachHandler(event -> {
            updateSelectedPaths();
        });
        tagVisibilityPresenter.start(filterView, treeWidget);
        setSelectionInTree(getSelectedEntity());
    }

    private void handleDisplayLanguageChanged(@Nonnull DisplayNameSettingsChangedEvent event) {
        setPreferredDisplayLanguage(event.getDisplayNameSettings());
    }

    private void setPreferredDisplayLanguage(@Nonnull DisplayNameSettings lang) {
        renderer.setDisplayLanguage(checkNotNull(lang));
        treeWidget.setRenderer(renderer);
    }

    private Optional<OWLClass> getFirstSelectedClass() {
        return treeWidget.getFirstSelectedKey()
                .filter(sel -> sel instanceof OWLClass)
                .map(sel -> (OWLClass) sel);
    }

    private void transmitSelectionFromTree(SelectionChangeEvent event) {
        actionStatePresenter.setSelectionPresent(!treeWidget.getSelectedKeys().isEmpty());
        if (!treeWidget.isAttached()) {
            return;
        }
        if (settingSelectionInTree) {
            return;
        }
        try {
            transmittingSelectionFromTree = true;
            treeWidget.getFirstSelectedKey()
                    .ifPresent(sel -> {
                        updateSelectedPaths();
                        getSelectionModel().setSelection(sel);
                    });

            if (!treeWidget.getFirstSelectedKey().isPresent()) {
                getSelectionModel().clearSelection();
                updateSelectedPaths();
            }
        } finally {
            transmittingSelectionFromTree = false;
        }
    }

    private void handleCreateSubClasses() {
        createEntityPresenter.createEntities(CLASS,
                getFirstSelectedClass(),
                CreateEntitiesInHierarchyHandler.get(treeWidget)
        );
    }

    private ImmutableSet<OWLClass> getSelectedOwlClasses() {
        return treeWidget.getSelectedKeys().stream().map(k -> k.asOWLClass()).collect(toImmutableSet());
    }

    private void handleDelete() {
        deleteEntitiesPresenter.start(treeWidget);
    }

    private void handleSearch() {
        searchModal.setEntityTypes(CLASS);
        searchModal.showModal();
    }

    private void handleIcdSearch() {
        searchIcdModal.setEntityTypes(CLASS);
        searchIcdModal.setHierarchySelectedOptions(treeWidget.getFirstSelectedUserObject());
        searchIcdModal.showModal();
    }

    private void selectAndExpandPath(Path<OWLEntity> entityPath) {
        treeWidget.setSelected(entityPath, true, () -> treeWidget.setExpanded(entityPath));
    }

    private void setSelectionInTree(Optional<OWLEntity> selection) {
        if (transmittingSelectionFromTree) {
            return;
        }
        updateSelectedPaths();
        if (treeWidget.getSelectedKeys().contains(selection.orElse(null))) {
            return;
        }
        try {
            settingSelectionInTree = true;
            selection.ifPresent(sel -> {
                if (sel.isOWLClass()) {
                    treeWidget.revealTreeNodesForKey(sel, REVEAL_FIRST);
                    updateSelectedPaths();
                }
            });
        } finally {
            settingSelectionInTree = false;
        }
    }

    private void updateSelectedPaths() {
        if(treeWidget.isAttached()) {
            selectedPathsModel.setSelectedPaths(treeWidget.getSelectedKeyPaths());
        }
    }

    @Override
    public void fillDisplayContext(DisplayContextBuilder displayContextBuilder) {
        super.fillDisplayContext(displayContextBuilder);
        List<List<OWLEntity>> selectedPaths = treeWidget.getSelectedKeyPaths()
                .stream()
                .map(path -> path.asList())
                .collect(Collectors.toList());
        displayContextBuilder.setSelectedPaths(selectedPaths);
    }
}
