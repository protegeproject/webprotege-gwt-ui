package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
@JsonTypeName("webprotege.events.watches.WatchRemoved")
public class WatchRemovedEvent extends ProjectEvent<WatchRemovedHandler> implements HasUserId {

    public transient static final Event.Type<WatchRemovedHandler> ON_WATCH_REMOVED = new Event.Type<WatchRemovedHandler>();

    private String eventId;

    private Watch watch;

    public WatchRemovedEvent(ProjectId source, Watch watch) {
        super(source);
        this.watch = watch;
    }

    @JsonCreator
    public WatchRemovedEvent(@JsonProperty("eventId") String eventId,
                             @JsonProperty("projectId") ProjectId source,
                             @JsonProperty("watch") Watch watch) {
        super(source);
        this.eventId = eventId;
        this.watch = watch;
    }

    private WatchRemovedEvent() {
    }

    @JsonProperty("eventId")
    public String getEventId() {
        return eventId;
    }

    @JsonIgnore
    @Override
    public Event.Type<WatchRemovedHandler> getAssociatedType() {
        return ON_WATCH_REMOVED;
    }

    @Override
    protected void dispatch(WatchRemovedHandler handler) {
        handler.handleWatchRemoved(this);
    }

    public Watch getWatch() {
        return watch;
    }

    @JsonIgnore
    public UserId getUserId() {
        return watch.getUserId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId(), watch);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof WatchRemovedEvent)) {
            return false;
        }
        WatchRemovedEvent other = (WatchRemovedEvent) obj;
        return this.getProjectId().equals(other.getProjectId()) && this.getWatch().equals(other.getWatch());
    }


    @Override
    public String toString() {
        return toStringHelper("WatchRemovedEvent")
                .addValue(getProjectId())
                .addValue(watch)
                .toString();
    }
}
