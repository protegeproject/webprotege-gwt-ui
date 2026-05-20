
package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.event.EventId;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
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
public class PermissionsChangedEvent_TestCase {

    private PermissionsChangedEvent permissionsChangedEvent;

    @Mock
    private ProjectId source;

    @Before
    public void setUp() {
        permissionsChangedEvent = new PermissionsChangedEvent(EventId.generate(), source);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_source_IsNull() {
        new PermissionsChangedEvent(EventId.generate(), null);
    }

    @Test
    public void shouldReturnSupplied_source() {
        assertThat(permissionsChangedEvent.getSource(), is(this.source));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(permissionsChangedEvent, is(permissionsChangedEvent));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(permissionsChangedEvent.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(permissionsChangedEvent, is(new PermissionsChangedEvent(EventId.generate(), source)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_source() {
        assertThat(permissionsChangedEvent, is(not(new PermissionsChangedEvent(EventId.generate(), mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(permissionsChangedEvent.hashCode(), is(new PermissionsChangedEvent(EventId.generate(), source).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(permissionsChangedEvent.toString(), startsWith("PermissionsChangedEvent"));
    }
}
