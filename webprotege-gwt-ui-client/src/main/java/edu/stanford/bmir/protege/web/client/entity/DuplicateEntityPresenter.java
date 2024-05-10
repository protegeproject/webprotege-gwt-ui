package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.search.EntitySearchFilterTokenFieldPresenter;
import edu.stanford.bmir.protege.web.client.search.HierarchyPopupElementSelectionHandler;
import edu.stanford.bmir.protege.web.client.search.SearchResultChosenHandler;
import edu.stanford.bmir.protege.web.client.search.SearchResultsListPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class DuplicateEntityPresenter {


    private final ProjectId projectId;

    @Nonnull
    private final SearchResultsListPresenter searchResultsPresenter;

    private final Set<EntityType<?>> entityTypes = new HashSet<>();

    private final EntitySearchFilterTokenFieldPresenter entitySearchFilterTokenFieldPresenter;

    private final DispatchServiceManager dispatchServiceManager;

    private DuplicateEntitiesView view;


    private String langTag = "";

    private HierarchyPopupElementSelectionHandler hierarchySelectionHandler = selection -> {};

    private final static Logger logger = Logger.getLogger(DuplicateEntityPresenter.class.getName());


    @Inject
    public DuplicateEntityPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull SearchResultsListPresenter searchResultsPresenter,
                                    @Nonnull DuplicateEntitiesView view,
                                    @Nonnull EntitySearchFilterTokenFieldPresenter entitySearchFilterTokenFieldPresenter,
                                    @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.searchResultsPresenter = searchResultsPresenter;
        this.view = view;
        this.entitySearchFilterTokenFieldPresenter = checkNotNull(entitySearchFilterTokenFieldPresenter);
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        hideSearchDuplicatesPanel();
        searchResultsPresenter.setHierarchySelectionHandler(hierarchySelectionHandler);
        searchResultsPresenter.start(this.view.getDuplicateResultsContainer());
    }

    public void setEntityTypes(EntityType<?>... entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(Arrays.asList(entityTypes));
    }


    private void performDuplicateSearch(String entitiesText) {
        if (entitiesText.length() < 1) {
            hideSearchDuplicatesPanel();
            searchResultsPresenter.clearSearchResults();
            return;
        }
        int pageNumber = searchResultsPresenter.getPageNumber();
        ImmutableList<EntitySearchFilter> searchFilters = entitySearchFilterTokenFieldPresenter.getSearchFilters();

        dispatchServiceManager.execute(PerformEntitySearchAction.create(projectId,
                        entitiesText,
                        entityTypes,
                        getLangTagFilter(),
                        searchFilters,
                        PageRequest.requestPage(pageNumber)),
                view,
                this::processSearchActionResult);

    }

    private LangTagFilter getLangTagFilter() {
        return LangTagFilter.get(ImmutableSet.of(LangTag.get(this.langTag)));
    }

    public void handleEntitiesStringChanged(String value) {
        this.performDuplicateSearch(value);
    }


    public void setLangTag(String langTag) {
        this.langTag = langTag;
    }

    private void processSearchActionResult(PerformEntitySearchResult result) {
        if(result.getResults() == null || result.getResults().getTotalElements() == 0){
            hideSearchDuplicatesPanel();
            return;
        }

        this.view.asWidget().setVisible(true);

        this.searchResultsPresenter.displaySearchResult(result.getResults());
    }

    private void hideSearchDuplicatesPanel(){
        this.view.asWidget().setVisible(false);
    }

    public Optional<OWLEntityData> getSelectedSearchResult() {
        return searchResultsPresenter.getSelectedSearchResult();
    }

    public void setHierarchySelectionHandler(HierarchyPopupElementSelectionHandler handler){
        this.hierarchySelectionHandler = handler;
    }

}
