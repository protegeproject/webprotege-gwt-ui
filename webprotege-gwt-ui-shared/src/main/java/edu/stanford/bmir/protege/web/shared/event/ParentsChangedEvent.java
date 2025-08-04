package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

@JsonTypeName("webprotege.events.projects.ui.ParentsChanged")
public class ParentsChangedEvent extends ProjectEvent<ParentsChangedHandler> {

    public static final transient Event.Type<ParentsChangedHandler> ON_PARENTS_UPDATED = new Event.Type<>();

    private IRI entityIri;
    private ParentsChangedEvent() {
    }

    public ParentsChangedEvent(ProjectId source, IRI entityIri) {
        super(source);
        this.entityIri = entityIri;
    }

    public IRI getEntityIri() {
        return entityIri;
    }

    @Override
    public Event.Type<ParentsChangedHandler> getAssociatedType() {
        return ON_PARENTS_UPDATED;
    }

    @Override
    protected void dispatch(ParentsChangedHandler handler) {
        handler.handleParentsChanged(this);
    }
}