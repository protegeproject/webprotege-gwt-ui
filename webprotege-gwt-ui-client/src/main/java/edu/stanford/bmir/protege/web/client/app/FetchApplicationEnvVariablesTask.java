package edu.stanford.bmir.protege.web.client.app;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.FetchAppEnvVariables;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class FetchApplicationEnvVariablesTask implements ApplicationInitManager.ApplicationInitializationTask {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ApplicationEnvironmentManager applicationEnvironmentManager;

    @Inject
    public FetchApplicationEnvVariablesTask(@Nonnull DispatchServiceManager dispatch, @Nonnull ApplicationEnvironmentManager applicationEnvironmentManager) {
        this.dispatch = dispatch;
        this.applicationEnvironmentManager = applicationEnvironmentManager;
    }

    @Override
    public void run(ApplicationInitManager.ApplicationInitTaskCallback callback) {
        dispatch.execute(new FetchAppEnvVariables(), result -> {
            applicationEnvironmentManager.loadAppEnvVariables(result);
            callback.taskComplete();
        });
    }

    @Override
    public String getName() {
        return "FetchApplicationEnvVariablesTask";
    }
}
