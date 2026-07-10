package edu.stanford.bmir.protege.web.client.form;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Regression tests for #281: a label with text but no explicit language tag must not
 * be silently dropped when the row is read back out of the editor.
 */
@RunWith(MockitoJUnitRunner.class)
public class LanguageMapEntryPresenter_TestCase {

    private LanguageMapEntryPresenter presenter;

    @Mock
    private LanguageMapEntryView view;

    @Before
    public void setUp() {
        presenter = new LanguageMapEntryPresenter(view);
    }

    @Test
    public void shouldReturnEmptyWhenValueAndLangTagAreBlank() {
        when(view.getValue()).thenReturn("");
        when(view.getLangTag()).thenReturn("");
        assertThat(presenter.getValue(), is(Optional.empty()));
        assertThat(presenter.isWellFormed(), is(false));
    }

    @Test
    public void shouldDefaultToEnglishWhenValueIsSetButLangTagIsBlank() {
        when(view.getValue()).thenReturn("TestForm");
        when(view.getLangTag()).thenReturn("");
        assertThat(presenter.getValue(), is(Optional.of(LanguageMapEntry.get("en", "TestForm"))));
        assertThat(presenter.isWellFormed(), is(true));
    }

    @Test
    public void shouldUseTheExplicitLangTagWhenOneIsProvided() {
        when(view.getValue()).thenReturn("TestForm");
        when(view.getLangTag()).thenReturn("fr");
        assertThat(presenter.getValue(), is(Optional.of(LanguageMapEntry.get("fr", "TestForm"))));
    }
}
