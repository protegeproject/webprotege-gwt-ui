package edu.stanford.bmir.protege.web.shared.app;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
public class SetApplicationSettingsResult implements Result {

    private SetApplicationSettingsResult() {
    }

    public static SetApplicationSettingsResult create() {
        return new SetApplicationSettingsResult();
    }
}
