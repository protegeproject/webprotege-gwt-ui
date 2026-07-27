package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
@JsonTypeName("webprotege.events.projects.LargeNumberOfChanges")
public class LargeNumberOfChangesEvent extends ProjectEvent<LargeNumberOfChangesHandler> {

    public static final Event.Type<LargeNumberOfChangesHandler> LARGE_NUMBER_OF_CHANGES = new Event.Type<>();

    private EventId eventId;

    @JsonCreator
    public LargeNumberOfChangesEvent(@JsonProperty("eventId") EventId eventId,
                                     @JsonProperty("projectId") ProjectId source) {
        super(source);
        this.eventId = eventId;
    }

    @GwtSerializationConstructor
    private LargeNumberOfChangesEvent() {
    }

    @JsonProperty("eventId")
    public EventId getEventId() {
        return eventId;
    }

    @JsonIgnore
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
        return Objects.hash(getProjectId(), eventId);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof LargeNumberOfChangesEvent)) {
            return false;
        }
        // The event id must take part: handled-event sets use equality to drop
        // the same signal arriving over both delivery paths, and comparing the
        // project alone would also swallow every LATER signal for the project.
        LargeNumberOfChangesEvent other = (LargeNumberOfChangesEvent) obj;
        return this.getProjectId().equals(other.getProjectId())
                && Objects.equals(this.eventId, other.eventId);
    }
}
