package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.AddNamedHierarchyAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@AutoFactory
public class ConfigureHierarchyAction extends AbstractUiAction {

    private final TreeWidget<EntityNode, OWLEntity> treeWidget;

    private final EntityHierarchyModel model;

    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;

    public ConfigureHierarchyAction(@Provided Messages messages,
                                    TreeWidget<EntityNode, OWLEntity> treeWidget,
                                    EntityHierarchyModel model,
                                    @Provided DispatchServiceManager dispatch,
                                    @Provided ProjectId projectId) {
        super(messages.hierarchy_configure());
        this.treeWidget = treeWidget;
        this.model = model;
        this.dispatch = dispatch;
        this.projectId = projectId;
    }

    @Override
    public void execute() {
        Set<OWLClass> sel = treeWidget.getSelectedKeys()
                .stream()
                .filter(e -> e.isOWLClass())
                .map(e -> e.asOWLClass())
                .collect(Collectors.toSet());
        HierarchyDescriptor hierarchyDescriptor;
        if(sel.isEmpty()) {
            hierarchyDescriptor = ClassHierarchyDescriptor.get();
        }
        else {
            hierarchyDescriptor = ClassHierarchyDescriptor.get(sel);
        }
        dispatch.execute(AddNamedHierarchyAction.get(projectId, LanguageMap.of("en", "Class hierarchy fragment"),
                LanguageMap.empty(),
                hierarchyDescriptor),
                result -> {
                    model.setHierarchyDescriptor(hierarchyDescriptor);
                    treeWidget.setModel(GraphTreeNodeModel.create(model, EntityNode::getEntity));
                });

    }
}
