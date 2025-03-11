package edu.stanford.bmir.protege.web.shared.access;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class BasicCapability_TestCase {

    private BasicCapability basicCapability;

    private String id = "The id";

    @Before
    public void setUp() {
        basicCapability = new BasicCapability(id);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new BasicCapability(null);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(basicCapability.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(basicCapability, is(basicCapability));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(basicCapability.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(basicCapability, is(new BasicCapability(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(basicCapability, is(not(new BasicCapability("String-49f80fc5-f0c4-4013-accc-4f37f60d5632"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(basicCapability.hashCode(), is(new BasicCapability(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(basicCapability.toString(), Matchers.startsWith("BasicCapability"));
    }

}
