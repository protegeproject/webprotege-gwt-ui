package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionFactory;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static edu.stanford.bmir.protege.web.server.logging.RequestFormatter.formatAddr;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 * <p>
 * A servlet which allows ontologies to be downloaded from WebProtege.
 * Triggers snapshot creation via the snapshot-generator-service, downloads
 * the result from MinIO, and streams it to the browser.
 * </p>
 */
@ApplicationSingleton
public class ProjectDownloadServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDownloadServlet.class);

    @Nonnull
    private final SnapshotRequestSender snapshotRequestSender;

    @Nonnull
    private final SnapshotDownloader snapshotDownloader;

    @Inject
    public ProjectDownloadServlet(@Nonnull SnapshotRequestSender snapshotRequestSender,
                                  @Nonnull SnapshotDownloader snapshotDownloader) {
        this.snapshotRequestSender = snapshotRequestSender;
        this.snapshotDownloader = snapshotDownloader;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final WebProtegeSession webProtegeSession = WebProtegeSessionFactory.getSession(req);
        UserId userId = webProtegeSession.getUserInSession();
        FileDownloadParameters downloadParameters = new FileDownloadParameters(req);
        if (!downloadParameters.isProjectDownload()) {
            logger.info("Bad project download request from {} at {}.  Request URI: {}  Query String: {}",
                        webProtegeSession.getUserInSession(),
                        formatAddr(req),
                        req.getRequestURI(),
                        req.getQueryString());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        var projectId = downloadParameters.getProjectId();
        var revisionNumber = downloadParameters.getRequestedRevision();
        var format = downloadParameters.getFormat();

        logger.info("Received download request from {} at {} for project {} (revision={}, format={})",
                    userId, formatAddr(req), projectId, revisionNumber.getValue(), format.getExtension());

        String accessToken = extractAccessToken(req);
        if (accessToken == null) {
            logger.warn("No access token found in download request from {} for project {}", userId, projectId);
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        Path tempFile = null;
        try {
            // 1. Request snapshot creation (or retrieval if cached)
            var coordinates = snapshotRequestSender.sendCreateSnapshotRequest(
                    projectId, revisionNumber, format, projectId.getId(), accessToken);

            // 2. Download snapshot from MinIO to temp file
            tempFile = snapshotDownloader.downloadToTempFile(coordinates);

            // 3. Stream to browser
            String fileName = projectId.getId() + "-ontologies." + format.getExtension() + ".zip";
            var fileTransferTask = new FileTransferTask(projectId, userId, tempFile, fileName, resp);
            fileTransferTask.call();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Download interrupted for project {}", projectId, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Download interrupted");
        } catch (Exception e) {
            logger.error("Error processing download for project {}", projectId, e);
            if (!resp.isCommitted()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Download failed");
            }
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    logger.warn("Failed to delete temp file {}", tempFile, e);
                }
            }
        }
    }

    private String extractAccessToken(HttpServletRequest req) {
        var principal = req.getUserPrincipal();
        if (principal instanceof KeycloakPrincipal<?>) {
            var keycloakPrincipal = (KeycloakPrincipal<?>) principal;
            return keycloakPrincipal.getKeycloakSecurityContext().getTokenString();
        }
        return null;
    }
}
