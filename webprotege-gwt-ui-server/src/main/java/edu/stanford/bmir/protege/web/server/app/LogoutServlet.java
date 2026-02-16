package edu.stanford.bmir.protege.web.server.app;



import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Invalidate local session
        request.getSession().invalidate();

        // Redirect to Keycloak's end-session endpoint with redirect URI
        response.sendRedirect("/keycloak/realms/webprotege/protocol/openid-connect/logout");
    }
}