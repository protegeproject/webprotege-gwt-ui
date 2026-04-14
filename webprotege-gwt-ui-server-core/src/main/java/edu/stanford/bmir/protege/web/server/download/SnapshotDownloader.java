package edu.stanford.bmir.protege.web.server.download;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Downloads a snapshot ZIP file from MinIO to a local temporary file.
 */
public class SnapshotDownloader {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotDownloader.class);

    @Nonnull
    private final MinioClient minioClient;

    @Inject
    public SnapshotDownloader(@Nonnull MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Downloads the snapshot from MinIO to a temporary file.
     *
     * @param coordinates the MinIO bucket and object name
     * @return path to the downloaded temporary file (caller is responsible for cleanup)
     */
    @Nonnull
    public Path downloadToTempFile(@Nonnull SnapshotStorageCoordinates coordinates) throws IOException {
        var tempFile = Files.createTempFile("webprotege-download-", ".zip");
        logger.info("Downloading snapshot from MinIO (bucket={}, name={}) to {}",
                     coordinates.getBucket(), coordinates.getName(), tempFile);

        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(coordinates.getBucket())
                        .object(coordinates.getName())
                        .build());
             BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(tempFile))) {

            inputStream.transferTo(outputStream);
        } catch (Exception e) {
            Files.deleteIfExists(tempFile);
            throw new IOException("Failed to download snapshot from MinIO", e);
        }

        double sizeInMB = Files.size(tempFile) / (1024.0 * 1024);
        logger.info("Downloaded snapshot ({} MB) to {}", String.format("%.4f", sizeInMB), tempFile);
        return tempFile;
    }
}
