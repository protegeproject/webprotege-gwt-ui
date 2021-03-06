
package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetProjectTagsResult_TestCase {

    private GetProjectTagsResult result;

    private Collection<Tag> tags;

    private Map<TagId, Integer> tagUsage;

    @Before
    public void setUp() {
        tags = Collections.singletonList(mock(Tag.class));
        tagUsage = ImmutableMap.of(mock(TagId.class), 1);
        result = GetProjectTagsResult.create(tags, tagUsage);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tags_IsNull() {
        GetProjectTagsResult.create(null, tagUsage);
    }

    @Test
    public void shouldReturnSupplied_tags() {
        assertThat(result.getTags(), is(this.tags));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tagUsage_IsNull() {
        GetProjectTagsResult.create(tags, null);
    }

    @Test
    public void shouldReturnSupplied_tagUsage() {
        assertThat(result.getTagUsage(), is(this.tagUsage));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(result));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(result.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(GetProjectTagsResult.create(tags, tagUsage)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tags() {
        assertThat(result, is(not(GetProjectTagsResult.create(ImmutableList.of(mock(Tag.class)), tagUsage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tagUsage() {
        assertThat(result, is(not(GetProjectTagsResult.create(tags, ImmutableMap.of(mock(TagId.class), 2)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(result.hashCode(), is(GetProjectTagsResult.create(tags, tagUsage).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(result.toString(), startsWith("GetProjectTagsResult"));
    }

}
