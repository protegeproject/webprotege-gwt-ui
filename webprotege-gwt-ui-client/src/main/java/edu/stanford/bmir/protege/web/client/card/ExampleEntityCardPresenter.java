package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * An example implementation of an {@link EntityCardPresenter}.  Entity card presenters
 * implement {@link CustomContentEntityCardPresenter}.  If editing support is required
 * then {@link EntityCardEditorPresenter} should also be implemented.  Finally, entity
 * card presenters should be annotated with @{@link Card} where the id for the card content
 * is specified.
 *
 * For this example, we just display the IRI of the selected entity and the length of the IRI.
 */
@Card(id = "example.card")
public class ExampleEntityCardPresenter implements CustomContentEntityCardPresenter {

    private final ExampleEntityCardView view;

    @Inject
    public ExampleEntityCardPresenter(ExampleEntityCardView view) {
        this.view = view;
    }

    @Override
    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {
        ui.setWidget(view);
    }

    @Override
    public void requestFocus() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setEntity(OWLEntity entity) {
        view.setIri(entity.getIRI().toString());
    }

    @Override
    public void clearEntity() {
        view.setIri("");
    }

    @Override
    public boolean isDirty() {
        // We are never dirty because we don't edit anything
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        // We don't change the dirty state so just return an empty HandlerRegistration.
        return () -> {};
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {

    }
}
