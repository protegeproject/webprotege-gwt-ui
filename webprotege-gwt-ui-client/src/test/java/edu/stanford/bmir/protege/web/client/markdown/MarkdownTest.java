package edu.stanford.bmir.protege.web.client.markdown;

import org.junit.Test;

import static org.junit.Assert.*;

public class MarkdownTest {

    @Test
    public void shouldRecognizeMarkdownPrefix_mdColon() {
        assertTrue(Markdown.isMarkdown("md:# Header"));
    }

    @Test
    public void shouldRecognizeMarkdownPrefix_markdownColon() {
        assertTrue(Markdown.isMarkdown("markdown:*italic*"));
    }

    @Test
    public void shouldRecognizeMarkdownPrefix_bangMarkdown() {
        assertTrue(Markdown.isMarkdown("!markdown**bold**"));
    }

    @Test
    public void shouldRecognizeMarkdownPrefix_shebangMd() {
        assertTrue(Markdown.isMarkdown("#!mdSome text"));
    }

    @Test
    public void shouldNotRecognizeMarkdownWithoutPrefix() {
        assertFalse(Markdown.isMarkdown("# Just a regular heading"));
        assertFalse(Markdown.isMarkdown("plain text"));
    }

    @Test
    public void shouldStripMdColonPrefix() {
        String input = "md:# Header";
        assertEquals("# Header", Markdown.stripMarkdownPrefix(input));
    }

    @Test
    public void shouldStripMarkdownColonPrefix() {
        String input = "markdown:*italic*";
        assertEquals("*italic*", Markdown.stripMarkdownPrefix(input));
    }

    @Test
    public void shouldStripBangMarkdownPrefix() {
        String input = "!markdown**bold**";
        assertEquals("**bold**", Markdown.stripMarkdownPrefix(input));
    }

    @Test
    public void shouldStripShebangMdPrefix() {
        String input = "#!mdSome text";
        assertEquals("Some text", Markdown.stripMarkdownPrefix(input));
    }

    @Test
    public void shouldReturnOriginalTextWhenNoPrefix() {
        String input = "No markdown prefix here";
        assertEquals("No markdown prefix here", Markdown.stripMarkdownPrefix(input));
    }
}