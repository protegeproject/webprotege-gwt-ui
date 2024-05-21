package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-15
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum GitHubReaction {

    @JsonProperty("+1")
    PLUS_1("+1", "\uD83D\uDC4D"),

    @JsonProperty("-1")
    MINUS_1("-1", "\uD83D\uDC4E"),

    @JsonProperty("laugh")
    LAUGH("laugh", "\uD83D\uDE04"),

    @JsonProperty("confused")
    CONFUSED("confused", "\uD83D\uDE15"),

    @JsonProperty("heart")
    HEART("heart", "❤\uFE0F"),

    @JsonProperty("hooray")
    HOORAY("hooray", "\uD83C\uDF89"),

    @JsonProperty("rocket")
    ROCKET("rocket", "\uD83D\uDE80"),

    @JsonProperty("eyes")
    EYES("eyes", "\uD83D\uDC40");

    private String text;

    private String emoji;

    GitHubReaction(String text, String emoji) {
        this.text = text;
        this.emoji = emoji;
    }

    public String getText() {
        return text;
    }

    public String getEmoji() {
        return emoji;
    }
}
