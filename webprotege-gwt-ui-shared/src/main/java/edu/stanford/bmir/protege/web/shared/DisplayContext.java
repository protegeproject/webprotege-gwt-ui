package edu.stanford.bmir.protege.web.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class DisplayContext implements IsSerializable {

    @JsonCreator
    public static DisplayContext create(@JsonProperty("projectId") ProjectId projectId,
                                        @JsonProperty("perspectiveId") PerspectiveId perspectiveId,
                                        @Nonnull @JsonProperty("viewId") ViewId viewId,
                                        @Nonnull @JsonProperty("viewNodeId") ViewNodeId viewNodeId,
                                        @Nonnull @JsonProperty("viewProperties") Map<String, String> viewProperties,
                                        @JsonProperty("formIds") List<FormId> formIds,
                                        @Nullable @JsonProperty("formFieldId") FormRegionId formFieldId,
                                        @Nonnull @JsonProperty("selectedPaths") List<List<OWLEntity>> selectedPaths) {
        return new AutoValue_DisplayContext(projectId, perspectiveId, viewId, viewNodeId, viewProperties, new ArrayList<>(formIds), formFieldId, selectedPaths);
    }

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @Nonnull
    @JsonProperty("perspectiveId")
    public abstract PerspectiveId getPerspectiveId();

    @Nonnull
    @JsonProperty("viewId")
    public abstract ViewId getViewId();

    @Nonnull
    @JsonProperty("viewNodeId")
    public abstract ViewNodeId getViewNodeId();

    @Nonnull
    @JsonProperty("viewProperties")
    public abstract Map<String, String> getViewProperties();

    @JsonProperty("formIds")
    public abstract List<FormId> getFormIds();

    @Nullable
    @JsonProperty("formFieldId")
    public abstract FormRegionId getFormFieldId();

    @Nonnull
    @JsonProperty("selectedPaths")
    public abstract List<List<OWLEntity>> getSelectedPaths();
}
