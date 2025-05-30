package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class HierarchyDescriptorPresenter {

    private final ProjectId projectId;

    private final HierarchyDescriptorView view;

    private final DispatchServiceManager dispatch;

    @Inject
    public HierarchyDescriptorPresenter(ProjectId projectId, HierarchyDescriptorView view, DispatchServiceManager dispatch) {
        this.projectId = projectId;
        this.view = view;
        this.dispatch = dispatch;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setHierarchyDescriptor(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        Set<OWLEntity> entities = new LinkedHashSet<>();
        if(hierarchyDescriptor instanceof ClassHierarchyDescriptor) {
            view.setEntityType(EntityType.CLASS);
            entities.addAll(((ClassHierarchyDescriptor) hierarchyDescriptor).getRoots());
        }
        else if(hierarchyDescriptor instanceof ObjectPropertyHierarchyDescriptor) {
            view.setEntityType(EntityType.OBJECT_PROPERTY);
            entities.addAll(((ObjectPropertyHierarchyDescriptor) hierarchyDescriptor).getRoots());
        }
        else if(hierarchyDescriptor instanceof DataPropertyHierarchyDescriptor) {
            view.setEntityType(EntityType.DATA_PROPERTY);
            entities.addAll(((DataPropertyHierarchyDescriptor) hierarchyDescriptor).getRoots());
        }
        else if(hierarchyDescriptor instanceof AnnotationPropertyHierarchyDescriptor) {
            view.setEntityType(EntityType.ANNOTATION_PROPERTY);
            entities.addAll(((AnnotationPropertyHierarchyDescriptor) hierarchyDescriptor).getRoots());
        }
        // This isn't ideal, but it's simplest for now, and, given the frequency with which this will be used
        // it's likely satisfactory
        Set<OWLEntityData> renderedEntities = new LinkedHashSet<>();
        try {
            dispatch.beginBatch();
            for(OWLEntity entity : entities) {
                dispatch.execute(GetEntityRenderingAction.create(projectId, entity), r -> {
                    renderedEntities.add(r.getEntityData());
                    if(renderedEntities.size() == entities.size()) {
                        view.setRoots(new ArrayList<>(renderedEntities));
                    }
                });
            }
        } finally {
            dispatch.executeCurrentBatch();
        }
    }


    public Optional<HierarchyDescriptor> getHierarchyDescriptor() {
        EntityType<?> entityType = view.getEntityType();
        List<OWLEntity> rootEntities = view.getRoots().stream().map(OWLEntityData::getEntity).filter(e -> e.getEntityType().equals(entityType)).distinct().collect(Collectors.toList());
        if(entityType.equals(EntityType.CLASS)) {
            Set<OWLClass> classes = rootEntities.stream().map(e -> (OWLClass) e).collect(Collectors.toSet());
            return Optional.of(ClassHierarchyDescriptor.get(classes));
        }
        else if(entityType.equals(EntityType.OBJECT_PROPERTY)) {
            Set<OWLObjectProperty> properties = rootEntities.stream().map(e -> (OWLObjectProperty) e).collect(Collectors.toSet());
            return Optional.of(ObjectPropertyHierarchyDescriptor.get(properties));
        }
        else if(entityType.equals(EntityType.DATA_PROPERTY)) {
            Set<OWLDataProperty> properties = rootEntities.stream().map(e -> (OWLDataProperty) e).collect(Collectors.toSet());
            return Optional.of(DataPropertyHierarchyDescriptor.get(properties));
        }
        else if(entityType.equals(EntityType.ANNOTATION_PROPERTY)) {
            Set<OWLAnnotationProperty> properties = rootEntities.stream().map(e -> (OWLAnnotationProperty) e).collect(Collectors.toSet());
            return Optional.of(AnnotationPropertyHierarchyDescriptor.get(properties));
        }
        else {
            return Optional.empty();
        }
    }
}
