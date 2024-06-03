package edu.stanford.bmir.protege.web.server.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var logout = getEnvVariable("webprotege.logoutUrl").orElse("http://webprotege-local.edu/auth/realms/webprotege/protocol/openid-connect/logout");
        var redirectUrl = getEnvVariable("webprotege.logoutRedirectUrl").orElse("http://webprotege-local.edu/webprotege");
        // Invalidate the local session
        request.getSession().invalidate();

        // Redirect to Keycloak logout endpoint
        String logoutUrl = logout + "?redirect_uri=" + redirectUrl;

        response.sendRedirect(logoutUrl);
    }


    private Optional<String> getEnvVariable(String path) {
        String env = System.getenv(path);
        return Optional.ofNullable(env);
    }
}