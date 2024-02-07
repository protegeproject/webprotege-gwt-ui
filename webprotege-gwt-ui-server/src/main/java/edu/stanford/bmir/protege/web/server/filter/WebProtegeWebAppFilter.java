package edu.stanford.bmir.protege.web.server.filter;

import edu.stanford.bmir.protege.web.server.app.GwtResourceCachingStrategy;
import edu.stanford.bmir.protege.web.server.app.ResourceCachingManager;
import edu.stanford.bmir.protege.web.server.filemanager.ConfigInputStreamSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/01/2013
 */
public class WebProtegeWebAppFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(WebProtegeWebAppFilter.class);

    public ResourceCachingManager cachingManager = new ResourceCachingManager(new GwtResourceCachingStrategy());

    public static void setError(Throwable t) {
    }


    /**
     * Called by the web container to indicate to a filter that it is being placed into
     * service. The servlet container calls the init method exactly once after instantiating the
     * filter. The init method must complete successfully before the filter is asked to do any
     * filtering work. <br><br>
     *
     * The web container cannot place the filter into service if the init method either<br>
     * 1.Throws a ServletException <br>
     * 2.Does not return within a time period defined by the web container
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the container
     * each time a request/response pair is passed through the chain due
     * to a client request for a resource at the end of the chain. The FilterChain passed in to this
     * method allows the Filter to pass on the request and response to the next entity in the
     * chain.<p>
     * A typical implementation of this method would follow the following pattern:- <br>
     * 1. Examine the request<br>
     * 2. Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering <br>
     * 3. Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering <br>
     * 4. a) <strong>Either</strong> translate the next entity in the chain using the FilterChain object
     * (<code>chain.doFilter()</code>), <br>
     * * 4. b) <strong>or</strong> not pass on the request/response pair to the next entity in the filter chain to block
     * the request processing<br>
     * * 5. Directly set headers on the response after invocation of the next entity in the filter chain.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            HttpServletResponse httpRes = (HttpServletResponse) response;
            logger.info("ALEX " + httpReq.getUserPrincipal().toString());

            doHttpFilter(httpReq, httpRes);
        }
    }

    private void doHttpFilter(HttpServletRequest httpReq, HttpServletResponse httpRes) {
        cachingManager.setAppropriateResponseHeaders(httpReq, httpRes);
    }

    /**
     * Called by the web container to indicate to a filter that it is being taken out of service. This
     * method is only called once all threads within the filter's doFilter method have exited or after
     * a timeout period has passed. After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter. <br><br>
     *
     * This method gives the filter an opportunity to clean up any resources that are being held (for
     * example, memory, file handles, threads) and make sure that any persistent state is synchronized
     * with the filter's current state in memory.
     */
    @Override
    public void destroy() {
    }



}
