package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@ApplicationSingleton
@SuppressWarnings("GwtServiceNotRegistered")
public class DispatchServlet extends WebProtegeRemoteServiceServlet implements DispatchService  {

    @Nonnull
    private final DispatchServiceExecutor executor;

    @Nonnull
    private static Logger logger = LoggerFactory.getLogger(DispatchServlet.class);

    @Inject
    public DispatchServlet(@Nonnull WebProtegeLogger logger,
                           @Nonnull DispatchServiceExecutor executor) {
        super(logger);
        this.executor = checkNotNull(executor);
    }

    @Override
    public DispatchServiceResultContainer executeAction(Action action) throws ActionExecutionException, PermissionDeniedException {
        var request = getThreadLocalRequest();
        var principal = (KeycloakPrincipal<?>) request.getUserPrincipal();
        var context = principal.getKeycloakSecurityContext();
        var idToken = context.getIdToken();
        var userId = UserId.valueOf(idToken.getPreferredUsername());
        var executionContext = new ExecutionContext(userId,
                                                    context.getTokenString());
        return executor.execute(action, executionContext);
    }

    @Override
    public RpcWhiteList getRpcWhiteList(RpcWhiteList list) {
        return new RpcWhiteList();
    }
}
