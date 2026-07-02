package edu.stanford.bmir.protege.web.server.download;

import java.io.IOException;

/**
 * Thrown when the snapshot service (via the API gateway) answers a
 * CreateSnapshot request with an error status.
 */
public class SnapshotRequestFailedException extends IOException {

    private final int statusCode;

    public SnapshotRequestFailedException(int statusCode) {
        super("Snapshot service returned error: " + statusCode);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
