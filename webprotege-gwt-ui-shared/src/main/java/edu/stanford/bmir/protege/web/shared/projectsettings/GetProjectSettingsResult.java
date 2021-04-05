package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetProjectSettingsResult implements Result {

    public static GetProjectSettingsResult get(@Nonnull ProjectSettings settings) {
        return new AutoValue_GetProjectSettingsResult(settings);
    }

    /**
     * Gets the {@link edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings}.
     * @return The project settings.  Not {@code null}.
     */
    public abstract ProjectSettings getProjectSettings();
}

