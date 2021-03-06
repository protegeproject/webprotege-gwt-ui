
package edu.stanford.bmir.protege.web.shared.tag;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UpdateEntityTagsAction_TestCase {

    private UpdateEntityTagsAction action;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity entity;

    @Mock
    private Set<TagId> fromTagIds;

    @Mock
    private Set<TagId> toTagIds;

    @Before
    public void setUp() {
        fromTagIds = Collections.singleton(mock(TagId.class));
        toTagIds = Collections.singleton(mock(TagId.class));
        action = UpdateEntityTagsAction.create(projectId, entity, fromTagIds, toTagIds);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        UpdateEntityTagsAction.create(null, entity, fromTagIds, toTagIds);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        UpdateEntityTagsAction.create(projectId, null, fromTagIds, toTagIds);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(action.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_fromTagIds_IsNull() {
        UpdateEntityTagsAction.create(projectId, entity, null, toTagIds);
    }

    @Test
    public void shouldReturnSupplied_fromTagIds() {
        assertThat(action.getFromTagIds(), is(this.fromTagIds));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_toTagIds_IsNull() {
        UpdateEntityTagsAction.create(projectId, entity, fromTagIds, null);
    }

    @Test
    public void shouldReturnSupplied_toTagIds() {
        assertThat(action.getToTagIds(), is(this.toTagIds));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(action));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(UpdateEntityTagsAction.create(projectId, entity, fromTagIds, toTagIds)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(Matchers.not(UpdateEntityTagsAction.create(mock(ProjectId.class), entity, fromTagIds, toTagIds))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(action, is(Matchers.not(UpdateEntityTagsAction.create(projectId, mock(OWLEntity.class), fromTagIds, toTagIds))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_fromTagIds() {
        assertThat(action, is(Matchers.not(UpdateEntityTagsAction.create(projectId, entity, Collections.singleton(mock(TagId.class)), toTagIds))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_toTagIds() {
        assertThat(action, is(Matchers.not(UpdateEntityTagsAction.create(projectId, entity, fromTagIds, Collections.singleton(mock(TagId.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(UpdateEntityTagsAction.create(projectId, entity, fromTagIds, toTagIds).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), Matchers.startsWith("UpdateEntityTagsAction"));
    }

}
