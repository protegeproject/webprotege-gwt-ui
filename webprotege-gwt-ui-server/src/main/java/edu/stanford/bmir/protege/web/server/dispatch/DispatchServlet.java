package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.jetbrains.annotations.NotNull;
import org.keycloak.KeycloakPrincipal;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public DispatchServlet(@Nonnull DispatchServiceExecutor executor) {
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
        return executeAction(action, executionContext);
    }

    private DispatchServiceResultContainer executeAction(Action action,
                                                         ExecutionContext executionContext) {
        if(action instanceof BatchAction) {
            return executeBatchAction((BatchAction) action, executionContext);
        }
        else {
            return executor.execute(action, executionContext);
        }
    }

    @NotNull
    private DispatchServiceResultContainer executeBatchAction(BatchAction action,
                                                              ExecutionContext executionContext) {
        // The results are returned in the same order as the actions.  This is how results are matched
        // up with actions
        var individualActions = action.getActions();
        logger.info("Executing a batch action that contains " + individualActions.size() + " individual actions");
        var resultList = Stream.generate(() -> null)
                               .limit(individualActions.size())
                               .map(o -> (ActionExecutionResult) o)
                               .collect(Collectors.toList());
        try {
            var future = createCompletableFutureForBatchAction(individualActions, resultList, executionContext);
            long t0 = System.currentTimeMillis();
            future.get();
            long t1 = System.currentTimeMillis();
            logger.info("Finished execution of batch action in " + (t1 - t0) + " ms");
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Error while waiting for completions", e);
            throw new ActionExecutionException("Error: " + e.getMessage());
        }
        return DispatchServiceResultContainer.create(BatchResult.get(ImmutableList.copyOf(resultList)));
    }

    private CompletableFuture<Void> createCompletableFutureForBatchAction(ImmutableList<Action<?>> individualActions,
                                                       List<ActionExecutionResult> resultList,
                                                       ExecutionContext executionContext) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < individualActions.size(); i++) {
            final int index = i;
            var future = CompletableFuture.runAsync(() -> {
                var innerAction = individualActions.get(index);
                try {
                    long t0 = System.currentTimeMillis();
                    var innerResult = executeAction(innerAction, executionContext);
                    long t1 = System.currentTimeMillis();
                    logger.info("    Executed " + innerAction.getClass().getSimpleName() + " in " + (t1 - t0) + " ms");
                    var innerExecutionResult = ActionExecutionResult.get(innerResult);
                    resultList.set(index, innerExecutionResult);
                } catch (ActionExecutionException e) {
                    resultList.add(ActionExecutionResult.get(e));
                } catch (PermissionDeniedException e) {
                    resultList.add(ActionExecutionResult.get(e));
                }
            });
            futures.add(future);
        }
        var cfs = futures.toArray(new CompletableFuture[0]);
        return CompletableFuture.allOf(cfs);
    }


    @Override
    public RpcWhiteList getRpcWhiteList(RpcWhiteList list) {
        return new RpcWhiteList();
    }
}
