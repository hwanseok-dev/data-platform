package io.dataplatform.ingest.service;

import io.dataplatform.common.pack.StreamPack;
import io.dataplatform.metadata.ingest.StreamIngestCountMeta;
import io.dataplatform.process.core.StreamPackCore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StreamPackSerivce {

    public void process(Integer agentId, String agentName, String resourceType, StreamPack pack){
        StreamPackCore.getInstance().add(pack);
        StreamIngestCountMeta.getInstance().addCount(agentId, agentName, resourceType);
    }
}
