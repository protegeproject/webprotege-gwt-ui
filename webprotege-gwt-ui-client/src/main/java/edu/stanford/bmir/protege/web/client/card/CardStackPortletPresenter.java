package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.tab.SelectedTabIdStash;
import edu.stanford.bmir.protege.web.client.tab.TabBarPresenter;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.card.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

@Portlet(id = "webprotege.card",
        title = "Card view",
        tooltip = "Presents a stack of cards that display information about the selected entity.")
public class CardStackPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final EntityCardPresenterFactory entityCardPresenterFactory;

    private Set<CardId> currentCardIds = new HashSet<>();

    private Map<CardId, EntityCardPresenter> cardPresenters = new LinkedHashMap<>();

    private Map<CardId, CardDescriptor> cardDescriptors = new LinkedHashMap<>();

    private Map<CardId, EntityCardContainer> cardContainers = new LinkedHashMap<>();

    private final CardStackPortletView view;

    @Nonnull
    private final Provider<EntityCardContainer> entityCardViewProvider;

    private final TabBarPresenter<CardId> tabBarPresenter;

    private final DispatchServiceManager dispatch;

    private Optional<WebProtegeEventBus> eventBus = Optional.empty();

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final CardWorkflow cardWorkflow;

    @Inject
    public CardStackPortletPresenter(@Nonnull ProjectId projectId,
                                     @Nonnull SelectionModel selectionModel,
                                     @Nonnull DisplayNameRenderer displayNameRenderer,
                                     @Nonnull DispatchServiceManager dispatch, CardStackPortletView view,
                                     @Nonnull EntityCardPresenterFactory entityCardPresenterFactory,
                                     @Nonnull Provider<EntityCardContainer> entityCardViewProvider,
                                     @Nonnull TabBarPresenter<CardId> tabBarPresenter,
                                     LoggedInUserProjectPermissionChecker permissionChecker,
                                     CardWorkflow cardWorkflow) {
        super(selectionModel, projectId, displayNameRenderer, dispatch);
        this.view = view;
        this.entityCardPresenterFactory = entityCardPresenterFactory;
        this.entityCardViewProvider = entityCardViewProvider;
        this.tabBarPresenter = tabBarPresenter;
        this.dispatch = dispatch;
        this.permissionChecker = permissionChecker;
        this.cardWorkflow = cardWorkflow;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.eventBus = Optional.of(eventBus);
        portletUi.setWidget(view);
        tabBarPresenter.setSelectedKeyStash(new SelectedTabIdStash<>(portletUi, new CardIdSerializer()));
        tabBarPresenter.start(view.getTabBarContainer());
        tabBarPresenter.setSelectedTabChangedHandler(this::handleSelectedTabChanged);
        view.setEnterEditModeHandler(this::handleStartEdits);
        view.setApplyEditsHandler(this::handleApplyEdits);
        view.setCancelEditsHandler(this::handleCancelEdits);
        updateCards();
        updateButtonStates();
    }

    private void handleStartEdits() {
        view.setEditButtonVisible(false);
        view.setCancelEditsButtonVisible(true);
        view.setApplyEditsButtonVisible(true);
        getSelectedCardPresenter().ifPresent(entityCardPresenter -> {
            entityCardPresenter.setEditable(true);
            entityCardPresenter.requestFocus();
        });
    }

    private void updateCards() {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();
        selectedEntity.ifPresent(this::updateCardsForEntity);
        setNothingSelectedVisible(!selectedEntity.isPresent());
    }

    private void updateCardsForEntity(OWLEntity sel) {
        dispatch.execute(GetEntityCardDescriptorsAction.get(getProjectId(), sel), this, this::setCardDescriptors);
    }

    private void setCardDescriptors(GetEntityCardDescriptorsResult result) {
        List<CardDescriptor> nextDescriptors = result.getDescriptors();
        Set<CardId> nextCardIds = new HashSet<>();
        nextDescriptors.forEach(descriptor -> {
            nextCardIds.add(descriptor.getId());
            installPresenterForDescriptor(descriptor);
        });
        setDisplayedCards(nextCardIds);
    }

    private void setDisplayedCards(Set<CardId> nextCardIds) {
         nextCardIds.forEach(cardId -> {
            EntityCardPresenter presenter = cardPresenters.get(cardId);
            presenter.setEditable(false);
        });
        if (currentCardIds.equals(nextCardIds)) {
            return;
        }
        Optional<CardId> selectedTab = tabBarPresenter.getSelectedTab();
        currentCardIds.clear();
        tabBarPresenter.clear();
        currentCardIds.addAll(nextCardIds);
        nextCardIds.forEach(cardId -> {
            EntityCardContainer cardContainer = cardContainers.get(cardId);
            EntityCardPresenter cardPresenter = cardPresenters.get(cardId);
            tabBarPresenter.addTab(cardId, cardPresenter.getLabel(), cardPresenter.getColor().orElse(null),
                    cardPresenter.getBackgroundColor().orElse(null),
                    cardContainer);

        });
        boolean canSel = selectedTab.map(nextCardIds::contains).orElse(false);
        if(canSel) {
            tabBarPresenter.restoreSelection();
        }
        else {
            tabBarPresenter.setFirstTabSelected();
        }

    }

    private void installPresenterForDescriptor(CardDescriptor descriptor) {
        cardDescriptors.put(descriptor.getId(), descriptor);
        EntityCardPresenter presenter = cardPresenters.get(descriptor.getId());
        if (presenter != null) {
            return;
        }
        presenter = entityCardPresenterFactory.create(descriptor);

        EntityCardContainer cardContainer = entityCardViewProvider.get();
        cardContainers.put(descriptor.getId(), cardContainer);
        view.addView(cardContainer);
        cardPresenters.put(descriptor.getId(), presenter);
        if(eventBus.isPresent()) {
            presenter.start(cardContainer, eventBus.get());
        }
    }

    @Override
    protected void handleReloadRequest() {
        // Handle outstanding edits
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        updateCards();
        updateButtonStates();
    }

    private void handleSelectedTabChanged() {
        // Handle
        updateButtonStates();
    }

    @Override
    protected void handleBeforeSetEntity(Optional<? extends OWLEntity> existingEntity) {
        // Any outstanding edits?  If so, handle outstanding edits

    }

    private Optional<EntityCardPresenter> getSelectedCardPresenter() {
        return tabBarPresenter.getSelectedTab()
                .flatMap(cardId -> Optional.ofNullable(cardPresenters.getOrDefault(cardId, null)));
    }

    private void handleApplyEdits() {
        dispatch.beginBatch();
        getSelectedCardPresenter().ifPresent(p -> {
            p.commitChanges();
            p.setEditable(false);
        });
        dispatch.executeCurrentBatch();
        updateButtonStates();
    }

    private void handleCancelEdits() {
        dispatch.beginBatch();

        getSelectedCardPresenter().ifPresent(p -> {
            p.cancelChanges();
            p.setEditable(false);
        });
        dispatch.executeCurrentBatch();
        updateButtonStates();
    }

    private void updateButtonStates() {
        view.setCancelEditsButtonVisible(false);
        view.setEditButtonVisible(false);
        view.setApplyEditsButtonVisible(false);

        // Is the card an editor
        boolean editor = getSelectedCardPresenter().map(EntityCardPresenter::isEditor).orElse(false);
        if(!editor) {
            view.setButtonBarVisible(false);
            return;
        }
        view.setButtonBarVisible(true);
        // Is the card content actually editable?
        Optional<CardId> selectedTab = tabBarPresenter.getSelectedTab();
        selectedTab.ifPresent(cardId -> {
            CardDescriptor cardDescriptor = cardDescriptors.get(cardId);
            if (cardDescriptor != null) {
                Set<ActionId> requiredWriteActions = cardDescriptor.getRequiredWriteActions();
                if(requiredWriteActions.isEmpty()) {
                    view.setEditButtonVisible(true);
                }
                else {
                    permissionChecker.hasPermissions(requiredWriteActions, view::setEditButtonVisible);
                }
            }
        });

    }
}

