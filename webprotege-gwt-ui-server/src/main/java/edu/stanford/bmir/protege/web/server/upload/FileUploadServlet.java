package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
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
 * Date: 19/01/2012
 * <p>
 * A servlet for uploading files to web protege.
 * </p>
 * <p>
 *     If the upload succeeds then the server returns an HTTP 201 response (indicating that a new resource was created
 *     on the server) and the body of the response consists of the name of the file resource.  The upload will fail if
 *     the content encoding is not multi-part. In this case, the server will return an HTTP 400 response code
 *     (indicating that the request was not well formed).  If the file could not be created on the server for what ever
 *     reason, the server will return an HTTP 500 response code (internal server error) and the body of the response
 *     will contain an message that describes the problem.
 * </p>
 */
@ApplicationSingleton
public class FileUploadServlet extends HttpServlet {

    public static final Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);

    public static final String RESPONSE_MIME_TYPE = "text/html";

    @Inject
    public FileUploadServlet() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(req.getSession());
        logger.info("Received upload request from {} at {}",
                    webProtegeSession.getUserInSession(),
                    formatAddr(req));
        resp.setHeader("Content-Type", RESPONSE_MIME_TYPE);
    }
}
