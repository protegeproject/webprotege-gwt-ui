package edu.stanford.bmir.protege.web.shared.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.EventId;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
@JsonTypeName("webprotege.events.projects.DisplayNameSettingsChanged")
public class DisplayNameSettingsChangedEvent extends ProjectEvent<DisplayNameSettingsChangedHandler> {

    public static final transient Event.Type<DisplayNameSettingsChangedHandler> ON_DISPLAY_LANGUAGE_CHANGED = new Event.Type<>();

    private EventId eventId;

    @Nonnull
    private final DisplayNameSettings displayNameSettings;

    private DisplayNameSettingsChangedEvent(EventId eventId,
                                            @Nonnull ProjectId source,
                                            @Nonnull DisplayNameSettings displayNameSettings) {
        super(source);
        this.eventId = eventId;
        this.displayNameSettings = checkNotNull(displayNameSettings);
    }

    @JsonCreator
    public static DisplayNameSettingsChangedEvent get(@JsonProperty("eventId") EventId eventId,
                                                      @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                      @JsonProperty("displayNameSettings") @Nonnull DisplayNameSettings displayNameSettings) {
        return new DisplayNameSettingsChangedEvent(eventId, projectId, displayNameSettings);
    }

    @JsonProperty("eventId")
    public EventId getEventId() {
        return eventId;
    }

    @Nonnull
    public static Event.Type<DisplayNameSettingsChangedHandler> getType() {
        return ON_DISPLAY_LANGUAGE_CHANGED;
    }

    @JsonIgnore
    @Override
    public Event.Type<DisplayNameSettingsChangedHandler> getAssociatedType() {
        return ON_DISPLAY_LANGUAGE_CHANGED;
    }

    @Override
    protected void dispatch(DisplayNameSettingsChangedHandler handler) {
        handler.handleDisplayNameSettingsChanged(this);
    }

    @Nonnull
    public DisplayNameSettings getDisplayNameSettings() {
        return displayNameSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisplayNameSettingsChangedEvent)) {
            return false;
        }
        DisplayNameSettingsChangedEvent that = (DisplayNameSettingsChangedEvent) o;
        return displayNameSettings.equals(that.displayNameSettings)
                && getProjectId().equals(that.getProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayNameSettings, getProjectId());
    }
}
