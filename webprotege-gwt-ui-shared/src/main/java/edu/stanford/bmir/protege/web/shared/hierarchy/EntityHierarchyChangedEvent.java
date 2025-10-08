package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.perspective.HasChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphModelChangedEvent;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 1 Dec 2017
 */
@JsonTypeName("webprotege.events.hierarchies.EntityHierarchyChanged")
public class EntityHierarchyChangedEvent extends ProjectEvent<EntityHierarchyChangedHandler> implements HasChangeRequestId {

    public static final transient Event.Type<EntityHierarchyChangedHandler> ON_HIERARCHY_CHANGED = new Event.Type<>();

    private HierarchyDescriptor hierarchyDescriptor;

    private GraphModelChangedEvent<EntityNode> changeEvent;

    private ChangeRequestId changeRequestId;

    @JsonCreator
    public EntityHierarchyChangedEvent(@JsonProperty("projectId") @Nonnull ProjectId source,
                                       @JsonProperty("hierarchyDescriptor") @Nonnull HierarchyDescriptor hierarchyDescriptor,
                                       @JsonProperty("changeEvent") @Nonnull GraphModelChangedEvent<EntityNode> changeEvent,
                                       @JsonProperty("changeRequestId") ChangeRequestId changeRequestId) {
        super(source);
        this.hierarchyDescriptor = checkNotNull(hierarchyDescriptor);
        this.changeEvent = checkNotNull(changeEvent);
        this.changeRequestId = changeRequestId;
    }

    @JsonProperty("hierarchyDescriptor")
    @Nonnull
    public HierarchyDescriptor getHierarchyDescriptor() {
        return hierarchyDescriptor;
    }

    @GwtSerializationConstructor
    private EntityHierarchyChangedEvent() {
    }

    @Nonnull
    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public Event.Type<EntityHierarchyChangedHandler> getAssociatedType() {
        return ON_HIERARCHY_CHANGED;
    }

    @Override
    protected void dispatch(EntityHierarchyChangedHandler handler) {
        handler.handleHierarchyChanged(this);
    }

    public GraphModelChangedEvent<EntityNode> getChangeEvent() {
        return changeEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityHierarchyChangedEvent)) {
            return false;
        }
        EntityHierarchyChangedEvent that = (EntityHierarchyChangedEvent) o;
        return hierarchyDescriptor.equals(that.hierarchyDescriptor) && changeEvent.equals(that.changeEvent) && getSource().equals(that.getSource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(hierarchyDescriptor, changeEvent, getSource());
    }

    @Override
    public String toString() {
        return "EntityHierarchyChangedEvent{" +
                "hierarchyDescriptor=" + hierarchyDescriptor +
                ", changeEvent=" + changeEvent +
                '}';
    }
}
