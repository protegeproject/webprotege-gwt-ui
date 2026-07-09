
package edu.stanford.bmir.protege.web.shared.watches;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SetEntityWatchesAction_TestCase {

    private SetEntityWatchesAction setEntityWatchesAction;
    @Mock
    private ChangeRequestId changeRequestId;
    @Mock
    private ProjectId projectId;
    @Mock
    private UserId userId;
    @Mock
    private OWLEntity entity;
    @Mock
    private ImmutableSet watches;

    @Before
    public void setUp()
    {
        setEntityWatchesAction = SetEntityWatchesAction.create(changeRequestId, projectId, userId, entity, watches);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_changeRequestId_IsNull() {
        SetEntityWatchesAction.create(null, projectId, userId, entity, watches);
    }

    @Test
    public void shouldReturnSupplied_changeRequestId() {
        assertThat(setEntityWatchesAction.getChangeRequestId(), is(this.changeRequestId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        SetEntityWatchesAction.create(changeRequestId, null, userId, entity, watches);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(setEntityWatchesAction.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        SetEntityWatchesAction.create(changeRequestId, projectId, null, entity, watches);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(setEntityWatchesAction.getUserId(), is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        SetEntityWatchesAction.create(changeRequestId, projectId, userId, null, watches);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(setEntityWatchesAction.getEntity(), is(this.entity));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_watches_IsNull() {
        SetEntityWatchesAction.create(changeRequestId, projectId, userId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_watches() {
        assertThat(setEntityWatchesAction.getWatches(), is(this.watches));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setEntityWatchesAction, is(setEntityWatchesAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(setEntityWatchesAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setEntityWatchesAction, is(SetEntityWatchesAction.create(changeRequestId, projectId, userId, entity, watches)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(setEntityWatchesAction, is(not(SetEntityWatchesAction.create(changeRequestId, mock(ProjectId.class), userId, entity, watches))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(setEntityWatchesAction, is(not(SetEntityWatchesAction.create(changeRequestId, projectId, mock(UserId.class), entity, watches))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(setEntityWatchesAction, is(not(SetEntityWatchesAction.create(changeRequestId, projectId, userId, mock(OWLEntity.class), watches))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_watches() {
        assertThat(setEntityWatchesAction, is(not(SetEntityWatchesAction.create(changeRequestId, projectId, userId, entity, mock(ImmutableSet.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(setEntityWatchesAction.hashCode(), is(SetEntityWatchesAction.create(changeRequestId, projectId, userId, entity, watches)
                                                                               .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(setEntityWatchesAction.toString(), Matchers.startsWith("SetEntityWatchesAction"));
    }

}
