package io.dataplatform.meta;

public interface CountMeta {

    void addCount(Integer agentId, String agentName, String resourceType);

    void addErrorCount(Integer agentId, String agentName, String resourceType);
}
