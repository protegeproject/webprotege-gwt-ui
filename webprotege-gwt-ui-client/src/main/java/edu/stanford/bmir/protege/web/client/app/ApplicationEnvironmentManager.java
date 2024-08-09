package edu.stanford.bmir.protege.web.client.app;


import edu.stanford.bmir.protege.web.shared.dispatch.actions.AppEnvVariables;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.inject.Inject;

@ApplicationSingleton
public class ApplicationEnvironmentManager {

  private AppEnvVariables appEnvVariables;

  @Inject
  public ApplicationEnvironmentManager() {

  }

  public void loadAppEnvVariables(AppEnvVariables envVariables) {
    this.appEnvVariables = envVariables;
  }


  public AppEnvVariables getAppEnvVariables() {
    return appEnvVariables;
  }
}
