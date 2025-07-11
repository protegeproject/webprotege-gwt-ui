package edu.stanford.bmir.protege.web.client.markdown;

import java.util.LinkedHashSet;
import java.util.Set;

public class Markdown {

    private static final Set<String> prefixes = new LinkedHashSet<>();

    static {
        prefixes.add("!markdown");
        prefixes.add("#!md");
        prefixes.add("markdown:");
        prefixes.add("md:");
    }

    public static boolean isMarkdown(String markdown) {
        return prefixes.stream().anyMatch(markdown::startsWith);
    }

    public static String stripMarkdownPrefix(String markdown) {
        return prefixes.stream().filter(markdown::startsWith).map(prefix -> markdown.substring(prefix.length())).findFirst().orElse(markdown);
    }
}
