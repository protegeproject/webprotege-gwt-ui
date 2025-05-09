
package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DeleteEntitiesAction_TestCase {

    private DeleteEntitiesAction deleteEntitiesAction;

    @Mock
    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;

    @Before
    public void setUp() {
        entities = ImmutableSet.of(mock(OWLEntity.class));
        deleteEntitiesAction = new DeleteEntitiesAction(ChangeRequestId.get(UUID.randomUUID().toString()),projectId, entities);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new DeleteEntitiesAction(ChangeRequestId.get(UUID.randomUUID().toString()),null, entities);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(deleteEntitiesAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entities_IsNull() {
        new DeleteEntitiesAction(ChangeRequestId.get(UUID.randomUUID().toString()),projectId, null);
    }

    @Test
    public void shouldReturnSupplied_entities() {
        assertThat(deleteEntitiesAction.getEntities(), is(this.entities));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(deleteEntitiesAction, is(deleteEntitiesAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(deleteEntitiesAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(deleteEntitiesAction, is(new DeleteEntitiesAction(ChangeRequestId.get(UUID.randomUUID().toString()),projectId, entities)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(deleteEntitiesAction, is(not(new DeleteEntitiesAction(ChangeRequestId.get(UUID.randomUUID().toString()),mock(ProjectId.class), entities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entities() {
        assertThat(deleteEntitiesAction, is(not(new DeleteEntitiesAction(ChangeRequestId.get(UUID.randomUUID().toString()),projectId, ImmutableSet.of()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(deleteEntitiesAction.hashCode(), is(new DeleteEntitiesAction(ChangeRequestId.get(UUID.randomUUID().toString()),projectId, entities).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(deleteEntitiesAction.toString(), startsWith("DeleteEntitiesAction"));
    }

}
