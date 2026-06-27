
package edu.stanford.bmir.protege.web.shared.change;

import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RevertRevisionAction_TestCase {

    private RevertRevisionAction revertRevisionAction;

    @Mock
    private ProjectId projectId;

    @Mock
    private RevisionNumber revisionNumber;

    @Before
    public void setUp()
        throws Exception
    {
        revertRevisionAction = RevertRevisionAction.create(ChangeRequestId.generate(), projectId, revisionNumber);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        RevertRevisionAction.create(ChangeRequestId.generate(), null, revisionNumber);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(revertRevisionAction.getProjectId(), Matchers.is(this.projectId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_revisionNumber_IsNull() {
        RevertRevisionAction.create(ChangeRequestId.generate(), projectId, null);
    }

    @Test
    public void shouldReturnSupplied_revisionNumber() {
        MatcherAssert.assertThat(revertRevisionAction.getRevisionNumber(), Matchers.is(this.revisionNumber));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(revertRevisionAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(revertRevisionAction.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(RevertRevisionAction.create(ChangeRequestId.generate(), projectId, revisionNumber)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(Matchers.not(RevertRevisionAction.create(ChangeRequestId.generate(), Mockito.mock(ProjectId.class), revisionNumber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_revisionNumber() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(Matchers.not(RevertRevisionAction.create(ChangeRequestId.generate(), projectId, Mockito.mock(RevisionNumber.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(revertRevisionAction.hashCode(), Matchers.is(RevertRevisionAction.create(ChangeRequestId.generate(), projectId, revisionNumber)
                                                                                                  .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(revertRevisionAction.toString(), Matchers.startsWith("RevertRevisionAction"));
    }


}
