package io.dataplatform.ingest.exception;

public abstract class IngestLayerException extends RuntimeException {
    protected IngestLayerException(String message) {
        super(message);
    }

    protected IngestLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
