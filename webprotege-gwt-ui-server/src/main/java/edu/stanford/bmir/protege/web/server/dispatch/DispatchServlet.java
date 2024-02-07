package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@ApplicationSingleton
public class DispatchServlet extends WebProtegeRemoteServiceServlet implements DispatchService  {
    private final static java.util.logging.Logger logger = Logger.getLogger("DispatchServiceManager");

    @Nonnull
    private final DispatchServiceExecutor executor;

    @Inject
    public DispatchServlet(@Nonnull WebProtegeLogger logger,
                           @Nonnull DispatchServiceExecutor executor) {
        this.executor = checkNotNull(executor);
    }

    @Override
    public DispatchServiceResultContainer executeAction(Action action) throws ActionExecutionException, PermissionDeniedException {
        logger.info("Alex tocmai ce intru in execute action " + action.getClass());
        var request = getThreadLocalRequest();
        var principal = (KeycloakPrincipal<?>) request.getUserPrincipal();
        var context = principal.getKeycloakSecurityContext();
        var idToken = context.getIdToken();
        var userId = UserId.valueOf(idToken.getPreferredUsername());
        var executionContext = new ExecutionContext(userId,
                                                    context.getTokenString());
        logger.info("ALEX execute with executor " + executor.getClass() + " principal " + principal + " request " + request + " userid " + userId + "context " + executionContext + " action " + action.getClass()) ;
        return executor.execute(action, executionContext);
    }

    @Override
    public RpcWhiteList getRpcWhiteList(RpcWhiteList list) {
        return new RpcWhiteList();
    }
}
