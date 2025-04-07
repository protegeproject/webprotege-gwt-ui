package edu.stanford.bmir.protege.web.shared;

import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisplayContextBuilder {

    private ProjectId projectId;

    private PerspectiveId perspectiveId;

    private ViewId viewId;

    private ViewNodeId viewNodeId;

    private Map<String, String> nodeProperties;

    private List<FormId> formIds = new ArrayList<>();

    private FormRegionId formFieldId;

    private List<List<OWLEntity>> selectedPaths = new ArrayList<>();


    public static DisplayContextBuilder builder() {
        return new DisplayContextBuilder();
    }

    public DisplayContextBuilder setProjectId(@Nonnull ProjectId projectId) {
        this.projectId = projectId;
        return this;
    }

    public DisplayContextBuilder setPerspectiveId(@Nonnull PerspectiveId perspectiveId) {
        this.perspectiveId = perspectiveId;
        return this;
    }

    public DisplayContextBuilder setViewId(@Nonnull ViewId viewId) {
        this.viewId = viewId;
        return this;
    }

    public DisplayContextBuilder setViewProperties(@Nonnull Map<String, String> nodeProperties) {
        this.nodeProperties = nodeProperties;
        return this;
    }

    public DisplayContextBuilder setFormIds(@Nonnull List<FormId> formIds) {
        this.formIds = new ArrayList<>(formIds);
        return this;
    }

    public DisplayContextBuilder addFormId(@Nonnull FormId formId) {
        this.formIds.add(formId);
        return this;
    }

    public DisplayContext build() {
        if (projectId == null) {
            throw new IllegalStateException("ProjectId must not be null");
        }
        if (perspectiveId == null) {
            throw new IllegalStateException("PerspectiveId must not be null");
        }
        if (viewId == null) {
            throw new IllegalStateException("ViewId must not be null");
        }
        if(viewNodeId == null) {
            throw new IllegalStateException("ViewNodeId must not be null");
        }
        if (nodeProperties == null) {
            throw new IllegalStateException("NodeProperties must not be null");
        }

        return DisplayContext.create(
                projectId,
                perspectiveId,
                viewId,
                viewNodeId,
                nodeProperties,
                Collections.unmodifiableList(formIds),
                formFieldId,
                selectedPaths
        );
    }

    public DisplayContextBuilder setFormFieldId(FormRegionId formRegionId) {
        this.formFieldId = formRegionId;
        return this;
    }

    public DisplayContextBuilder setSelectedPaths(List<List<OWLEntity>> selectedPaths) {
        this.selectedPaths.clear();
        this.selectedPaths.addAll(selectedPaths);
        return this;
    }

    public List<List<OWLEntity>> getSelectedPaths() {
        return new ArrayList<>(selectedPaths);
    }

    public DisplayContextBuilder setViewNodeId(ViewNodeId viewNodeId) {
        this.viewNodeId = viewNodeId;
        return this;
    }

    @Override
    public String toString() {
        return "DisplayContextBuilder{" +
                "projectId=" + projectId +
                ", perspectiveId=" + perspectiveId +
                ", viewId=" + viewId +
                ", viewNodeId=" + viewNodeId +
                ", nodeProperties=" + nodeProperties +
                ", formIds=" + formIds +
                ", formFieldId=" + formFieldId +
                ", selectedPaths=" + selectedPaths +
                '}';
    }
}
