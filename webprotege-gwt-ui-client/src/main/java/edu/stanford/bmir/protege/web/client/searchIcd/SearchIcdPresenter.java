package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
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

    private final SearchIcdView view;

    private final Set<EntityType<?>> entityTypes = new HashSet<>();


    private SearchIcdResultChosenHandler searchResultChosenHandler;

    private final SearchInputManager searchInputManager;


    @Inject
    public SearchIcdPresenter(@Nonnull SearchIcdView view,
                              @Nonnull SearchInputManager searchInputManager) {
        this.view = view;
        this.searchInputManager = searchInputManager;
    }

    public void start() {
        view.setInputFieldValue(searchInputManager.getSearchInputText());

        view.setSearchStringChangedHandler(() -> searchInputManager.setSearchInputText(view.getInputFieldValue()));
    }

    public void setSubTreeFilter(Optional<EntityNode> selectedOption) {

        selectedOption.ifPresent(this.view::setSubtreeFilterText);
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
