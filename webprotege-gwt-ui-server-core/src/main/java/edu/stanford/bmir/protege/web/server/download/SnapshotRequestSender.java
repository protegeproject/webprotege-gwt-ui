package edu.stanford.bmir.protege.web.server.download;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcEndPoint;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * Sends a CreateSnapshot request to the snapshot-generator-service via the
 * API gateway JSON-RPC endpoint and returns the MinIO storage coordinates.
 */
public class SnapshotRequestSender {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotRequestSender.class);

    private static final String CREATE_SNAPSHOT_CHANNEL = "webprotege.snapshots.CreateSnapshot";

    @Nonnull
    private final HttpClient httpClient;

    @Nonnull
    private final JsonRpcEndPoint jsonRpcEndPoint;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public SnapshotRequestSender(@Nonnull HttpClient httpClient,
                                 @Nonnull JsonRpcEndPoint jsonRpcEndPoint,
                                 @Nonnull ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.jsonRpcEndPoint = jsonRpcEndPoint;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a CreateSnapshot request and returns the storage coordinates (bucket + object name).
     *
     * @param projectId      the project to snapshot
     * @param revisionNumber the revision number to snapshot
     * @param format         the download format (e.g., owl, ttl, owx, omn, ofn)
     * @param fileName       the project display name used for ZIP folder naming
     * @param accessToken    the JWT bearer token for authentication
     * @return the snapshot storage coordinates
     */
    public SnapshotStorageCoordinates sendCreateSnapshotRequest(@Nonnull ProjectId projectId,
                                                                @Nonnull RevisionNumber revisionNumber,
                                                                @Nonnull DownloadFormat format,
                                                                @Nonnull String fileName,
                                                                @Nonnull String accessToken) throws IOException, InterruptedException {
        var requestBody = buildJsonRpcRequestBody(projectId, revisionNumber, format, fileName);

        var httpRequest = HttpRequest.newBuilder()
                .uri(jsonRpcEndPoint.getUri())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + accessToken)
                .build();

        logger.info("Sending CreateSnapshot request for project {} revision {}",
                     projectId, revisionNumber.getValue());

        var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 400) {
            logger.error("Error response from snapshot service: {} {}",
                         httpResponse.statusCode(), httpResponse.body());
            throw new SnapshotRequestFailedException(httpResponse.statusCode());
        }

        return parseStorageCoordinates(httpResponse.body());
    }

    private String buildJsonRpcRequestBody(ProjectId projectId,
                                           RevisionNumber revisionNumber,
                                           DownloadFormat format,
                                           String fileName) throws IOException {
        var root = objectMapper.createObjectNode();
        root.put("jsonrpc", "2.0");
        root.put("id", UUID.randomUUID().toString());
        root.put("method", CREATE_SNAPSHOT_CHANNEL);

        var params = objectMapper.createObjectNode();
        params.put("projectId", projectId.getId());
        params.put("revisionNumber", revisionNumber.getValue());
        // The backend DocumentFormat is identified by its MIME type on the wire
        params.put("documentFormat", format.getMimeType());
        params.put("fileName", fileName);
        root.set("params", params);

        return objectMapper.writeValueAsString(root);
    }

    private SnapshotStorageCoordinates parseStorageCoordinates(String responseBody) throws IOException {
        var tree = objectMapper.readTree(responseBody);
        var errorNode = tree.get("error");
        if (errorNode != null && !errorNode.isNull()) {
            var message = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
            throw new IOException("Snapshot creation failed: " + message);
        }
        var resultNode = tree.get("result");
        if (resultNode == null || resultNode.isNull()) {
            throw new IOException("No result in snapshot response");
        }
        var coordinatesNode = resultNode.get("snapshotStorageCoordinates");
        if (coordinatesNode == null || coordinatesNode.isNull()) {
            throw new IOException("No storage coordinates in snapshot response");
        }
        var bucket = coordinatesNode.get("bucket").asText();
        var name = coordinatesNode.get("name").asText();
        return new SnapshotStorageCoordinates(bucket, name);
    }
}
