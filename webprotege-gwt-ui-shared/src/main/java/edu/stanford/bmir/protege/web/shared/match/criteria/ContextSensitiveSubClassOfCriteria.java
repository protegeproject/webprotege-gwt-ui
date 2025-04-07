package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ContextSensitiveSubClassOf")
public abstract class ContextSensitiveSubClassOfCriteria implements EntityMatchCriteria, ContextSensitiveCriteria {

    private static final String FILTER_TYPE = "filterType";

    @JsonProperty(FILTER_TYPE)
    public abstract HierarchyFilterType getFilterType();

    @JsonCreator
    @Nonnull
    public static ContextSensitiveSubClassOfCriteria get(@Nonnull @JsonProperty(FILTER_TYPE) HierarchyFilterType filterType) {
        return new AutoValue_ContextSensitiveSubClassOfCriteria(filterType);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public EntityMatchCriteria getEffectiveCriteria(DisplayContext displayContext) {
        if(!displayContext.getSelectedPaths().isEmpty()){
            List<OWLEntity> primarySelectionPath = displayContext.getSelectedPaths().get(0);
            OWLEntity selectedEntity = primarySelectionPath.get(primarySelectionPath.size() - 1);
            if (selectedEntity.isOWLClass()) {
                return SubClassOfCriteria.get(selectedEntity.asOWLClass(), getFilterType());
            }
        }
        return SubClassOfCriteria.get(DataFactory.getOWLThing(), getFilterType());
    }
}
