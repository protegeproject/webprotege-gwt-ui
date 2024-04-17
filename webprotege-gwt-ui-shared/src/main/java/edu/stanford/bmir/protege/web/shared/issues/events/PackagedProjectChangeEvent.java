package edu.stanford.bmir.protege.web.shared.issues.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

@JsonTypeName(PackagedProjectChangeEvent.CHANNEL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PackagedProjectChangeEvent extends ProjectEvent {
    public final static String CHANNEL = "webprotege.events.projects.PackagedProjectChange";


    private ProjectId projectId;

    private List<ProjectEvent> projectEvents;

    public PackagedProjectChangeEvent(ProjectId projectId, List<ProjectEvent> projectEvents) {
        super(projectId);
        this.projectId = projectId;
        this.projectEvents = projectEvents;
    }


    @JsonCreator
    public static PackagedProjectChangeEvent create(@JsonProperty("projectId") ProjectId projectId, @JsonProperty("projectEvents") List<ProjectEvent> projectEventList) {
        return new PackagedProjectChangeEvent(projectId, projectEventList);
    }

    @GwtSerializationConstructor
    private PackagedProjectChangeEvent() {
    }

    @Override
    public Event.Type getAssociatedType() {
        return null;
    }

    @Override
    protected void dispatch(Object handler) {

    }
}
