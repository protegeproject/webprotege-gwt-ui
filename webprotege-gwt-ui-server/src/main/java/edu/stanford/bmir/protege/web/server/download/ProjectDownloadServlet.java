package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionFactory;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static edu.stanford.bmir.protege.web.server.logging.RequestFormatter.formatAddr;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 * <p>
 * A servlet which allows ontologies to be downloaded from WebProtege.
 * </p>
 */
@ApplicationSingleton
public class ProjectDownloadServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDownloadServlet.class);

    @Inject
    public ProjectDownloadServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebProtegeSession webProtegeSession = WebProtegeSessionFactory.getSession(req);
        UserId userId = webProtegeSession.getUserInSession();
        FileDownloadParameters downloadParameters = new FileDownloadParameters(req);
        if(!downloadParameters.isProjectDownload()) {
            logger.debug("Bad project download request from {} at {}.  Request URI: {}  Query String: {}",
                        webProtegeSession.getUserInSession(),
                        formatAddr(req),
                        req.getRequestURI(),
                        req.getQueryString());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        logger.debug("Received download request from {} at {} for project {}",
                    userId,
                    formatAddr(req),
                    downloadParameters.getProjectId());

    }
}
