package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */

@JsonTypeName("webprotege.hierarchies.GetProjectEvents")
public class GetProjectEventsAction implements Action<GetProjectEventsResult>, HasProjectId {

    private ProjectId projectId;

    private EventTag sinceTag;

    /**
     * When {@code true} the request asks only for the current head position of
     * the project's event stream: the service reads no history and returns an
     * empty window whose start and end tags are both the head. This anchors a
     * freshly-opened project at "now" instead of replaying the whole archive
     * from ordinal zero. Absent or {@code false} on the wire means the normal
     * since-tag catch-up query, so an old service simply ignores it (returning
     * history) rather than failing.
     */
    @JsonProperty("latestOnly")
    private boolean latestOnly;

    /**
     * For serialization purposes only.
     */
    private GetProjectEventsAction() {
    }

    private GetProjectEventsAction(@Nonnull EventTag sinceTag, @Nonnull ProjectId projectId, boolean latestOnly) {
        this.sinceTag = checkNotNull(sinceTag);
        this.projectId = checkNotNull(projectId);
        this.latestOnly = latestOnly;
    }

    public static GetProjectEventsAction create(@Nonnull EventTag sinceTag, @Nonnull ProjectId projectId) {
        return new GetProjectEventsAction(sinceTag, projectId, false);
    }

    /**
     * Creates an action that anchors at the current head of the project's event
     * stream. The response carries no historical events, only the current
     * position, so an opening client can begin receiving live updates from
     * "now" without downloading and re-dispatching the entire archived history.
     */
    public static GetProjectEventsAction anchor(@Nonnull ProjectId projectId) {
        return new GetProjectEventsAction(EventTag.getFirst(), projectId, true);
    }

    public EventTag getSinceTag() {
        return sinceTag;
    }

    public boolean isLatestOnly() {
        return latestOnly;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }



    @Override
    public int hashCode() {
        return Objects.hashCode(sinceTag, projectId, latestOnly);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectEventsAction)) {
            return false;
        }
        GetProjectEventsAction other = (GetProjectEventsAction) obj;
        return this.sinceTag.equals(other.sinceTag)
                && this.projectId.equals(other.projectId)
                && this.latestOnly == other.latestOnly;
    }

    @Override
    public String toString() {
        return toStringHelper("GetProjectEventsAction")
                          .addValue(projectId)
                          .add("since", sinceTag)
                          .add("latestOnly", latestOnly).toString();
    }
}
