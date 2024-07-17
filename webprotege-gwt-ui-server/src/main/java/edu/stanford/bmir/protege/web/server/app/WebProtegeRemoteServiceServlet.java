package edu.stanford.bmir.protege.web.server.app;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.UserToken;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionFactory;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.app.StatusCodes;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 * <p>
 * A base for web-protege services.  This class provides useful functionality for checking that a caller is logged in,
 * checking whether the caller is the project owner etc.
 * </p>
 */
public abstract class WebProtegeRemoteServiceServlet extends RemoteServiceServlet {


    private final static java.util.logging.Logger logger = Logger.getLogger("WebProtegeRemoteServiceServlet");

    /**
     * Gets the userId for the client associated with the current thread local request.
     *
     * @return The UserId.  Not null (if no user is logged in then the value specified by {@link edu.stanford.bmir.protege.web.shared.user.UserId#getGuest()} ()}
     *         will be returned.
     */
    public UserId getUserInSession() {
        return WebProtegeSessionFactory.getSession(getThreadLocalRequest())
                                       .getUserInSession();
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////
    //////  Logging
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String processCall(String payload) throws SerializationException {
        return super.processCall(payload);
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {
        HttpServletRequest request = getThreadLocalRequest();
        logger.log(Level.SEVERE, e.getMessage(), e);
        if(e instanceof SerializationException) {
            HttpServletResponse response = getThreadLocalResponse();
            response.reset();
            try {
                response.setContentType("text/plain");
                response.sendError(StatusCodes.UPDATED, "WebProtege has been updated. Please refresh your browser");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }
        }
        else {
            super.doUnexpectedFailure(e);
        }
    }

    @Override
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        // Taken from http://stackoverflow.com/a/3771391

        //get the base url from the header instead of the body this way
        //apache reverse proxy with rewrite on the header can work

        String moduleBaseURLHdr = request.getHeader("X-GWT-Module-Base");
        if(moduleBaseURLHdr != null){
            moduleBaseURL = moduleBaseURLHdr;
        }
        return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
    }
}
