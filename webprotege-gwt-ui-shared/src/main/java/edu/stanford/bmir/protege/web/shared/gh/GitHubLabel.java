package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-12
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GitHubLabel implements IsSerializable {

    @JsonProperty("id")
    public abstract long id();

    @JsonProperty("node_id")
    public abstract String nodeId();

    @JsonProperty("url")
    public abstract String url();

    @JsonProperty("name")
    public abstract String name();

    @JsonProperty("color")
    public abstract String color();

    @JsonProperty("default")
    public abstract boolean isDefault();

    @JsonProperty("description")
    public abstract String description();

    @JsonCreator
    public static GitHubLabel get(@JsonProperty("id") long id,
                                  @JsonProperty("node_id") @Nullable String nodeId,
                                  @JsonProperty("url") @Nullable String url,
                                  @JsonProperty("name") @Nullable String name,
                                  @JsonProperty("color") @Nullable String color,
                                  @JsonProperty("default") boolean isDefault,
                                  @JsonProperty("description") @Nullable String description) {
        return new AutoValue_GitHubLabel(
                id,
                Helper.requireNonNullElse(nodeId, ""),
                Helper.requireNonNullElse(url, ""),
                Helper.requireNonNullElse(name, ""),
                Helper.requireNonNullElse(color, ""),
                isDefault,
                Helper.requireNonNullElse(description, ""));
    }

    @JsonIgnore
    public Color getTextColor() {
        String labelColor = color();
        Color bgColor = Color.getHex("#" + labelColor);
        return getRecommendedTextColor(bgColor);
    }

    /**
     * Calculates the perceived lightness of a given color.
     * @param color The background color.
     * @return The perceived lightness, in range [0, 1].
     */
    private static double calculatePerceivedLightness(Color color) {
        double r = color.getRed();
        double g = color.getGreen();
        double b = color.getBlue();

        return (0.2126 * r + 0.7152 * g + 0.0722 * b) / 255.0;
    }

    /**
     * Gets the recommended text color for a GitHub label background.
     * @param bgColor The label background color.
     * @return "white", "black", or "gray" if close to threshold.
     */
    public static Color getRecommendedTextColor(Color bgColor) {
        double lightness = calculatePerceivedLightness(bgColor);
        double threshold = 0.453;

        if (Math.abs(lightness - threshold) < 0.002) {
            return Color.getRGB(70, 70, 70); // Approximate behavior near threshold
        } else if (lightness < threshold) {
            return Color.getWhite();
        } else {
            return Color.get(0, 0, 0);
        }
    }
}
