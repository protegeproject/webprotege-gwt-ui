package edu.stanford.bmir.protege.web.server.auth;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Logging out the user");
        try {
            var logout = getEnvVariable("webprotege.keycloakLogoutUrl").orElse("http://webprotege-local.edu/keycloak-admin/realms/webprotege/protocol/openid-connect/logout");
            var redirectUrl = getEnvVariable("webprotege.logoutRedirectUrl").orElse("http://webprotege-local.edu/webprotege");

            request.getSession().invalidate();

            RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) request.getAttribute("org.keycloak.KeycloakSecurityContext");
            String refreshToken = context.getRefreshToken();

            String clientId = "webprotege";

            String requestBody = "client_id=" + clientId
                    + "&refresh_token=" + refreshToken
                    + "&post_logout_redirect_uri=" + redirectUrl;

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(logout);

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new StringEntity(requestBody));

            client.execute(httpPost);
            client.close();
        } catch (Exception e) {
            logger.error("ERROR logging out the user ", e);
        }
    }

    private Optional<String> getEnvVariable(String path) {
        String env = System.getenv(path);
        return Optional.ofNullable(env);
    }
}