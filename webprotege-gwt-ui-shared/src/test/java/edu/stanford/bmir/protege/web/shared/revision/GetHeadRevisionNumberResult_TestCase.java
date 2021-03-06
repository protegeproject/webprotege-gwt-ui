package edu.stanford.bmir.protege.web.shared.revision;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetHeadRevisionNumberResult_TestCase {


    private GetHeadRevisionNumberResult result;

    private GetHeadRevisionNumberResult otherResult;

    @Mock
    private RevisionNumber revisionNumber;


    @Before
    public void setUp() throws Exception {
        result = GetHeadRevisionNumberResult.create(revisionNumber);
        otherResult = GetHeadRevisionNumberResult.create(revisionNumber);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        GetHeadRevisionNumberResult.create(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(equalTo(result)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(equalTo(otherResult)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(result.hashCode(), is(otherResult.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(result.toString(), startsWith("GetHeadRevisionNumberResult"));
    }

    @Test
    public void shouldReturnSupplied_RevisionNumber() {
        assertThat(result.getRevisionNumber(), is(revisionNumber));
    }
}