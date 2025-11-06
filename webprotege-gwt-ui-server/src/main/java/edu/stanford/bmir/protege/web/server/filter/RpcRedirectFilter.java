package edu.stanford.bmir.protege.web.server.filter;

// For javax.servlet.* (Servlet 3.x). For Jakarta 5+, change imports to jakarta.servlet.*
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class RpcRedirectFilter implements Filter {

    private static final Logger logger = Logger.getLogger(RpcRedirectFilter.class.getName());

    @Inject
    public RpcRedirectFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        logger.info("RpcRedirectFilter doFilter");

        var request = (HttpServletRequest) req;
        var response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        boolean isRpcPost = "POST".equalsIgnoreCase(request.getMethod())
                            && path.startsWith(request.getContextPath() + "/webprotege/dispatchservice");

        logger.info("Is rpc post: " + isRpcPost);

        // Optionally bypass for health checks or specific endpoints
        boolean isHealth = path.startsWith(request.getContextPath() + "/health");

        String enabled = Objects.requireNonNullElse(System.getenv("rpc.redirect.enabled"), "false");
        boolean redirectEnabled = Boolean.parseBoolean(enabled);
        String targetUrl = Objects.requireNonNullElse(System.getenv("rpc.redirect.to"), null);

        // Example: init params OR environment/system properties
        logger.info("Redirect enabled: " + redirectEnabled);
        logger.info("Redirect target: " + targetUrl);

        if (redirectEnabled && isRpcPost && !isHealth) {

            // Return 200 with a header; client RpcRequestBuilder will handle navigation.
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("X-Redirect-To", targetUrl);
            // Nobody; don't call chain -> prevents GWT-RPC decode attempts.
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}

