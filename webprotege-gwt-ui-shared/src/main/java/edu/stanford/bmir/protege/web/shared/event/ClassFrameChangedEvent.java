package edu.stanford.bmir.protege.web.shared.event;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
@JsonTypeName("webprotege.events.frames.ClassFrameChanged")
public class ClassFrameChangedEvent extends EntityFrameChangedEvent<OWLClass, ClassFrameChangedEventHandler> {

    @JsonIgnore
    public transient static final Event.Type<ClassFrameChangedEventHandler> CLASS_FRAME_CHANGED = new Event.Type<ClassFrameChangedEventHandler>();

    public ClassFrameChangedEvent(OWLClass entity, ProjectId projectId, UserId userId) {
        super(entity, projectId, userId);
    }

    private ClassFrameChangedEvent() {
        super();
    }

    /**
     * Returns the {@link com.google.web.bindery.event.shared.Event.Type} used to register this event, allowing an
     * {@link com.google.web.bindery.event.shared.EventBus} to find handlers of the appropriate class.
     * @return the type
     */
    @JsonIgnore
    @Override
    public Event.Type<ClassFrameChangedEventHandler> getAssociatedType() {
        return CLASS_FRAME_CHANGED;
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
    protected void dispatch(ClassFrameChangedEventHandler handler) {
        handler.classFrameChanged(this);
    }

    @Override
    public int hashCode() {
        return "ClassFrameChangedEvent".hashCode() + getEntity().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ClassFrameChangedEvent)) {
            return false;
        }
        ClassFrameChangedEvent other = (ClassFrameChangedEvent) obj;
        return this.getEntity().equals(other.getEntity());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ClassFrameChangedEvent")
                          .addValue(getEntity()).toString();
    }
}
