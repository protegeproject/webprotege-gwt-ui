
package edu.stanford.bmir.protege.web.shared.frame;

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
public class GetOntologyFramesAction_TestCase {

    private GetOntologyFramesAction action;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp()
        throws Exception
    {
        action = GetOntologyFramesAction.create(projectId);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetOntologyFramesAction.create(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(action));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(GetOntologyFramesAction.create(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(GetOntologyFramesAction.create(mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(GetOntologyFramesAction.create(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), startsWith("GetOntologyFramesAction"));
    }

}
