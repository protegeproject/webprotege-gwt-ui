package edu.stanford.bmir.protege.web.server.app;

import dagger.Component;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServlet;
import edu.stanford.bmir.protege.web.server.download.ProjectDownloadServlet;
import edu.stanford.bmir.protege.web.server.inject.ApplicationModule;
import edu.stanford.bmir.protege.web.server.upload.FileUploadServlet;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.inject.SharedApplicationModule;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
@Component(modules = {
        ApplicationModule.class,
        SharedApplicationModule.class,
        ServerModule.class
})
@ApplicationSingleton
public interface ServerComponent {

    ApplicationNameSupplier getApplicationNameSupplier();

    ApplicationSettingsChecker getApplicationSettingsChecker();

    FileUploadServlet getFileUploadServlet();

    ProjectDownloadServlet getProjectDownloadServlet();

    DispatchServlet getDispatchServlet();

    UserDetailsManager getUserDetailsManager();

    AccessManager getAccessManager();
}
