package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.list.ListBox;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.icd.AncestorClassHierarchy;
import edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogicalDefinitionPortletViewImpl extends Composite implements LogicalDefinitionPortletView {
    @UiField
    HTMLPanel paneContainer;

    @UiField
    com.google.gwt.user.client.ui.ListBox secondList;

    private static final LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder.class);


    private OWLEntity owlEntity;

    private DispatchServiceManager dispatchServiceManager;

    private ProjectId projectId;

    private List<OWLEntityData> ancestorsList = new ArrayList<>();


    @Inject
    public LogicalDefinitionPortletViewImpl(DispatchServiceManager dispatchServiceManager) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.dispatchServiceManager = dispatchServiceManager;
    }
    @Override
    public void setWidget(IsWidget w) {

    }
    @Override
    public void dispose() {

    }

    @Override
    public void setEntity(OWLEntity owlEntity, ProjectId projectId) {
        secondList.clear();
        this.owlEntity = owlEntity;
        this.projectId = projectId;
        dispatchServiceManager.execute(GetClassAncestorsAction.create(owlEntity.getIRI(), projectId), getHierarchyParentsResult -> {
            Set<OWLEntityData> result = new HashSet<>();
            populateAncestorsFromTree(getHierarchyParentsResult.getAncestorsTree(), result);
            ancestorsList = new ArrayList<>(result);
            for(OWLEntityData ancestor: ancestorsList) {
                secondList.addItem(ancestor.getBrowserText(), ancestor.getIri().toString());
            }
        });


    }


    private void populateAncestorsFromTree(AncestorClassHierarchy node, Set<OWLEntityData> accumulator) {
        accumulator.add(node.getCurrentNode());
        for(AncestorClassHierarchy child: node.getChildren()) {
            populateAncestorsFromTree(child, accumulator);
        }
    }


    interface LogicalDefinitionPortletViewImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionPortletViewImpl> {

    }
}
