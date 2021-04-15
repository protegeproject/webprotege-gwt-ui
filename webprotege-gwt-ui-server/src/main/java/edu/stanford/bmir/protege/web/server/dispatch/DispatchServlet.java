package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.auth.PerformLoginHandler;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.UserToken;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionFactory;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginAction;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

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
    private final PerformLoginHandler performLoginHandler;

    @Inject
    public DispatchServlet(@Nonnull WebProtegeLogger logger,
                           @Nonnull DispatchServiceExecutor executor,
                           @Nonnull PerformLoginHandler performLoginHandler) {
        super(logger);
        this.executor = checkNotNull(executor);
        this.performLoginHandler = checkNotNull(performLoginHandler);
    }

    @Override
    public DispatchServiceResultContainer executeAction(Action action) throws ActionExecutionException, PermissionDeniedException {
        // Special treatment of login action
        if(action instanceof PerformLoginAction) {
           // Login via the authenticate endpoint
           // Get the session id and set this as the usertoken in a session cookie
            var result = performLoginHandler.performLogin((PerformLoginAction) action, userToken -> {
                getThreadLocalResponse().addCookie(new Cookie(UserToken.COOKIE_NAME, userToken.getToken()));
            });
            return DispatchServiceResultContainer.create(result);
        }
        else {
            var webProtegeSession = WebProtegeSessionFactory.getSession(getThreadLocalRequest());
            var userId = webProtegeSession.getUserInSession();
            var requestContext = new RequestContext(userId);
            var executionContext = new ExecutionContext(webProtegeSession);
            return executor.execute(action, requestContext, executionContext);
        }
    }

    @Override
    public RpcWhiteList getRpcWhiteList(RpcWhiteList list) {
        return new RpcWhiteList();
    }
}
