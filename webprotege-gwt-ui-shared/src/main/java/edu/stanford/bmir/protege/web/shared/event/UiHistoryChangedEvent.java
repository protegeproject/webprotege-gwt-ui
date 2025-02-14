package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Set;

@JsonTypeName("webprotege.events.projects.uiHistory.UpdateUiHistoryEvent")
public class UiHistoryChangedEvent extends ProjectEvent<UiHistoryChangedHandler> {

    public static final Event.Type<UiHistoryChangedHandler> UI_HISTORY_CHANGED = new Event.Type<>();

    private Set<String> afectedEntityIris;

    /**
     * For serialization purposes only.
     */
    private UiHistoryChangedEvent() {
    }

    public UiHistoryChangedEvent(ProjectId source, Set<String> afectedEntityIris) {
        super(source);
        this.afectedEntityIris = afectedEntityIris;
    }


    @Override
    public Event.Type<UiHistoryChangedHandler> getAssociatedType() {
        return UI_HISTORY_CHANGED;
    }

    @Override
    protected void dispatch(UiHistoryChangedHandler handler) {
        handler.handleUiHistoryChanged(this);
    }


    @Override
    public int hashCode() {
        return "UiHistoryChangedEvent".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UiHistoryChangedEvent)) {
            return false;
        }
        UiHistoryChangedEvent other = (UiHistoryChangedEvent) obj;
        return this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("UiHistoryChangedEvent")
                .add("projectId", getProjectId()).toString();
    }

    public Set<String> getAfectedEntityIris() {
        return afectedEntityIris;
    }
}