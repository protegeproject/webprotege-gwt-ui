package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(TextControlDescriptor.TYPE)
public class TextControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "TEXT";

    private LanguageMap placeholder = LanguageMap.empty();

    private StringType stringType = StringType.SIMPLE_STRING;

    private String specificLangTag = "";

    private LineMode lineMode = LineMode.SINGLE_LINE;

    private String pattern = "";

    private LanguageMap patternViolationErrorMessage = LanguageMap.empty();

    private TextControlDescriptor() {
    }

    public TextControlDescriptor(@JsonProperty(PropertyNames.PLACEHOLDER) @Nonnull LanguageMap placeholder,
                                 @JsonProperty(PropertyNames.STRING_TYPE) @Nonnull StringType stringType,
                                 @JsonProperty(PropertyNames.SPECIFIC_LANG_TAG) @Nonnull String specificLangTag,
                                 @JsonProperty(PropertyNames.LINE_MODE) @Nonnull LineMode lineMode,
                                 @JsonProperty(PropertyNames.PATTERN) @Nonnull String pattern,
                                 @JsonProperty(PropertyNames.PATTERN_VIOLATION_ERROR_MESSAGE) @Nonnull LanguageMap patternViolationErrorMessage) {
        this.placeholder = checkNotNull(placeholder);
        this.specificLangTag = checkNotNull(specificLangTag);
        this.stringType = checkNotNull(stringType);
        this.lineMode = checkNotNull(lineMode);
        this.pattern = checkNotNull(pattern);
        this.patternViolationErrorMessage = checkNotNull(patternViolationErrorMessage);
    }

    @Nonnull
    public static String getType() {
        return TYPE;
    }

    public static TextControlDescriptor getDefault() {
        return new TextControlDescriptor(
                LanguageMap.empty(),
                StringType.SIMPLE_STRING,
                "",
                LineMode.SINGLE_LINE,
                "",
                LanguageMap.empty()
        );
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof TextControlDescriptor)) {
            return false;
        }
        TextControlDescriptor other = (TextControlDescriptor) obj;
        return this.placeholder.equals(other.placeholder)
                && this.stringType.equals(other.stringType)
                && this.pattern.equals(other.pattern)
                && this.patternViolationErrorMessage.equals(other.patternViolationErrorMessage)
                && this.lineMode.equals(other.lineMode);
    }

    @Override
    public String toString() {
        return toStringHelper("TextControlDescriptor")
                .add("placeholder", placeholder)
                .add("stringType", stringType)
                .add("pattern", pattern)
                .add("patternViolationErrorMessage", patternViolationErrorMessage)
                .add("lineMode", lineMode)
                .toString();
    }

    @Nonnull
    @Override
    @JsonIgnore
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    @JsonProperty(PropertyNames.LINE_MODE)
    public LineMode getLineMode() {
        return lineMode;
    }

    @Nonnull
    @JsonProperty(PropertyNames.PATTERN)
    public String getPattern() {
        return pattern;
    }

    @Nonnull
    @JsonProperty(PropertyNames.PATTERN_VIOLATION_ERROR_MESSAGE)
    public LanguageMap getPatternViolationErrorMessage() {
        return patternViolationErrorMessage;
    }

    @Nonnull
    @JsonProperty(PropertyNames.PLACEHOLDER)
    public LanguageMap getPlaceholder() {
        return placeholder;
    }

    @Nonnull
    @JsonProperty(PropertyNames.SOURCE_TYPE)
    public StringType getStringType() {
        return stringType;
    }

    @Nonnull
    @JsonProperty(PropertyNames.SPECIFIC_LANG_TAG)
    public String getSpecificLangTag() {
        return specificLangTag;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(placeholder, stringType, pattern, patternViolationErrorMessage, lineMode);
    }
}
