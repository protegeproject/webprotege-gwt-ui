package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */

@JsonTypeName("webprotege.events.projects.OntologyChanged")
public class OntologyFrameChangedEvent extends ProjectEvent<OntologyFrameChangedEventHandler> implements Serializable, IsSerializable {

    public static final transient Event.Type<OntologyFrameChangedEventHandler> TYPE = new Event.Type<OntologyFrameChangedEventHandler>();

    private OntologyChange ontologyChange;

    private OWLOntologyID ontologyId;

    public OntologyFrameChangedEvent(OntologyChange ontologyChange, ProjectId projectId) {
        super(projectId);
        this.ontologyChange = ontologyChange;
        this.ontologyId = ontologyChange.ontologyId();
    }

    private OntologyFrameChangedEvent() {

    }

    public void setOntologyChange(OntologyChange ontologyChange) {
        this.ontologyChange = ontologyChange;
    }

    public void setOntologyId(OWLOntologyID ontologyId) {
        this.ontologyId = ontologyId;
    }

    public OWLOntologyID getOntologyId() {
        return ontologyId;
    }

    /**
     * Returns the {@link com.google.web.bindery.event.shared.Event.Type} used to register this event, allowing an
     * {@link com.google.web.bindery.event.shared.EventBus} to find handlers of the appropriate class.
     * @return the type
     */
    @Override
    public Event.Type<OntologyFrameChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Implemented by subclasses to to invoke their handlers in a type safe
     * manner. Intended to be called by {@link com.google.web.bindery.event.shared.EventBus#fireEvent(
     *com.google.web.bindery.event.shared.Event)} or
     * {@link com.google.web.bindery.event.shared.EventBus#fireEventFromSource(com.google.web.bindery.event.shared.Event,
     * Object)}.
     * @param handler handler
     * @see com.google.web.bindery.event.shared.EventBus#dispatchEvent(com.google.web.bindery.event.shared.Event,
     *      Object)
     */
    @Override
    protected void dispatch(OntologyFrameChangedEventHandler handler) {
        handler.ontologyFrameChanged(this);
    }
}
