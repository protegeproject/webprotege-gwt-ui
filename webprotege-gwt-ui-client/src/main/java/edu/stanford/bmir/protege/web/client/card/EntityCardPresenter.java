package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.shared.HasDirty;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;


public interface EntityCardPresenter extends HasDirty {

    void start(EntityCardUi ui, WebProtegeEventBus eventBus);

    void requestFocus();

    void stop();

    default boolean isEditor() {
        return this instanceof EntityCardEditorPresenter;
    }

    void setEntity(OWLEntity entity);

    void clearEntity();
}
