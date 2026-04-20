package edu.stanford.bmir.protege.web.client.role;

import javax.inject.Inject;

public final class ParentRoleSelectorViewImplFactory {
  @Inject
  public ParentRoleSelectorViewImplFactory() {}

  public ParentRoleSelectorViewImpl create() {
    return new ParentRoleSelectorViewImpl();
  }
}
