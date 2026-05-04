package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class DeleteEntitiesResult_TestCase {

    private DeleteEntitiesResult deleteEntitiesResult;

    private ImmutableSet<OWLEntity> deletedEntities;

    @BeforeEach
    void setUp() {
        deletedEntities = ImmutableSet.of(mock(OWLEntity.class));
        deleteEntitiesResult = new DeleteEntitiesResult(deletedEntities);
    }

    @Test
    void shouldThrowNullPointerExceptionIf_deletedEntities_IsNull() {
        assertThatThrownBy(() -> new DeleteEntitiesResult(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldReturnSupplied_deletedEntities() {
        assertThat(deleteEntitiesResult.getDeletedEntities()).isEqualTo(deletedEntities);
    }

    @Test
    void shouldBeEqualToSelf() {
        assertThat(deleteEntitiesResult).isEqualTo(deleteEntitiesResult);
    }

    @Test
    void shouldNotBeEqualToNull() {
        assertThat(deleteEntitiesResult).isNotEqualTo(null);
    }

    @Test
    void shouldBeEqualToOther() {
        assertThat(deleteEntitiesResult).isEqualTo(new DeleteEntitiesResult(deletedEntities));
    }

    @Test
    void shouldNotBeEqualToOtherThatHasDifferent_deletedEntities() {
        assertThat(deleteEntitiesResult).isNotEqualTo(new DeleteEntitiesResult(ImmutableSet.of()));
    }

    @Test
    void shouldBeEqualToOtherHashCode() {
        assertThat(deleteEntitiesResult.hashCode()).isEqualTo(new DeleteEntitiesResult(deletedEntities).hashCode());
    }

    @Test
    void shouldImplementToString() {
        assertThat(deleteEntitiesResult.toString()).startsWith("DeleteEntitiesResult");
    }
}
