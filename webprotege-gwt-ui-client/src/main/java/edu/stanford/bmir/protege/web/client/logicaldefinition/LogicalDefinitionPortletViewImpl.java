package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.icd.AncestorClassHierarchy;
import edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsAction;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogicalDefinitionPortletViewImpl extends Composite implements LogicalDefinitionPortletView {

    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionPortletViewImpl");

    @UiField
    HTMLPanel paneContainer;

    @UiField
    HTMLPanel superClassContainer;

    @UiField
    HTMLPanel necessaryConditionsContainer;
    @UiField
    ListBox ancestorDropdown;

    private static final LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder.class);


    private OWLEntity owlEntity;

    private DispatchServiceManager dispatchServiceManager;

    private ProjectId projectId;

    private List<OWLEntityData> ancestorsList = new ArrayList<>();

    private PostCoordinationTableConfiguration postCoordinationTableConfiguration;

    private List<PostCoordinationTableAxisLabel> labels;

    private WhoficCustomScalesValues superclassScalesValue;

    private WhoficEntityPostCoordinationSpecification superclassSpecification;

    private List<PostcoordinationAxisToGenericScale> axisToGenericScales = new ArrayList<>();

    private LogicalDefinitionTable superClassTable = new LogicalDefinitionTable(new LogicalDefinitionTableConfig("Logical Definition Axis",
            "Value",
            (postCoordinationAxis, table) -> {

                List<String> selectedScales = superclassScalesValue.getScaleCustomizations().stream()
                        .filter(s -> s.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                        .flatMap(s -> s.getPostcoordinationScaleValues().stream())
                        .collect(Collectors.toList());

                if(selectedScales.isEmpty()) {
                    List<String> equivalentRoots = axisToGenericScales.stream()
                            .filter(axis -> axis.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                            .map(PostcoordinationAxisToGenericScale::getGenericPostcoordinationScaleTopClass)
                            .collect(Collectors.toList());
                    selectedScales.addAll(equivalentRoots);
                }

                Map<String, String> map = new HashMap<>();
                for(String availableAxis : selectedScales) {
                    map.put(availableAxis, availableAxis);
                }

                table.setAvailableValues(map);
            }));


    private LogicalDefinitionTable necessaryConditionsTable = new LogicalDefinitionTable(new LogicalDefinitionTableConfig("Necessary Axis",
            "Value",
            (postCoordinationAxis, table) -> {
                List<String> selectedScales = superclassScalesValue.getScaleCustomizations().stream()
                        .filter(s -> s.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                        .flatMap(s -> s.getPostcoordinationScaleValues().stream())
                        .collect(Collectors.toList());
                if(selectedScales.isEmpty()) {
                    List<String> equivalentRoots = axisToGenericScales.stream()
                            .filter(axis -> axis.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                            .map(PostcoordinationAxisToGenericScale::getGenericPostcoordinationScaleTopClass)
                            .collect(Collectors.toList());
                    selectedScales.addAll(equivalentRoots);
                }
                Map<String, String> map = new HashMap<>();
                for(String availableAxis : selectedScales) {
                    map.put(availableAxis, availableAxis);
                }

                table.setAvailableValues(map);
            }));

    @Inject
    public LogicalDefinitionPortletViewImpl(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        initWidget(ourUiBinder.createAndBindUi(this));

        superClassContainer.add(superClassTable);

        necessaryConditionsContainer.add(necessaryConditionsTable);

        dispatchServiceManager.execute(GetPostcoordinationAxisToGenericScaleAction.create(), result -> {
            this.axisToGenericScales = result.getPostcoordinationAxisToGenericScales();
        });

        this.ancestorDropdown.addChangeHandler((changeEvent) -> {
            dispatchServiceManager.execute(GetEntityCustomScalesAction.create(ancestorDropdown.getSelectedValue(), projectId), postcoordination -> {
                this.superclassScalesValue = postcoordination.getWhoficCustomScaleValues();
            });
            dispatchServiceManager.execute(GetEntityPostCoordinationAction.create(ancestorDropdown.getSelectedValue(), projectId), postcoordination -> {

                this.superclassSpecification = postcoordination.getPostCoordinationSpecification();
                populateAvailableAxisValues();
            });
        });
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

            dispatchServiceManager.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), config -> {
                this.postCoordinationTableConfiguration = config.getTableConfiguration();
                this.labels = config.getLabels();
            });

        });


    }


    private void populateAvailableAxisValues() {
        Optional<PostCoordinationSpecification> mmsSpec = this.superclassSpecification.getPostCoordinationSpecifications()
                .stream().filter(spec -> spec.getLinearizationView().equalsIgnoreCase("http://id.who.int/icd/release/11/mms"))
                .findFirst();

        List<String> orderedAxis = new ArrayList<>();
        if(mmsSpec.isPresent()) {
            orderedAxis.addAll(mmsSpec.get().getRequiredAxes().stream().sorted((s1, s2) -> getAxisName(s1).compareTo(getAxisName(s2)))
                    .collect(Collectors.toList()));
            orderedAxis.addAll(mmsSpec.get().getAllowedAxes().stream().sorted((s1, s2) -> getAxisName(s1).compareTo(getAxisName(s2))).collect(Collectors.toList()));
            orderedAxis.addAll(mmsSpec.get().getNotAllowedAxes().stream().sorted((s1, s2) -> getAxisName(s1).compareTo(getAxisName(s2))).collect(Collectors.toList()));
        }

        Map<String, String> orderedAxisMap = new HashMap<>();

        for(String axis: orderedAxis) {
            orderedAxisMap.put(axis, getAxisName(axis));
        }
        
        superClassTable.setAvailableAxis(orderedAxisMap);
        necessaryConditionsTable.setAvailableAxis(orderedAxisMap);
    }


    private String getAxisName(String axisIri) {
        return this.labels.stream()
                .filter(entry -> entry.getPostCoordinationAxis().equalsIgnoreCase(axisIri))
                .map(PostCoordinationTableAxisLabel::getTableLabel).findFirst()
                .orElse("");
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
