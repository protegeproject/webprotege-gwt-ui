package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class DeleteEntitiesAction_TestCase {

    private DeleteEntitiesAction deleteEntitiesAction;

    private ProjectId projectId;

    private ChangeRequestId changeRequestId;

    private ImmutableSet<OWLEntity> entities;

    @BeforeEach
    void setUp() {
        projectId = mock(ProjectId.class);
        changeRequestId = mock(ChangeRequestId.class);
        entities = ImmutableSet.of(mock(OWLEntity.class));
        deleteEntitiesAction = new DeleteEntitiesAction(changeRequestId, projectId, entities);
    }

    @Test
    void shouldThrowNullPointerExceptionIf_changeRequestId_IsNull() {
        assertThatThrownBy(() -> new DeleteEntitiesAction(null, projectId, entities))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        assertThatThrownBy(() -> new DeleteEntitiesAction(changeRequestId, null, entities))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerExceptionIf_entities_IsNull() {
        assertThatThrownBy(() -> new DeleteEntitiesAction(changeRequestId, projectId, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldReturnSupplied_changeRequestId() {
        assertThat(deleteEntitiesAction.getChangeRequestId()).isEqualTo(changeRequestId);
    }

    @Test
    void shouldReturnSupplied_projectId() {
        assertThat(deleteEntitiesAction.getProjectId()).isEqualTo(projectId);
    }

    @Test
    void shouldReturnSupplied_entities() {
        assertThat(deleteEntitiesAction.getEntities()).isEqualTo(entities);
    }

    @Test
    void shouldBeEqualToSelf() {
        assertThat(deleteEntitiesAction).isEqualTo(deleteEntitiesAction);
    }

    @Test
    void shouldNotBeEqualToNull() {
        assertThat(deleteEntitiesAction).isNotEqualTo(null);
    }

    @Test
    void shouldBeEqualToOther() {
        assertThat(deleteEntitiesAction).isEqualTo(new DeleteEntitiesAction(changeRequestId, projectId, entities));
    }

    @Test
    void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(deleteEntitiesAction).isNotEqualTo(new DeleteEntitiesAction(changeRequestId, mock(ProjectId.class), entities));
    }

    @Test
    void shouldNotBeEqualToOtherThatHasDifferent_entities() {
        assertThat(deleteEntitiesAction).isNotEqualTo(new DeleteEntitiesAction(changeRequestId, projectId, ImmutableSet.of()));
    }

    @Test
    void shouldBeEqualToOtherHashCode() {
        assertThat(deleteEntitiesAction.hashCode()).isEqualTo(new DeleteEntitiesAction(changeRequestId, projectId, entities).hashCode());
    }

    @Test
    void shouldImplementToString() {
        assertThat(deleteEntitiesAction.toString()).startsWith("DeleteEntitiesAction");
    }
}
