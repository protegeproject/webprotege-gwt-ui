
package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ResetPerspectiveLayoutAction_TestCase {

    private ResetPerspectiveLayoutAction resetPerspectiveLayoutAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private PerspectiveId perspectiveId;

    @Before
    public void setUp()
        throws Exception
    {
        resetPerspectiveLayoutAction = ResetPerspectiveLayoutAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),projectId, perspectiveId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        ResetPerspectiveLayoutAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),null, perspectiveId);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(resetPerspectiveLayoutAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_perspectiveId_IsNull() {
        ResetPerspectiveLayoutAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),projectId, null);
    }

    @Test
    public void shouldReturnSupplied_perspectiveId() {
        assertThat(resetPerspectiveLayoutAction.getPerspectiveId(), is(this.perspectiveId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(resetPerspectiveLayoutAction, is(resetPerspectiveLayoutAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(resetPerspectiveLayoutAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(resetPerspectiveLayoutAction, is(ResetPerspectiveLayoutAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),projectId, perspectiveId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(resetPerspectiveLayoutAction, is(not(ResetPerspectiveLayoutAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),mock(ProjectId.class), perspectiveId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_perspectiveId() {
        assertThat(resetPerspectiveLayoutAction, is(not(ResetPerspectiveLayoutAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),projectId, mock(PerspectiveId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(resetPerspectiveLayoutAction.hashCode(), is(ResetPerspectiveLayoutAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),projectId, perspectiveId)
                                                                                           .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(resetPerspectiveLayoutAction.toString(), Matchers.startsWith("ResetPerspectiveLayoutAction"));
    }
}
