package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import edu.stanford.bmir.protege.web.shared.frame.State;
import edu.stanford.bmir.protege.web.shared.icd.AncestorClassHierarchy;
import edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsAction;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogicalDefinitionPortletViewImpl extends Composite implements LogicalDefinitionPortletView {

    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionPortletViewImpl");


    @UiField
    HTMLPanel paneContainer;
    @UiField
    ListBox ancestorDropdown;

    ListBox axisDropdown;

    ListBox valuesDropdown;

    private List<LogicalDefinitionTableRow> superClassTableRows = new ArrayList<>();

    @UiField
    protected FlexTable superclassTable;

    private static final LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder.class);
    LogicalDefinitionResourceBundle.LogicalDefinitionCss style;


    private OWLEntity owlEntity;

    private DispatchServiceManager dispatchServiceManager;

    private ProjectId projectId;

    private List<OWLEntityData> ancestorsList = new ArrayList<>();

    private PostCoordinationTableConfiguration postCoordinationTableConfiguration;

    private List<PostCoordinationTableAxisLabel> labels;

    private WhoficCustomScalesValues superclassScalesValue;

    @Inject
    public LogicalDefinitionPortletViewImpl(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        initWidget(ourUiBinder.createAndBindUi(this));
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();
        this.ancestorDropdown.addChangeHandler((changeEvent) -> {
            dispatchServiceManager.execute(GetEntityCustomScalesAction.create(ancestorDropdown.getSelectedValue(), projectId), postcoordination -> {
                this.superclassScalesValue = postcoordination.getWhoficCustomScaleValues();
                logger.info("ALEX " + superclassScalesValue);
            });
        });
        initializeSuperClassTableHeader();

        initializeSuperClassValueControls();

    }

    @Override
    public void setWidget(IsWidget w) {

    }
    @Override
    public void dispose() {

    }

    @Override
    public void setEntity(OWLEntity owlEntity, ProjectId projectId) {
        ancestorDropdown.clear();
        this.owlEntity = owlEntity;
        this.projectId = projectId;
        dispatchServiceManager.execute(GetClassAncestorsAction.create(owlEntity.getIRI(), projectId), getHierarchyParentsResult -> {
            Set<OWLEntityData> result = new HashSet<>();
            populateAncestorsFromTree(getHierarchyParentsResult.getAncestorsTree(), result);
            ancestorsList = new ArrayList<>(result);
            for(OWLEntityData ancestor: ancestorsList) {
                ancestorDropdown.addItem(ancestor.getBrowserText(), ancestor.getIri().toString());
            }
            List<PropertyValue> propertyValues = ancestorsList.stream().map(ancestor -> PropertyClassValue.get(
                    OWLObjectPropertyData.get(new OWLObjectPropertyImpl(ancestor.getIri()),
                            ImmutableMap.of()), OWLClassData.get(ancestor.asEntity().get().asOWLClass(), ImmutableMap.of()), State.ASSERTED)
            ).collect(Collectors.toList());
            PropertyValueList propertyValueList = new PropertyValueList(propertyValues);
            dispatchServiceManager.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), config -> {
                this.postCoordinationTableConfiguration = config.getTableConfiguration();
                this.labels = config.getLabels();
            });

        });


    }

    private void initializeSuperClassValueControls() {
        this.axisDropdown = new ListBox();
        this.valuesDropdown = new ListBox();


        this.superclassTable.setWidget(this.superClassTableRows.size()+1, 0, axisDropdown);
        this.superclassTable.setWidget(this.superClassTableRows.size()+1, 1, valuesDropdown);

    }

    private void initializeSuperClassTableHeader() {
        superclassTable.setStyleName(style.superClassTable());
        addHeaderCell("Logical definition axis", 0);
        addHeaderCell("Value", 1);
        superclassTable.getRowFormatter().addStyleName(0, style.superClassTableHeader());

    }
    private void addHeaderCell(String headerText, int column) {
        Widget headerCell = new Label(headerText);
        superclassTable.setWidget(0, column, headerCell);
        superclassTable.getCellFormatter().addStyleName(0, column, style.tableText());
    }


//    private void populateAvailableAxisValues() {
//        this.superclassScalesValue.getScaleCustomizations()
//    }

    private void populateAncestorsFromTree(AncestorClassHierarchy node, Set<OWLEntityData> accumulator) {
        accumulator.add(node.getCurrentNode());
        for(AncestorClassHierarchy child: node.getChildren()) {
            populateAncestorsFromTree(child, accumulator);
        }
    }


    interface LogicalDefinitionPortletViewImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionPortletViewImpl> {

    }
}
