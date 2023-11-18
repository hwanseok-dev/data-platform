package io.dataplatform.ingest.service;

import io.dataplatform.common.pack.StreamPack;
import io.dataplatform.meta.StreamIngestCountMeta;
import io.dataplatform.process.pre.StreamPackCore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StreamPackService {

    public void process(Integer agentId, String agentName, String resourceType, StreamPack pack){
        // update pack
        // TODO controller 앞단에서 이 작업을 미리 처리하도록 수정
        pack.updateTime();
        pack.updateAgentInfo(agentId, agentName);
        pack.putTag("resourceType", resourceType);

        // add to core
        StreamPackCore.getInstance().add(pack);

        // add meta
        StreamIngestCountMeta.getInstance().addCount(agentId, agentName, resourceType);
    }
}
