package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.search.EntitySearchFilterTokenFieldPresenter;
import edu.stanford.bmir.protege.web.client.search.SearchResultsListPresenter;
import edu.stanford.bmir.protege.web.shared.entity.DuplicateEntitiesUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class DuplicateEntityPresenter {


    private final ProjectId projectId;

    @Nonnull
    private final SearchResultsListPresenter searchResultsPresenter;

    private final Set<EntityType<?>> entityTypes = new HashSet<>();

    private final EntitySearchFilterTokenFieldPresenter entitySearchFilterTokenFieldPresenter;

    private final DispatchServiceManager dispatchServiceManager;

    private List<PerformEntitySearchResult> duplicates = new ArrayList<>();

    private DuplicateEntitiesView view;


    private String langTag = "";

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
        logger.info("..........................................................");
        logger.info("Suntem in start");
        logger.info("..........................................................");
        searchResultsPresenter.start(this.view.getDuplicateResultsContainer());
    }

    public void setEntityTypes(EntityType<?>... entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(Arrays.asList(entityTypes));
    }


    private void performDuplicateSearch(String entitiesText) {
        logger.info("..........................................................");
        logger.info("Suntem in performSearch: " + entitiesText);
        logger.info("..........................................................");

        GWT.log("..........................................................");
        GWT.log("Suntem in performSearch: " + entitiesText);
        GWT.log("..........................................................");


        duplicates.clear();
        if (entitiesText.length() < 1) {
            searchResultsPresenter.clearSearchResults();
            return;
        }
        int pageNumber = searchResultsPresenter.getPageNumber();
        ImmutableList<EntitySearchFilter> searchFilters = entitySearchFilterTokenFieldPresenter.getSearchFilters();

//        Set<String> entityNames = getEntities(entitiesText);
//
//        AtomicInteger searchActionRequestCount = new AtomicInteger(entityNames.size());

//        entityNames.forEach(entityName -> {
//            dispatchServiceManager.execute(PerformEntitySearchAction.create(projectId,
//                            entityName,
//                            entityTypes,
//                            getLangTagFilter(),
//                            searchFilters,
//                            PageRequest.requestPage(pageNumber)),
//                    view,
//                    result -> {
//                        logger.info("result for "+entityName+" is: " + result.getResults());
//
//                        this.processSearchActionResult(result, searchActionRequestCount);
//                        logger.info("executie pentru entityName:" + entityName);
//                    });
//        });

        dispatchServiceManager.execute(PerformEntitySearchAction.create(projectId,
                        entitiesText,
                        entityTypes,
                        getLangTagFilter(),
                        searchFilters,
                        PageRequest.requestPage(pageNumber)),
                view,
                this::processSearchActionResult);

    }

    private Set<String> getEntities(String entitiesText) {
        return Arrays.stream(entitiesText.split("\\s+")).collect(Collectors.toSet());
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

//    private void processSearchActionResult(PerformEntitySearchResult result, AtomicInteger searchActionRequestCount) {
//        if(result.getResults().getTotalElements() == 0){
//            searchActionRequestCount.decrementAndGet();
//        }
//        logger.info(result.getResults().iterator().next().getEntity().getBrowserText());
//        duplicates.add(result);
//        searchActionRequestCount.decrementAndGet();
//        logger.info("duplicates are dimensiunea:" + duplicates.size());
//        logger.info("primul element din duplicates are atatea elemente: " + duplicates.get(0).getResults().getTotalElements());
//        logger.info("primul rezultat din primul element din lista de duplicate " + duplicates.get(0).getResults().getPageElements().get(0));
//        logger.info("searchActionRequestCount: " + searchActionRequestCount);
//
//        if (searchActionRequestCount.get() == 0) {
//            populateDuplicateResultContainer();
//        }
//    }

    private void processSearchActionResult(PerformEntitySearchResult result) {

        logger.info(result.getResults().iterator().next().getEntity().getBrowserText());
        duplicates.add(result);
        logger.info("duplicates are dimensiunea:" + duplicates.size());
        logger.info("primul element din duplicates are atatea elemente: " + duplicates.get(0).getResults().getTotalElements());
        logger.info("primul rezultat din primul element din lista de duplicate " + duplicates.get(0).getResults().getPageElements().get(0));

        this.searchResultsPresenter.displaySearchResult(result.getResults());
    }

    private void populateDuplicateResultContainer() {
        if (duplicates.isEmpty()) {
            this.searchResultsPresenter.clearSearchResults();
            logger.info("lista duplicatelor. este goala");
            return;
        }
        logger.info("lista duplicatelor. element 1:" + duplicates.get(0));

        this.searchResultsPresenter.displaySearchResult(DuplicateEntitiesUtil.concatenateDuplicateSearchResults(duplicates));
    }

}
