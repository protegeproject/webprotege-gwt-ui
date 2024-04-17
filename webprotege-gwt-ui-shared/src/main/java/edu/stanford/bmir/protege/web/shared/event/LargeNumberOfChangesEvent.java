package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
@JsonTypeName("webprotege.events.projects.LargeNumberOfChanges")
public class LargeNumberOfChangesEvent extends ProjectEvent<LargeNumberOfChangesHandler> {

    public static final Event.Type<LargeNumberOfChangesHandler> LARGE_NUMBER_OF_CHANGES = new Event.Type<>();

    public LargeNumberOfChangesEvent(ProjectId source) {
        super(source);
    }

    @GwtSerializationConstructor
    private LargeNumberOfChangesEvent() {
    }

    @Override
    public Event.Type<LargeNumberOfChangesHandler> getAssociatedType() {
        return LARGE_NUMBER_OF_CHANGES;
    }

    @Override
    protected void dispatch(LargeNumberOfChangesHandler handler) {
        handler.handleLargeNumberOfChanges(this);
    }

    @Override
    public int hashCode() {
        return getProjectId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof LargeNumberOfChangesEvent)) {
            return false;
        }
        LargeNumberOfChangesEvent other = (LargeNumberOfChangesEvent) obj;
        return this.getProjectId().equals(other.getProjectId());
    }
}
