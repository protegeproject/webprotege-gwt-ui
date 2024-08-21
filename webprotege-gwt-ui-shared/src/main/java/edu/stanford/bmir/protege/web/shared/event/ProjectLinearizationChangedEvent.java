package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@JsonTypeName("webprotege.linearization.ProjectLinearizationChangedEvent")
public class ProjectLinearizationChangedEvent extends ProjectEvent<ProjectLinearizationChangedHandler> {

    public static final Event.Type<ProjectLinearizationChangedHandler> PROJECT_LINEARIZATION_CHANGED = new Event.Type<>();

    /**
     * For serialization purposes only.
     */
    private ProjectLinearizationChangedEvent() {
    }

    public ProjectLinearizationChangedEvent(ProjectId source) {
        super(source);
    }


    @Override
    public Event.Type<ProjectLinearizationChangedHandler> getAssociatedType() {
        return PROJECT_LINEARIZATION_CHANGED;
    }

    @Override
    protected void dispatch(ProjectLinearizationChangedHandler handler) {
        handler.handleLinearizationChanged(this);
    }


    @Override
    public int hashCode() {
        return "ProjectLinearizationChangedEvent".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectLinearizationChangedEvent)) {
            return false;
        }
        ProjectLinearizationChangedEvent other = (ProjectLinearizationChangedEvent) obj;
        return this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ProjectLinearizationChangedEvent")
                .add("projectId", getProjectId()).toString();
    }
}