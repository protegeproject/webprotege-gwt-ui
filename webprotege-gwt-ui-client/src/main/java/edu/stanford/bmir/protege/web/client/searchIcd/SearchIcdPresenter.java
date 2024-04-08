package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
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


    private SearchIcdResultChosenHandler searchResultChosenHandler;

    private AcceptKeyHandler acceptKeyHandler = () -> {
    };


    @Inject
    public SearchIcdPresenter(@Nonnull ProjectId projectId,
                              @Nonnull SearchIcdView view) {
        this.projectId = projectId;
        this.view = view;
    }

    public void start() {
        view.setAcceptKeyHandler(this::handleAcceptKey);
    }

    public void setSubTreeFilter(Optional<EntityNode> selectedOption) {

        selectedOption.ifPresent((selectedOptPres) -> this.view.setSubtreeFilterText(selectedOptPres));
    }

    public void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler) {
        this.acceptKeyHandler = checkNotNull(acceptKeyHandler);
    }

    private void handleAcceptKey() {
        this.acceptKeyHandler.handleAcceptKey();
    }

    public void setSearchResultChosenHandler(SearchIcdResultChosenHandler handler) {
        searchResultChosenHandler = checkNotNull(handler);
    }

    public IsWidget getView() {
        return view;
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    public void setEntityTypes(EntityType<?>... entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(Arrays.asList(entityTypes));
    }


    //ToDo see how to get entityType
    @Nonnull
    public Optional<OWLEntity> getSelectedSearchResult() {
        return Optional.of(DataFactory.getOWLEntity(EntityType.CLASS, view.getSelectedURI()));
    }
}
