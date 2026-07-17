<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page import="javax.servlet.RequestDispatcher" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%
    // Reached whenever any endpoint in the app answers a request with
    // HTTP 400. Only redirect when the request itself looks like the
    // Keycloak OAuth callback (context root, with 'code' and 'state' query
    // params): that shape is what a stale state-cookie mismatch produces,
    // e.g. after a Keycloak required action (email verification, OTP
    // setup, etc.) or a concurrent auth challenge overwrote the cookie of
    // the login in progress. Anything else (e.g. ProjectDownloadServlet's
    // malformed-download-request check) keeps its genuine 400 status
    // instead of being silently redirected -- this page is a servlet-spec
    // error-page target, not an OAuth-specific one, so it must not swallow
    // unrelated 400s from elsewhere in the app.
    String contextPath = request.getContextPath();
    String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
    boolean isCallbackShape = requestUri != null
            && (requestUri.equals(contextPath) || requestUri.equals(contextPath + "/"))
            && request.getParameter("code") != null
            && request.getParameter("state") != null;

    if (isCallbackShape) {
        String target = contextPath + "/";
        // no-store keeps a proxy from caching this transient bounce past
        // the moment it applies.
        response.setHeader("Cache-Control", "no-store");
        response.sendRedirect(target);
%><html>
<head><title>Signing you in&hellip;</title></head>
<body><p>Signing you in&hellip; if nothing happens, <a href="<%= target %>">click here</a>.</p></body>
</html>
<%
    } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
%><html>
<head><title>HTTP Status 400 &ndash; Bad Request</title></head>
<body><h1>HTTP Status 400 &ndash; Bad Request</h1></body>
</html>
<%
    }
%>
