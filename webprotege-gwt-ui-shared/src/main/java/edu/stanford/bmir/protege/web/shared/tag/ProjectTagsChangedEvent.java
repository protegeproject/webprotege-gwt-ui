package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import edu.stanford.bmir.protege.web.shared.event.EventId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Mar 2018
 */
@JsonTypeName("webprotege.events.tags.ProjectTagsChanged")
public class ProjectTagsChangedEvent extends ProjectEvent<ProjectTagsChangedHandler> {

    public static final transient Event.Type<ProjectTagsChangedHandler> ON_PROJECT_TAGS_CHANGED = new Event.Type<>();


    private EventId eventId;

    private Collection<Tag> projectTags;

    @JsonCreator
    public ProjectTagsChangedEvent(@JsonProperty("eventId") EventId eventId,
                                   @JsonProperty("projectId") @Nonnull ProjectId source,
                                   @JsonProperty("projectTags") @Nonnull Collection<Tag> projectTags) {
        super(source);
        this.eventId = eventId;
        this.projectTags = ImmutableList.copyOf(checkNotNull(projectTags));
    }

    @GwtSerializationConstructor
    private ProjectTagsChangedEvent() {
    }

    @JsonProperty("eventId")
    public EventId getEventId() {
        return eventId;
    }

    /**
     * Returns an immutable list of project tags.
     */
    @Nonnull
    public Collection<Tag> getProjectTags() {
        return projectTags;
    }

    @JsonIgnore
    @Override
    public Event.Type<ProjectTagsChangedHandler> getAssociatedType() {
        return ON_PROJECT_TAGS_CHANGED;
    }

    @Override
    protected void dispatch(ProjectTagsChangedHandler handler) {
        handler.handleProjectTagsChanged(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId(), projectTags);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectTagsChangedEvent)) {
            return false;
        }
        ProjectTagsChangedEvent other = (ProjectTagsChangedEvent) obj;
        return this.getProjectId().equals(other.getProjectId())
                && this.projectTags.equals(other.projectTags);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectTagsChangedEvent")
                .addValue(getProjectId())
                .add("tags", projectTags)
                .toString();
    }
}
