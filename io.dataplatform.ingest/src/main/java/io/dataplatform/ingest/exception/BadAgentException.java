package io.dataplatform.ingest.exception;

public class BadAgentException extends IngestLayerException {
    public BadAgentException(String message) {
        super(message);
    }

    public BadAgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
