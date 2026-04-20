package edu.stanford.bmir.protege.web.client.projectlist;

import edu.stanford.bmir.protege.web.client.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.projectmanager.LoadProjectInNewWindowRequestHandler;
import edu.stanford.bmir.protege.web.client.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import javax.inject.Inject;
import javax.inject.Provider;

public final class AvailableProjectPresenterFactory {
  private final Provider<AvailableProjectView> viewProvider;

  private final Provider<LoadProjectInNewWindowRequestHandler>
      loadProjectInNewWindowRequestHandlerProvider;

  private final Provider<TrashManagerRequestHandler> trashManagerRequestHandlerProvider;

  private final Provider<LoadProjectRequestHandler> loadProjectRequestHandlerProvider;

  private final Provider<DownloadProjectRequestHandler> downloadProjectRequestHandlerProvider;

  @Inject
  public AvailableProjectPresenterFactory(
      Provider<AvailableProjectView> viewProvider,
      Provider<LoadProjectInNewWindowRequestHandler> loadProjectInNewWindowRequestHandlerProvider,
      Provider<TrashManagerRequestHandler> trashManagerRequestHandlerProvider,
      Provider<LoadProjectRequestHandler> loadProjectRequestHandlerProvider,
      Provider<DownloadProjectRequestHandler> downloadProjectRequestHandlerProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.loadProjectInNewWindowRequestHandlerProvider =
        checkNotNull(loadProjectInNewWindowRequestHandlerProvider, 2);
    this.trashManagerRequestHandlerProvider = checkNotNull(trashManagerRequestHandlerProvider, 3);
    this.loadProjectRequestHandlerProvider = checkNotNull(loadProjectRequestHandlerProvider, 4);
    this.downloadProjectRequestHandlerProvider =
        checkNotNull(downloadProjectRequestHandlerProvider, 5);
  }

  public AvailableProjectPresenter create(AvailableProject project) {
    return new AvailableProjectPresenter(
        checkNotNull(project, 1),
        checkNotNull(viewProvider.get(), 2),
        checkNotNull(loadProjectInNewWindowRequestHandlerProvider.get(), 3),
        checkNotNull(trashManagerRequestHandlerProvider.get(), 4),
        checkNotNull(loadProjectRequestHandlerProvider.get(), 5),
        checkNotNull(downloadProjectRequestHandlerProvider.get(), 6));
  }

  private static <T> T checkNotNull(T reference, int argumentIndex) {
    if (reference == null) {
      throw new NullPointerException(
          "@AutoFactory method argument is null but is not marked @Nullable. Argument index: "
              + argumentIndex);
    }
    return reference;
  }
}
