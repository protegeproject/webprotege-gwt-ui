
package edu.stanford.bmir.protege.web.shared.change;

import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
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
public class RevertRevisionResult_TestCase {

    private RevertRevisionResult revertRevisionResult;

    @Mock
    private ProjectId projectId;

    @Mock
    private EventList<ProjectEvent<?>> eventList;

    @Mock
    private RevisionNumber revisionNumber;

    @Before
    public void setUp() {
        revertRevisionResult = RevertRevisionResult.create(projectId, revisionNumber);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        RevertRevisionResult.create(null, revisionNumber);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(revertRevisionResult.getProjectId(), Matchers.is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(revertRevisionResult));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(revertRevisionResult.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(RevertRevisionResult.create(projectId, revisionNumber)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(Matchers.not(RevertRevisionResult.create(Mockito.mock(ProjectId.class), revisionNumber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_eventList() {
        MatcherAssert.assertThat(revertRevisionResult, Matchers.is(Matchers.not(RevertRevisionResult.create(projectId, revisionNumber))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(revertRevisionResult.hashCode(), Matchers.is(RevertRevisionResult.create(projectId, revisionNumber).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(revertRevisionResult.toString(), Matchers.startsWith("RevertRevisionResult"));
    }
}
