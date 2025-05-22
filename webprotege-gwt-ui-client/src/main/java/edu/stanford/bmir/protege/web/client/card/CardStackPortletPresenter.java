package edu.stanford.bmir.protege.web.client.card;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.*;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.selection.*;
import edu.stanford.bmir.protege.web.client.tab.*;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.card.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.permissions.GetAuthorizedCapabilitiesForEntityAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.*;

@Portlet(id = "webprotege.card",
        title = "Card view",
        tooltip = "Presents a stack of cards that display information about the selected entity.")
public class CardStackPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final Logger logger = Logger.getLogger("CardStackPortletPresenter");

    private final EntityCardPresenterFactory entityCardPresenterFactory;

    private final Set<CardId> currentCardIds = new HashSet<>();

    private final Set<CardId> writableCardIds = new HashSet<>();

    private final Map<CardId, EntityCardComponents> cardComponents = new LinkedHashMap<>();

    private final CardStackPortletView view;

    @Nonnull
    private final Provider<EntityCardUi> entityCardViewProvider;

    private final TabBarPresenter<CardId> tabBarPresenter;

    private final DispatchServiceManager dispatch;

    private Optional<WebProtegeEventBus> eventBus = Optional.empty();

    private final InputBox inputBox;

    private final MessageBox messageBox;
    @Nonnull
    private final Messages messages;

    private final CommitChangesWorkflow commitChangesWorkflow;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    private ImmutableSet<Capability> currentCapabilities = ImmutableSet.of();

    @Inject
    public CardStackPortletPresenter(@Nonnull ProjectId projectId,
                                     @Nonnull SelectionModel selectionModel,
                                     @Nonnull DisplayNameRenderer displayNameRenderer,
                                     @Nonnull DispatchServiceManager dispatch, CardStackPortletView view,
                                     @Nonnull EntityCardPresenterFactory entityCardPresenterFactory,
                                     @Nonnull Provider<EntityCardUi> entityCardViewProvider,
                                     @Nonnull TabBarPresenter<CardId> tabBarPresenter,
                                     @Nonnull InputBox inputBox,
                                     @Nonnull MessageBox messageBox,
                                     @Nonnull Messages messages,
                                     @Nonnull CommitChangesWorkflow commitChangesWorkflow,
                                     @Nonnull SelectedPathsModel selectedPathsModel,
                                     @Nonnull LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.view = view;
        this.entityCardPresenterFactory = entityCardPresenterFactory;
        this.entityCardViewProvider = entityCardViewProvider;
        this.tabBarPresenter = tabBarPresenter;
        this.dispatch = dispatch;
        this.inputBox = inputBox;
        this.messageBox = messageBox;
        this.messages = messages;
        this.commitChangesWorkflow = commitChangesWorkflow;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        setDisplaySelectedEntityNameAsSubtitle(true);
        this.eventBus = Optional.of(eventBus);
        portletUi.setWidget(view);
        tabBarPresenter.setSelectedKeyStash(new SelectedTabIdStash<>(portletUi, new CardIdSerializer()));
        tabBarPresenter.start(view.getTabBarContainer());
        view.setBeginEditingHandler(this::handleStartEditing);
        view.setFinishEditingHandler(this::handleFinishEditing);
        view.setCancelEditingHandler(this::handleCancelEditing);
        initializeSelection();
    }

    private void initializeSelection() {
        handleAfterSetEntity(getSelectedEntity());
    }

    private void displayCardsForSelectedEntity() {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();

        selectedEntity.ifPresent(sel -> {
            dispatch.execute(
                    GetAuthorizedCapabilitiesForEntityAction.create(getProjectId(), loggedInUserProvider.getCurrentUserId(), sel.getIRI()),
                    result -> {
                        this.currentCapabilities = result.getCapabilities();
                        retrieveAndSetDisplayedCardsForEntity(sel, () -> {
                            this.transmitEntitySelectionToDisplayedCards();
                            this.transmitCapabilitiesToCards();
                        });
                    }
            );
            dispatch.execute(GetEntityRenderingAction.create(getProjectId(), sel),
                    (result) -> setDisplayedEntity(Optional.of(result.getEntityData())));
        });
        setNothingSelectedVisible(!selectedEntity.isPresent());
    }

    private void retrieveAndSetDisplayedCardsForEntity(OWLEntity entity,
                                                       Runnable successCallback) {
        dispatch.execute(
                GetEntityCardDescriptorsAction.get(getProjectId(), entity),
                this,
                result -> setDisplayedCards(result, successCallback)
        );
    }

    private void setDisplayedCards(GetEntityCardDescriptorsResult result, Runnable successCallback) {
        List<CardDescriptor> nextDescriptors = result.getDescriptors();
        Set<CardId> nextCardIds = new HashSet<>();
        nextDescriptors.forEach(descriptor -> {
            nextCardIds.add(descriptor.getId());
            installPresenterForDescriptor(descriptor);
        });
        writableCardIds.clear();
        writableCardIds.addAll(result.getWritableCards());
        setDisplayedCards(nextCardIds);
        updateButtonVisibility();
        successCallback.run();
    }

    private void setDisplayedCards(Set<CardId> nextCardIds) {
        if (currentCardIds.equals(nextCardIds)) {
            return;
        }
        Optional<CardId> selectedTab = tabBarPresenter.getSelectedTab();
        currentCardIds.clear();
        tabBarPresenter.clear();
        currentCardIds.addAll(nextCardIds);
        nextCardIds.forEach(cardId -> {
            EntityCardComponents components = cardComponents.get(cardId);
            if (components != null) {
                EntityCardPresenter presenter = components.getPresenter();
                EntityCardUi entityCardUi = components.getUi();
                CardDescriptor cardDescriptor = components.getDescriptor();
                TabPresenter<CardId> tabPresenter = tabBarPresenter.addTab(cardId,
                        cardDescriptor.getLabel(),
                        cardDescriptor.getColor().orElse(null),
                        cardDescriptor.getBackgroundColor().orElse(null),
                        entityCardUi);
                entityCardUi.addAttachHandler(event -> {
                    boolean attached = event.isAttached();
                    if (attached) {
                        transmitSelectionToCard(presenter);
                    }
                });
                tabPresenter.getView().addStyleName(WebProtegeClientBundle.BUNDLE.style().entityCardStack__tabBar__tab());
                if (presenter.isEditor() && writableCardIds.contains(cardId)) {
                    tabPresenter.getView().addStyleName(WebProtegeClientBundle.BUNDLE.style().entityCardStack__tabBar__tabWritable());
                    entityCardUi.setWritable(true);
                } else {
                    tabPresenter.getView().addStyleName(WebProtegeClientBundle.BUNDLE.style().entityCardStack__tabBar__tabReadOnly());
                    entityCardUi.setWritable(false);
                }

                presenter.addDirtyChangedHandler(evt -> {
                    boolean dirty = presenter.isDirty();
                    tabPresenter.setDirty(dirty);
                });
            }
        });
        boolean canSel = selectedTab.map(nextCardIds::contains).orElse(false);
        if (canSel) {
            tabBarPresenter.restoreSelection();
        } else {
            tabBarPresenter.setFirstTabSelected();
        }

    }

    private void installPresenterForDescriptor(CardDescriptor descriptor) {
        EntityCardComponents existingComponents = cardComponents.get(descriptor.getId());
        if (existingComponents != null) {
            return;
        }
        Optional<? extends EntityCardPresenter> p = entityCardPresenterFactory.create(descriptor);
        p.ifPresent(pp -> {
            if (pp instanceof EntityCardEditorPresenter) {
                ((EntityCardEditorPresenter) pp).setParentDisplayContextBuilder(this);
            }
            EntityCardUi ui = entityCardViewProvider.get();
            view.addView(ui);
            eventBus.ifPresent(webProtegeEventBus -> pp.start(ui, webProtegeEventBus));
            EntityCardComponents components = EntityCardComponents.get(descriptor, pp, ui);
            EntityCardPresenter presenter = components.getPresenter();
            cardComponents.put(descriptor.getId(), components);
            if (presenter instanceof EntityCardEditorPresenter) {
                ((EntityCardEditorPresenter) presenter).setParentDisplayContextBuilder(this);
            }
        });
    }

    @Override
    protected void handleReloadRequest() {
        // Handle outstanding edits
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if (isDirty()) {
            // NO - Cancel
            messageBox.showConfirmBox(MessageStyle.QUESTION,
                    messages.editing_saveChangesPrompt_title(), messages.editing_saveChangesPrompt_message(),
                    DialogButton.NO,
                    this::handleCancelEditing,
                    DialogButton.YES,
                    this::commitChangesAndUpdateSelection,
                    DialogButton.YES);
        } else {
            displayCardsForSelectedEntity();
        }
    }

    private void commitChangesAndUpdateSelection() {
        // Proceed as normal
        commitChanges(this::displayCardsForSelectedEntity);
    }


    private void transmitEntitySelectionToDisplayedCards() {
        getCurrentPresenters()
                .forEach(this::transmitSelectionToCard);

    }

    private void transmitSelectionToCard(EntityCardPresenter p) {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();
        if (selectedEntity.isPresent()) {
            p.setEntity(selectedEntity.get());
        } else {
            p.clearEntity();
        }
    }

    private void transmitCapabilitiesToCards(){
        getCurrentPresenters()
                .forEach(presenter -> presenter.setCapabilities(this.currentCapabilities));
    }

    private Optional<EntityCardPresenter> getSelectedCardPresenter() {
        return tabBarPresenter.getSelectedTab()
                .map(cardComponents::get)
                .filter(Objects::nonNull)
                .map(EntityCardComponents::getPresenter);
    }


    private void handleStartEditing() {
        view.setEditModeActive(true);
        view.setEditButtonVisible(false);
        view.setCancelButtonVisible(true);
        view.setApplyButtonVisible(true);
        processCurrentCards(card -> {
            if (card instanceof EntityCardEditorPresenter) {
                ((EntityCardEditorPresenter) card).beginEditing();
            }
        });
        getSelectedCardPresenter().ifPresent(EntityCardPresenter::requestFocus);
    }

    private void handleCancelEditing() {
        view.setEditModeActive(false);
        dispatch.beginBatch();
        processCurrentCards(card -> {
            if (card instanceof EntityCardEditorPresenter) {
                ((EntityCardEditorPresenter) card).cancelEditing();
            }
        });
        dispatch.executeCurrentBatch();
        updateButtonVisibility();
        displayCardsForSelectedEntity();
    }

    private void handleFinishEditing() {
        commitChanges(this::updateButtonVisibility);
    }

    private void commitChanges(Runnable commitFinished) {
        view.setEditModeActive(false);
        commitChangesWorkflow.run(getCurrentPresenters().collect(Collectors.toList()),
                commitFinished);
    }

    private Stream<EntityCardPresenter> getCurrentPresenters() {
        return currentCardIds.stream()
                .map(cardComponents::get)
                .filter(Objects::nonNull)
                .map(EntityCardComponents::getPresenter);
    }

    private void processCurrentCards(Consumer<EntityCardPresenter> consumer) {
        getCurrentPresenters().forEach(consumer);
    }


    private void updateButtonVisibility() {
        view.setCancelButtonVisible(false);
        boolean containsAtLeastOneWritableCard = !writableCardIds.isEmpty();
        view.setEditButtonVisible(containsAtLeastOneWritableCard);
        view.setApplyButtonVisible(false);
        view.setButtonBarVisible(containsAtLeastOneWritableCard);
    }

    public boolean isDirty() {
        return getCurrentPresenters().anyMatch(EntityCardPresenter::isDirty);
    }

    @Override
    public void fillDisplayContext(DisplayContextBuilder displayContextBuilder) {
        super.fillDisplayContext(displayContextBuilder);
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        logger.info("CardStackPortletPresenter: set parent displaycontextbuilder" + super.fillDisplayContextBuilder());
        super.setParentDisplayContextBuilder(parent);
    }
}

