package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

@Card(id = "Hello.World")
public class EntityIriCardPresenter implements CustomContentEntityCardPresenter, EntityCardEditorPresenter {

    private final EntityIriCardViewImpl view;

    @Inject
    public EntityIriCardPresenter(EntityIriCardViewImpl view) {
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
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return () -> {};
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {

    }

    @Override
    public void beginEditing() {

    }

    @Override
    public void cancelEditing() {

    }

    @Override
    public void finishEditing(String commitMessage) {

    }
}
