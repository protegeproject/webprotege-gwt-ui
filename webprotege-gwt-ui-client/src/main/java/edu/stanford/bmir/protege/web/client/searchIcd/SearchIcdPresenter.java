package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.search.SearchResultChosenHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class SearchIcdPresenter implements HasInitialFocusable {

    private static final int SEARCH_DELAY_MILLIS = 900;

    private static final int PAGE_CHANGE_DELAY_MILLIS = 250;

    private final ProjectId projectId;

    private final SearchIcdView view;

    private final Set<EntityType<?>> entityTypes = new HashSet<>();

    private final DispatchServiceManager dispatchServiceManager;

    private final Timer searchTimer = new Timer() {
        @Override
        public void run() {
            performSearch();
        }
    };

    private final Timer pageChangeTimer = new Timer() {
        @Override
        public void run() {
            performSearch();
        }
    };


    private SearchResultChosenHandler searchResultChosenHandler;

    private AcceptKeyHandler acceptKeyHandler = () -> {};



    @Inject
    public SearchIcdPresenter(@Nonnull ProjectId projectId,
                              @Nonnull SearchIcdView view,
                              @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void start() {
        view.setSearchStringChangedHandler(() -> {
//            triggerSearch(view.getSearchString());
            restartSearchTimer();
        });
        view.setAcceptKeyHandler(this::handleAcceptKey);
//        Map<String, Object> opts = new HashMap<>();
//
//        opts.put("apiServerUrl", "https://icd11restapi-developer-test.azurewebsites.net");
//        ECT.configure(opts);
    }

    public void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler) {
        this.acceptKeyHandler = checkNotNull(acceptKeyHandler);
    }

    private void handleAcceptKey() {
        this.acceptKeyHandler.handleAcceptKey();
    }


    private void restartSearchTimer() {
        searchTimer.cancel();
        searchTimer.schedule(SEARCH_DELAY_MILLIS);
    }

    private void restartPageChangeTimer() {
        pageChangeTimer.cancel();
        pageChangeTimer.schedule(PAGE_CHANGE_DELAY_MILLIS);
    }

    public void setSearchResultChosenHandler(SearchResultChosenHandler handler) {
        searchResultChosenHandler = checkNotNull(handler);
    }

    public IsWidget getView() {
        return view;
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    public void setEntityTypes(EntityType<?> ... entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(Arrays.asList(entityTypes));
    }

    private void performSearch() {
//        triggerSearch(view.getSearchString());
    }

    private void displaySearchResult(PerformEntitySearchResult result) {
        if(!view.getSearchString().equals(result.getSearchString())) {
            return;
        }
    }

    public native void triggerSearch(String query) /*-{
    $wnd.alert("s-a facut search");
        $wnd.ECT.Handler.search("1", query);
    }-*/;


}
