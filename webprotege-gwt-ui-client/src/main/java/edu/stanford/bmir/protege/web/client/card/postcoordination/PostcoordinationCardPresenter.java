package edu.stanford.bmir.protege.web.client.card.postcoordination;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.card.CustomContentEntityCardPresenter;
import edu.stanford.bmir.protege.web.client.card.EntityCardEditorPresenter;
import edu.stanford.bmir.protege.web.client.card.EntityCardUi;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

@Card(id = "postcoordination.card")
public class PostcoordinationCardPresenter implements CustomContentEntityCardPresenter, EntityCardEditorPresenter {


    @Override
    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {

    }

    @Override
    public void requestFocus() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setEntity(OWLEntity entity) {

    }

    @Override
    public void clearEntity() {

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

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {

    }
}
