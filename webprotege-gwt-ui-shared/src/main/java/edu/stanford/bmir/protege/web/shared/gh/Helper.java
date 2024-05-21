package edu.stanford.bmir.protege.web.shared.gh;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-20
 */
public class Helper {

    @SuppressWarnings("ReplaceNullCheck")
    @Nonnull
    public static <T> T requireNonNullElse(@Nullable T value, @Nonnull T defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        else {
            return value;
        }
    }
}
