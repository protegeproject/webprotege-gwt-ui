
package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class MergeEntitiesResult_TestCase {

    private MergeEntitiesResult mergeEntitiesResult;
    @Mock
    private EventList<ProjectEvent<?>> eventList;

    @Before
    public void setUp() {
        mergeEntitiesResult = MergeEntitiesResult.create();
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(mergeEntitiesResult, is(mergeEntitiesResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(mergeEntitiesResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(mergeEntitiesResult, is(MergeEntitiesResult.create()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(mergeEntitiesResult.toString(), startsWith("MergeEntitiesResult"));
    }

}
