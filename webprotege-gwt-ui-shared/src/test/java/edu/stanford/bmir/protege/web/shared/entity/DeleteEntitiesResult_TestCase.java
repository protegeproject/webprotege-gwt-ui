
package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DeleteEntitiesResult_TestCase {

    private DeleteEntitiesResult deleteEntitiesResult;

    private Set<OWLEntity> deletedEntities;

    @Before
    public void setUp() {
        deletedEntities = new HashSet<>();
        deletedEntities.add(mock(OWLEntity.class));
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
        assertThat(deleteEntitiesResult, is(not(new DeleteEntitiesResult(new HashSet<>()))));
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
