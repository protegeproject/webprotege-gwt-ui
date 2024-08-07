package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.shared.dispatch.DispatchService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-15
 */
public class MinioStorageService implements StorageService {


    private static final Logger logger = LoggerFactory.getLogger(MinioStorageService.class);

    private static final String BUCKET_NAME = "webprotege-uploads";

    private final MinioClient minioClient;

    private final DispatchService dispatchService;

    @Inject
    public MinioStorageService(MinioClient minioClient, DispatchService dispatchService) {
        this.minioClient = minioClient;
        this.dispatchService = dispatchService;
    }

    @Override
    public FileSubmissionId storeUpload(Path tempFile) {


        var fileIdentifier = UUID.randomUUID().toString();
        logger.debug("Storing uploaded file ({} MB) with an identifier of {}", getFileSizeInMB(tempFile), fileIdentifier);
        createBucketIfNecessary();
        uploadObject(tempFile, fileIdentifier);
        return FileSubmissionId.valueOf(fileIdentifier);
    }

    private long getFileSizeInMB(Path tempFile) {
        try {
            return Files.size(tempFile) / (1024 * 1024);
        } catch (IOException e) {
            return -1;
        }
    }

    private void uploadObject(Path tempFile, String fileIdentifier) {
        try {
            minioClient.uploadObject(UploadObjectArgs.builder()
                                                     .bucket(BUCKET_NAME)
                                                     .object(fileIdentifier)
                                                     .filename(tempFile.toString())
                                                     .build());
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            throw new StorageException(e);
        }
    }

    private void createBucketIfNecessary() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | IllegalArgumentException | InvalidKeyException e) {
            throw new StorageException(e);
        }
    }
}
