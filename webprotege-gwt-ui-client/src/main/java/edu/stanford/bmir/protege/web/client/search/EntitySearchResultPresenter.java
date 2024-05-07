package edu.stanford.bmir.protege.web.client.search;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenter;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.OboId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-24
 */
public class EntitySearchResultPresenter {

    @Nonnull
    private final EntitySearchResult result;

    @Nonnull
    private final EntitySearchResultView view;

    private final Provider<SearchResultMatchPresenter> matchPresenterProvider;

    @Nonnull
    private final HierarchyPopupPresenter hierarchyPopupPresenter;

    private WebProtegeEventBus eventBus;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;

    private HierarchyPopupElementSelectionHandler hierarchySelectionHandler = (selection) -> {};


    @AutoFactory
    public EntitySearchResultPresenter(
            @Nonnull EntitySearchResult result,
            @Provided @Nonnull EntitySearchResultView view,
            @Provided @Nonnull Provider<SearchResultMatchPresenter> matchPresenterProvider,
            @Nonnull HierarchyPopupPresenter HierarchyPopupPresenter,
            @Provided @Nonnull WebProtegeEventBus eventBus,
            @Provided @Nonnull DisplayNameSettingsManager displayNameSettingsManager) {
        this.result = checkNotNull(result);
        this.view = checkNotNull(view);
        this.matchPresenterProvider = checkNotNull(matchPresenterProvider);
        this.hierarchyPopupPresenter = checkNotNull(HierarchyPopupPresenter);
        this.eventBus = eventBus;
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
    }

    public void start() {
        ImmutableList<SearchResultMatchView> views = result.getMatches()
                .stream()
                .map(resultMatch -> {
                    SearchResultMatchPresenter presenter = matchPresenterProvider.get();
                    presenter.setSearchResultMatch(resultMatch);
                    return presenter;
                })
                .map(SearchResultMatchPresenter::getView)
                .collect(toImmutableList());
        EntityNode entity = result.getEntity();
        this.view.setEntity(entity);
        this.view.setResultMatchViews(views);
        this.view.setPopUpHierarchyHandler((target -> {
            hierarchyPopupPresenter.start(eventBus);
            hierarchyPopupPresenter.setSelectedEntity(entity.getEntity());
            hierarchyPopupPresenter.show(target, hierarchySelectionHandler::handleHierarchySelection);
            hierarchyPopupPresenter.setDisplayNameSettings(displayNameSettingsManager.getLocalDisplayNameSettings());
        }));
        Optional<String> oboId = OboId.getOboId(entity.getEntity().getIRI());
        oboId.ifPresent(view::setOboId);
        if (!oboId.isPresent()) {
            view.clearOboId();
        }
    }

    @Nonnull
    public EntitySearchResultView getView() {
        return view;
    }

    @Nonnull
    public EntityNode getEntity() {
        return result.getEntity();
    }

    public void setHierarchySelectionHandler(HierarchyPopupElementSelectionHandler handler){
        this.hierarchySelectionHandler = handler;
    }
}
