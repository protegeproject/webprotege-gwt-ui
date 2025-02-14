
package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetHierarchyRootsAction_TestCase {

    private GetHierarchyRootsAction action;
    @Mock
    private ProjectId projectId;
    @Mock
    private HierarchyDescriptor hierarchyDescriptor;

    @Before
    public void setUp() {
        action = GetHierarchyRootsAction.create(projectId, hierarchyDescriptor);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetHierarchyRootsAction.create(null, hierarchyDescriptor);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_hierarchyId_IsNull() {
        GetHierarchyRootsAction.create(projectId, null);
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
        assertThat(action, is(GetHierarchyRootsAction.create(projectId, hierarchyDescriptor)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(GetHierarchyRootsAction.create(mock(ProjectId.class), hierarchyDescriptor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_hierarchyId() {
        assertThat(action, is(not(GetHierarchyRootsAction.create(projectId, mock(ClassHierarchyDescriptor.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(GetHierarchyRootsAction.create(projectId, hierarchyDescriptor).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), startsWith("GetHierarchyRootsAction"));
    }

}
