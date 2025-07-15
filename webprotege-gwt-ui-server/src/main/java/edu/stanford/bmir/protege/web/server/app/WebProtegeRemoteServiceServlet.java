package edu.stanford.bmir.protege.web.server.app;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            logger.log(Level.SEVERE, e.getMessage(), e);
            HttpServletResponse response = getThreadLocalResponse();
            response.reset();
            response.setContentType("text/plain");
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
