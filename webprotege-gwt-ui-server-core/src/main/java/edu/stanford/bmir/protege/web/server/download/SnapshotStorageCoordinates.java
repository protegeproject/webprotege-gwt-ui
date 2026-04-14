package edu.stanford.bmir.protege.web.server.download;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Coordinates identifying a snapshot file in MinIO object storage.
 */
public class SnapshotStorageCoordinates {

    @Nonnull
    private final String bucket;

    @Nonnull
    private final String name;

    public SnapshotStorageCoordinates(@Nonnull String bucket, @Nonnull String name) {
        this.bucket = checkNotNull(bucket);
        this.name = checkNotNull(name);
    }

    @Nonnull
    public String getBucket() {
        return bucket;
    }

    @Nonnull
    public String getName() {
        return name;
    }
}
