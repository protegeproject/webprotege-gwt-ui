package edu.stanford.bmir.protege.web.shared.individuals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.GetIndividualsPageContainingIndividual")
public abstract class GetIndividualsPageContainingIndividualResult implements Result {

    @JsonCreator
    public static GetIndividualsPageContainingIndividualResult create(@JsonProperty("individual") @Nonnull OWLNamedIndividual individual,
                                                                      @JsonProperty("page") @Nonnull Page<EntityNode> page,
                                                                      @JsonProperty("actualType") @Nonnull EntityNode actualType,
                                                                      @JsonProperty("actualMode") @Nonnull InstanceRetrievalMode actualMode,
                                                                      @JsonProperty("types") @Nonnull ImmutableSet<EntityNode> types) {
        return new AutoValue_GetIndividualsPageContainingIndividualResult(individual,
                                                                          actualType,
                                                                          actualMode,
                                                                          page,
                                                                          types);
    }

    @Nonnull
    public abstract OWLNamedIndividual getIndividual();

    @Nonnull
    public abstract EntityNode getActualType();

    @Nonnull
    public abstract InstanceRetrievalMode getActualMode();

    @Nonnull
    public abstract Page<EntityNode> getPage();

    @Nonnull
    public abstract ImmutableSet<EntityNode> getTypes();
}
