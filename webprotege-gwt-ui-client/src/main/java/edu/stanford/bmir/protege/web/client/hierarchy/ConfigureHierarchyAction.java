package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.factory.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import org.semanticweb.owlapi.model.*;

import java.util.*;
import java.util.stream.Collectors;

@AutoFactory
public class ConfigureHierarchyAction extends AbstractUiAction {

    private final TreeWidget<EntityNode, OWLEntity> treeWidget;

    private final EntityHierarchyModel model;

    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;

    private final UuidV4Provider uuidV4Provider;

    public ConfigureHierarchyAction(@Provided Messages messages,
                                    TreeWidget<EntityNode, OWLEntity> treeWidget,
                                    EntityHierarchyModel model,
                                    @Provided DispatchServiceManager dispatch,
                                    @Provided UuidV4Provider uuidV4Provider,
                                    @Provided ProjectId projectId) {
        super(messages.hierarchy_configure());
        this.treeWidget = treeWidget;
        this.model = model;
        this.dispatch = dispatch;
        this.projectId = projectId;
        this.uuidV4Provider = uuidV4Provider;
    }

    @Override
    public void execute() {
        Set<OWLClass> sel = treeWidget.getSelectedKeys()
                .stream()
                .filter(OWLEntity::isOWLClass)
                .map(OWLEntity::asOWLClass)
                .collect(Collectors.toSet());
        HierarchyDescriptor hierarchyDescriptor;
        if (sel.isEmpty()) {
            hierarchyDescriptor = ClassHierarchyDescriptor.get();
        } else {
            hierarchyDescriptor = ClassHierarchyDescriptor.get(sel);
        }

        NamedHierarchy hierarchy = NamedHierarchy.get(HierarchyId.get(uuidV4Provider.get()), LanguageMap.of("en", "Class hierarchy fragment"),
                LanguageMap.empty(),
                hierarchyDescriptor);
        dispatch.execute(SetNamedHierarchiesAction.get(projectId, Collections.singletonList(hierarchy)),
                result -> {
                    model.setHierarchyDescriptor(hierarchyDescriptor);
                    treeWidget.setModel(GraphTreeNodeModel.create(model, EntityNode::getEntity));
                });

    }
}
