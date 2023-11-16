package io.dataplatform.common.pack;

import java.util.HashMap;
import java.util.Map;

public class StreamPack extends AbstractPack {

    private final Map<String, Object> fields = new HashMap<>(); // 소스 시스템에서 발생한 데이터

    protected StreamPack(long time, int agentId) {
        super(time, agentId);
    }

    protected StreamPack(long time, int agentId, String agentName) {
        super(time, agentId, agentName);
    }
}
