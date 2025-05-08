package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetClassHierarchyParentsByAxiomType")
public abstract class GetClassHierarchyParentsByAxiomTypeAction extends AbstractHasProjectAction<GetClassHierarchyParentsByAxiomTypeResult> {

    @JsonCreator
    public static GetClassHierarchyParentsByAxiomTypeAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                                   @JsonProperty("owlClass") @Nonnull OWLClass owlClass,
                                                                   @JsonProperty("classHierarchyDescriptor") @Nonnull ClassHierarchyDescriptor classHierarchyDescriptor) {
        return new AutoValue_GetClassHierarchyParentsByAxiomTypeAction(projectId, owlClass, classHierarchyDescriptor);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLClass getOwlClass();

    public abstract ClassHierarchyDescriptor getClassHierarchyDescriptor();
}
