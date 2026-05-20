package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import edu.stanford.bmir.protege.web.shared.event.EventId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
@JsonTypeName("webprotege.events.watches.WatchAdded")
public class WatchAddedEvent extends ProjectEvent<WatchAddedHandler> {

    public static final transient Event.Type<WatchAddedHandler> ON_WATCH_ADDED = new Event.Type<>();

    private EventId eventId;

    private Watch watch;

    /**
     * For serialization only.
     */
    private WatchAddedEvent() {
    }

    @JsonCreator
    public WatchAddedEvent(@JsonProperty("eventId") EventId eventId,
                           @JsonProperty("projectId") ProjectId source,
                           @JsonProperty("watch") Watch watch) {
        super(source);
        this.eventId = eventId;
        this.watch = watch;
    }

    @JsonProperty("eventId")
    public EventId getEventId() {
        return eventId;
    }

    public Watch getWatch() {
        return watch;
    }

    @JsonIgnore
    public UserId getUserId() {
        return watch.getUserId();
    }


    @JsonIgnore
    @Override
    public Event.Type<WatchAddedHandler> getAssociatedType() {
        return ON_WATCH_ADDED;
    }

    @Override
    protected void dispatch(WatchAddedHandler handler) {
        handler.handleWatchAdded(this);
    }


    @Override
    public String toString() {
        return toStringHelper("WatchAddedEvent")
                .addValue(getProjectId())
                .addValue(getWatch())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WatchAddedEvent)) {
            return false;
        }
        WatchAddedEvent that = (WatchAddedEvent) o;
        return Objects.equals(watch, that.watch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(watch);
    }
}
