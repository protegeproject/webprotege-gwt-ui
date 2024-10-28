package edu.stanford.bmir.protege.web.client.logicaldefinition;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableConfiguration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

public class LogicalDefinitionTableBuilder {


    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectId projectId;

    private List<OWLEntityData> ancestorsList = new ArrayList<>();

    private List<PostCoordinationTableAxisLabel> labels;

    private LogicalDefinitionTableWrapperImpl.RemoveTableHandler removeTableHandler;
    private LogicalDefinitionTableConfig.AddAxisValueHandler addAxisValueHandler;

    private PostCoordinationTableConfiguration postCoordinationTableConfiguration;

    private String parentIri;

    public LogicalDefinitionTableBuilder(DispatchServiceManager dispatchServiceManager,
                                         ProjectId projectId,
                                         LogicalDefinitionTableConfig.AddAxisValueHandler addAxisValueHandler) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.addAxisValueHandler = addAxisValueHandler;
    }


    public LogicalDefinitionTableBuilder withAncestorsList(List<OWLEntityData> ancestorsList) {
        this.ancestorsList = ancestorsList;
        return this;
    }

    public LogicalDefinitionTableBuilder withLabels(List<PostCoordinationTableAxisLabel> labels) {
        this.labels = labels;
        return this;
    }
    public LogicalDefinitionTableBuilder withParentIri(String parentIri) {
        this.parentIri = parentIri;
        return this;
    }

    public LogicalDefinitionTableBuilder withPostCoordinationTableConfiguration(PostCoordinationTableConfiguration postCoordinationTableConfiguration) {
        this.postCoordinationTableConfiguration = postCoordinationTableConfiguration;
        return this;
    }

    public LogicalDefinitionTableBuilder withRemoveHandler(LogicalDefinitionTableWrapperImpl.RemoveTableHandler removeTableHandler) {
        this.removeTableHandler = removeTableHandler;
        return this;
    }

    public LogicalDefinitionTableWrapper asExistingTable(){
        LogicalDefinitionTableWrapper logicalDefinitionTableWrapper = new LogicalDefinitionTableWrapperImpl(dispatchServiceManager,
                projectId,
                addAxisValueHandler);

        logicalDefinitionTableWrapper.setAncestorList(this.ancestorsList);
        logicalDefinitionTableWrapper.setLabels(this.labels);
        logicalDefinitionTableWrapper.setParentIRI(this.parentIri);
        logicalDefinitionTableWrapper.enableReadOnly();
        logicalDefinitionTableWrapper.setPostCoordinationTableConfiguration(this.postCoordinationTableConfiguration);
        logicalDefinitionTableWrapper.setRemoveTableHandleWrapper(this.removeTableHandler);
        logicalDefinitionTableWrapper.asExistingTable();

        return logicalDefinitionTableWrapper;
    }

    public LogicalDefinitionTableWrapper asNewTable() {
        LogicalDefinitionTableWrapper logicalDefinitionTableWrapper = new LogicalDefinitionTableWrapperImpl(dispatchServiceManager,
                projectId,
                addAxisValueHandler);

        logicalDefinitionTableWrapper.setAncestorList(this.ancestorsList);
        logicalDefinitionTableWrapper.setLabels(this.labels);
        logicalDefinitionTableWrapper.setParentIRI(this.parentIri);
        logicalDefinitionTableWrapper.enableEditable();
        logicalDefinitionTableWrapper.setPostCoordinationTableConfiguration(this.postCoordinationTableConfiguration);
        logicalDefinitionTableWrapper.setRemoveTableHandleWrapper(this.removeTableHandler);
        logicalDefinitionTableWrapper.asNewTable();



        return logicalDefinitionTableWrapper;
    }
}
