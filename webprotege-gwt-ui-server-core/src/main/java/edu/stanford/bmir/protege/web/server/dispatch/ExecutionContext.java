package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.server.session.UserToken;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     Describes the context in which an action is being executed.
 * </p>
 */
public class ExecutionContext {

    private final UserId userId;

    private final String token;

    public ExecutionContext(UserId userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        return "ExecutionContext".hashCode();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ExecutionContext")
                .addValue(userId)
                          .toString();
    }
}
