
package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DeleteEntitiesResult_TestCase {

    private DeleteEntitiesResult deleteEntitiesResult;

    private ImmutableSet<OWLEntityData> deletedEntities;

    @Before
    public void setUp() {
        deletedEntities = ImmutableSet.of(mock(OWLEntityData.class));
        deleteEntitiesResult = new DeleteEntitiesResult(deletedEntities);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_deletedEntities_IsNull() {
        new DeleteEntitiesResult(null);
    }

    @Test
    public void shouldReturnSupplied_deletedEntities() {
        assertThat(deleteEntitiesResult.getDeletedEntities(), is(this.deletedEntities));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(deleteEntitiesResult, is(deleteEntitiesResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(deleteEntitiesResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(deleteEntitiesResult, is(new DeleteEntitiesResult(deletedEntities)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_deletedEntities() {
        assertThat(deleteEntitiesResult, is(not(new DeleteEntitiesResult(ImmutableSet.of()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(deleteEntitiesResult.hashCode(), is(new DeleteEntitiesResult(deletedEntities).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(deleteEntitiesResult.toString(), Matchers.startsWith("DeleteEntitiesResult"));
    }
}
