
package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetHierarchyChildrenAction_TestCase {

    private GetHierarchyChildrenAction action;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity entity;

    @Mock
    private HierarchyDescriptor hierarchyDescriptor;

    @Before
    public void setUp() {
        action = GetHierarchyChildrenAction.create(projectId, entity, hierarchyDescriptor);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetHierarchyChildrenAction.create(null, entity, hierarchyDescriptor);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        GetHierarchyChildrenAction.create(projectId, null, hierarchyDescriptor);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(action.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_hierarchyId_IsNull() {
        GetHierarchyChildrenAction.create(projectId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_hierarchyId() {
        assertThat(action.getHierarchyDescriptor(), is(this.hierarchyDescriptor));
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
        assertThat(action, is(GetHierarchyChildrenAction.create(projectId, entity, hierarchyDescriptor)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(GetHierarchyChildrenAction.create(mock(ProjectId.class), entity, hierarchyDescriptor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(action, is(not(GetHierarchyChildrenAction.create(projectId, mock(OWLEntity.class), hierarchyDescriptor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_hierarchyId() {
        assertThat(action, is(not(GetHierarchyChildrenAction.create(projectId, entity, mock(HierarchyDescriptor.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(GetHierarchyChildrenAction.create(projectId, entity, hierarchyDescriptor).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), startsWith("GetHierarchyChildrenAction"));
    }

}
