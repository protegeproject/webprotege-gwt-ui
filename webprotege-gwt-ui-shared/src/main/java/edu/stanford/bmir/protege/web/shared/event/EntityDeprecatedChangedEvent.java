package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Objects;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
@JsonTypeName("webprotege.events.entities.EntityDeprecationStatusChanged")
public class EntityDeprecatedChangedEvent extends ProjectEvent<EntityDeprecatedChangedHandler> {

    public transient static final Event.Type<EntityDeprecatedChangedHandler> ON_ENTITY_DEPRECATED = new Event.Type<EntityDeprecatedChangedHandler>();

    private OWLEntity entity;

    private boolean deprecated;

    public EntityDeprecatedChangedEvent(ProjectId source, OWLEntity entity, boolean deprecated) {
        super(source);
        this.entity = entity;
        this.deprecated = deprecated;
    }

    /**
     * For serialization only
     */
    private EntityDeprecatedChangedEvent() {
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public Event.Type<EntityDeprecatedChangedHandler> getAssociatedType() {
        return ON_ENTITY_DEPRECATED;
    }

    @Override
    protected void dispatch(EntityDeprecatedChangedHandler handler) {
        handler.handleEntityDeprecatedChangedEvent(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityDeprecatedChangedEvent)) {
            return false;
        }
        EntityDeprecatedChangedEvent that = (EntityDeprecatedChangedEvent) o;
        return deprecated == that.deprecated && entity.equals(that.entity) && this.getProjectId().equals(that.getProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, deprecated, getProjectId());
    }
}
