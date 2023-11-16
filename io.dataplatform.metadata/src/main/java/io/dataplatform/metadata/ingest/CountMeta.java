package io.dataplatform.metadata.ingest;

public interface CountMeta {

    void addCount(Integer agentId, String agentName, String resourceType);

    void addErrorCount(Integer agentId, String agentName, String resourceType);
}
